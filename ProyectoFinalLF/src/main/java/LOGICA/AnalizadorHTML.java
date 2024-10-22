package LOGICA;

import java.util.ArrayList;

public class AnalizadorHTML {

    //El contenidoHTML es para meterlo todo al HTML que se exporta
    private ArrayList<String> contenidoHTML = new ArrayList<>();
    private ArrayList<String> contenidoOptimizadoHTML;
    //Abajo los token son para los reportes
    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<String> errores;
    private ArrayList<Token> erroresToken;

    //Abajo es el primer <>
    private boolean estadoAbierto = false;
    //Abajo es para saber si completó </>
    private boolean estadoAbiertoExpandido = false;
    //Es para saber si es texto
    private boolean esTexto = false;

    public void setErrores(ArrayList<String> errores, ArrayList<Token> erroresToken) {
        this.errores = errores;
        this.erroresToken = erroresToken;

    }

    public ArrayList<String> getContenidoHTML() {
        return contenidoHTML;
    }

    public ArrayList<String> getContenidoOptimizadoHTML() {
        return contenidoOptimizadoHTML;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void procesarLinea(String linea, int indiceLinea, ArrayList<String> errores) {
        contenidoHTML.add(traducir(linea));
        conseguirTokens(linea, indiceLinea);

    }

    public void optimizarCodigo(ArrayList<String> txtOptimizado, ArrayList<Token> tokensEliminados/*, String[] lineasOriginal*/) {
        contenidoOptimizadoHTML = new ArrayList<>();

        for (int indiceLinea = 0; indiceLinea < contenidoHTML.size(); indiceLinea++) {
            //for (int indiceLinea = 0; indiceLinea < contenidoHTML.size(); indiceLinea++) {
            String linea = contenidoHTML.get(indiceLinea);
            String lineaOriginal = linea;
            linea = modificarLinea(linea);

            if (!(linea.isBlank()) && !(linea.contains("//"))) {

                if (linea.contains(">>[js]") || linea.contains(">>[css]") || linea.contains(">>[html]")) {
                    txtOptimizado.add("");
                    txtOptimizado.add(lineaOriginal);
                    contenidoOptimizadoHTML.add("");
                } else {
                    txtOptimizado.add(lineaOriginal);
                    contenidoOptimizadoHTML.add(lineaOriginal);
                }
            } else if (linea.contains("//")) {
                extraerToken(linea, indiceLinea, tokensEliminados);

            }
        }
    }

    public void conseguirTokens(String linea, int indiceLinea) {

        linea = modificarLinea(linea);

        extraerToken(linea, indiceLinea, tokens);

    }

    public String modificarLinea(String linea) {
        linea = linea.replace("<", " < ");

        if (!linea.contains(">>[")) {
            linea = linea.replace(">", " > ");
        }

        if (!linea.contains("//")) {
            linea = linea.replace(" /", "/ ");
        }
        linea = linea.replace("=", " = ");

        //Abajo recorta espacios vacíos del inicio
        if (linea.startsWith(" ")) {
            linea = linea.substring(1);
        }
        return linea;
    }

    public void extraerToken(String linea, int indiceLinea, ArrayList<Token> tokens) {

        String[] palabras = linea.split(" ");
        //System.out.println("PRUEBA" + linea);
        Token tokenNuevo = null;
        Token tokenError = null;
        boolean isComment = false;

        String tipo = "";
        for (int indicePalabra = 0; indicePalabra < palabras.length; indicePalabra++) {
            String palabra = palabras[indicePalabra];

            if (isComment) {
                tipo = "Comentario";
            } else if (palabra.equals(">>[html]")) {
                tipo = "Estado";
            } else if (palabra.equals("<")) {
                estadoAbierto = true;
                tipo = "Etiqueta_de_Apertura";
            } else if (palabra.equals(">") && estadoAbierto) {
                estadoAbierto = false;
                esTexto = true;
                tipo = "Etiqueta_de_Cierre";
            } else if (palabra.equals("</")) {
                estadoAbiertoExpandido = true;
                esTexto = false;
                tipo = "Etiqueta_de_Cierre";
            } else if (estadoAbiertoExpandido && (palabra.equals(">"))) {
                estadoAbiertoExpandido = false;
                tipo = "Etiqueta_de_Cierre";
            } else if (palabra.contains("//")) {
                tipo = "Comentario";
                isComment = true;
            } else if ((estadoAbierto || estadoAbiertoExpandido) && (palabra.equals("principal") || palabra.equals("encabezado")
                    || palabra.equals("navegacion") || palabra.equals("apartado")
                    || palabra.equals("listaordenada") || palabra.equals("listadesordenada")
                    || palabra.equals("itemlista") || palabra.equals("anclaje")
                    || palabra.equals("contenedor") || palabra.equals("seccion")
                    || palabra.equals("articulo") || palabra.equals("titulo1")
                    || palabra.equals("titulo2") || palabra.equals("titulo3")
                    || palabra.equals("titulo4") || palabra.equals("titulo5")
                    || palabra.equals("titulo6") || palabra.equals("parrafo")
                    || palabra.equals("span") || palabra.equals("entrada")
                    || palabra.equals("formulario") || palabra.equals("label")
                    || palabra.equals("area") || palabra.equals("boton")
                    || palabra.equals("piepagina"))) {

                tipo = "Etiqueta";
            } else if (estadoAbierto && (palabra.equals("class") || palabra.equals("=")
                    || palabra.equals("href") || palabra.equals("onClick")
                    || palabra.equals("id") || palabra.equals("style")
                    || palabra.equals("type") || palabra.equals("placeholder")
                    || palabra.equals("required") || palabra.equals("name"))) {

                tipo = "Palabra Reservada";
            } else if (estadoAbierto && (palabra.startsWith("\"") && palabra.endsWith("\""))) {
                tipo = "Cadena";

            } else if (esTexto /*!estadoAbierto && estadoAbiertoExpandido*/) {
                tipo = "Texto";
            } else if (palabra.isBlank()) {

            } else {

                tipo = "Error";
                //System.out.println("Texto con el error: " + palabra);
                String error = "ERROR en HTML: \"" + palabra + "\", Fila: " + (indiceLinea + 1) + ", Columna: " + (indicePalabra + 1);
                errores.add(error);
                tokenError = new Token(palabra, palabra, "Html", tipo, indiceLinea + 1, indicePalabra + 1);
                erroresToken.add(tokenError);
            }

            //Crear Token
            tokenNuevo = new Token(palabra, palabra, "Html", tipo, indiceLinea + 1, indicePalabra + 1);

            tokens.add(tokenNuevo);
        }
    }

    public String traducir(String linea) {
        String lineaTraducida = linea;

//        System.out.println("Linea no traducida: ");
//        System.out.println(linea);
        lineaTraducida = lineaTraducida.replace("principal", "main");
        lineaTraducida = lineaTraducida.replace("encabezado", "header");
        lineaTraducida = lineaTraducida.replace("navegacion", "nav");
        lineaTraducida = lineaTraducida.replace("apartado", "aside");
        lineaTraducida = lineaTraducida.replace("listaordenada", "ul");
        lineaTraducida = lineaTraducida.replace("listadesordenada", "ol");
        lineaTraducida = lineaTraducida.replace("itemlista", "li");
        lineaTraducida = lineaTraducida.replace("anclaje", "a");
        lineaTraducida = lineaTraducida.replace("contenedor", "div");
        lineaTraducida = lineaTraducida.replace("seccion", "section");
        lineaTraducida = lineaTraducida.replace("articulo", "article");
        lineaTraducida = lineaTraducida.replace("titulo", "h");
        lineaTraducida = lineaTraducida.replace("parrafo", "p");
        lineaTraducida = lineaTraducida.replace("entrada", "input");
        lineaTraducida = lineaTraducida.replace("formulario", "form");
        lineaTraducida = lineaTraducida.replace("area", "textarea");
        lineaTraducida = lineaTraducida.replace("boton", "button");
        lineaTraducida = lineaTraducida.replace("piepagina", "footer");

//        System.out.println("Linea traducida: ");
//        System.out.println(lineaTraducida);
        return lineaTraducida;

    }

    public void imprimirTokens() {
        System.out.println("IMPRIMIENDO TOKENS");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public boolean isHTML(String palabra) {

        if (palabra.equals(">>[html]")) {
        } else if (palabra.equals("<")) {
        } else if (palabra.equals(">")) {
        } else if (palabra.equals("</")) {
        } else if ((palabra.equals("principal") || palabra.equals("encabezado")
                || palabra.equals("navegacion") || palabra.equals("apartado")
                || palabra.equals("listaordenada") || palabra.equals("listadesordenada")
                || palabra.equals("itemlista") || palabra.equals("anclaje")
                || palabra.equals("contenedor") || palabra.equals("seccion")
                || palabra.equals("articulo") || palabra.equals("titulo1")
                || palabra.equals("titulo2") || palabra.equals("titulo3")
                || palabra.equals("titulo4") || palabra.equals("titulo5")
                || palabra.equals("titulo6") || palabra.equals("parrafo")
                || palabra.equals("span") || palabra.equals("entrada")
                || palabra.equals("formulario") || palabra.equals("label")
                || palabra.equals("area") || palabra.equals("boton")
                || palabra.equals("piepagina"))) {

        } else if ((palabra.equals("class") || palabra.equals("=")
                || palabra.equals("href") || palabra.equals("onClick")
                || palabra.equals("id") || palabra.equals("style")
                || palabra.equals("type") || palabra.equals("placeholder")
                || palabra.equals("required") || palabra.equals("name"))) {

        } else if ((palabra.startsWith("\"") && palabra.endsWith("\""))) {

        } else if (esTexto /*!estadoAbierto && estadoAbiertoExpandido*/) {
        } else if (palabra.isBlank()) {

        } else {
            return false;
        }
        
        return true;
    }
}
