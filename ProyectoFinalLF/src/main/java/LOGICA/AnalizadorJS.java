package LOGICA;

import Automatas.AutomataIdentificadorJS;
import java.util.ArrayList;

public class AnalizadorJS {

    private ArrayList<String> contenidoJS = new ArrayList<>();
    private ArrayList<String> contenidoOptimizadoJS;
    //Abajo los token son para los reportes
    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<String> errores;
    private ArrayList<Token> erroresToken;

    //Automatas
    private AutomataIdentificadorJS automataIdentificadorJS = new AutomataIdentificadorJS();

    //Abajo es el primer <>
    private boolean estadoAbierto = false;
    //Abajo es para saber si completó </>
    private boolean estadoAbiertoExpandido = false;
    //Es para saber si es texto
    private boolean dentroLlaves = false;

    public ArrayList<String> getContenidoJS() {
        return contenidoJS;
    }

    public ArrayList<String> getContenidoOptimizadoJS() {
        return contenidoOptimizadoJS;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setErrores(ArrayList<String> errores, ArrayList<Token> erroresToken) {
        this.errores = errores;
        this.erroresToken = erroresToken;
    }

    public void procesarLinea(String linea, int indiceLinea) {
        contenidoJS.add(linea);
        conseguirTokens(linea, indiceLinea);

    }

    public void conseguirTokens(String linea, int indiceLinea) {

        linea = modificarLinea(linea);

        extraerToken(linea, indiceLinea, tokens);

    }

    public String modificarLinea(String linea) {
        linea = linea.replace("(", " ( ");
        linea = linea.replace(")", " ) ");
        linea = linea.replace("{", " { ");
        linea = linea.replace("}", " } ");

        if (!linea.contains(">>[")) {
            linea = linea.replace(">", " > ");
        }

        if (!linea.contains("//")) {
            linea = linea.replace(" /", "/ ");
        }
        linea = linea.replace(":", " : ");
        linea = linea.replace(";", " ; ");
        linea = linea.replace(",", " , ");
        linea = linea.replace(".", " . ");

        //Abajo recorta espacios vacíos del inicio
        if (linea.startsWith(" ")) {
            linea = linea.substring(1);
        }
        return linea;
    }

    public void extraerToken(String linea, int indiceLinea, ArrayList<Token> tokens) {
        String[] palabras = linea.split(" ");
        Token tokenNuevo = null;
        Token tokenError = null;
        boolean isComment = false;

        String tipo = "";
        for (int indicePalabra = 0; indicePalabra < palabras.length; indicePalabra++) {
            String palabra = palabras[indicePalabra];

            if (isComment) {
                tipo = "Comentario";
            } else if (palabra.equals(">>[js]")) {
                tipo = "Estado";
            } else if (palabra.equals("{")) {
                dentroLlaves = true;
                tipo = "Símbolo_de_Apertura";
            } else if (palabra.equals("}") && dentroLlaves) {
                dentroLlaves = false;
                tipo = "Etiqueta_de_Cierre";
            } else if (palabra.contains("//")) {
                tipo = "Comentario";
                isComment = true;
            } else if ((palabra.equals("+") || palabra.equals("-")
                    || palabra.equals("*") || palabra.equals("/"))) {
                tipo = "Aritméticos";
            } else if ((palabra.equals("==") || palabra.equals("<")
                    || palabra.equals(">") || palabra.equals("<=")
                    || palabra.equals(">=") || palabra.equals("!="))) {
                tipo = "Relacionales";
            } else if (palabra.equals("||") || palabra.equals("&&")
                    || palabra.equals("!") /*|| palabra.equals(" ")*/) {
                tipo = "Lógicos";
            } else if (palabra.equals("++") || palabra.equals("--")) {
                tipo = "Incrementales";
            } else if (!palabra.contains(".") && isWholeNumber(palabra)) {
                tipo = "Entero";
            } else if (palabra.contains(".") && isDecimal(palabra)) {
                tipo = "Decimal";
            } else if ((palabra.startsWith("\"") && palabra.endsWith("\""))
                    || (palabra.startsWith("'") && palabra.endsWith("'"))
                    || (palabra.startsWith("`") && palabra.endsWith("´"))
                    || (palabra.startsWith("“") && palabra.endsWith("”"))
                    || (palabra.startsWith("‘") && palabra.endsWith("’"))
                    || (palabra.startsWith("`") && palabra.endsWith("`"))
                    || (palabra.startsWith("``") && palabra.endsWith("``"))
                    || (palabra.startsWith("``") && palabra.endsWith("´´"))) {
                tipo = "Cadena";
            } else if (palabra.equals("true") || palabra.equals("false")) {
                tipo = "Booleano";
            } else if (palabra.equals("function") || palabra.equals("const")
                    || palabra.equals("let") || palabra.equals("document")
                    || palabra.equals("event") || palabra.equals("alert")
                    || palabra.equals("for") || palabra.equals("while")
                    || palabra.equals("if") || palabra.equals("else")
                    || palabra.equals("return") || palabra.equals("console.log")
                    || palabra.equals("null")) {
                tipo = "Palabra Reservada";
            } else if (palabra.equals("(") || palabra.equals(")")) {
                tipo = "Paréntesis";
            } else if (palabra.equals("[") || palabra.equals("]")) {
                tipo = "Corchetes";
            } else if (palabra.equals("{") || palabra.equals("}")) {
                tipo = "Llaves";
            } else if (palabra.equals("=")) {
                tipo = "Asignación";
            } else if (palabra.equals(";")) {
                tipo = "Punto y Coma";
            } else if (palabra.equals(",")) {
                tipo = "Coma";
            } else if (palabra.equals(".")) {
                tipo = "Punto";
            } else if (palabra.equals(":")) {
                tipo = "Dos Puntos";
            } else if (automataIdentificadorJS.verificarPerteneceAlAutomata(palabra)) {
                tipo = "Identificador";
            } else if (palabra.isBlank()) {

            } else {
                tipo = "Error";
                //System.out.println("Texto con el error: " + palabra);
                String error = "ERROR en JS: \"" + palabra + "\", Fila: " + (indiceLinea + 1) + ", Columna: " + (indicePalabra + 1);
                errores.add(error);
                tokenError = new Token(palabra, palabra, "Javascript", tipo, indiceLinea + 1, indicePalabra + 1);
                erroresToken.add(tokenError);
            }

            //Crear Token
            tokenNuevo = new Token(palabra, palabra, "Javascript", tipo, indiceLinea  + 1, indicePalabra + 1);

            tokens.add(tokenNuevo);

        }
    }

    public void optimizarCodigo(ArrayList<String> txtOptimizado, ArrayList<Token> tokensEliminados/*, String[] lineasOriginal*/) {

        contenidoOptimizadoJS = new ArrayList<>();

        for (int indiceLinea = 0; indiceLinea < contenidoJS.size(); indiceLinea++) {
            //for (int indiceLinea = 0; indiceLinea < contenidoHTML.size(); indiceLinea++) {
            String linea = contenidoJS.get(indiceLinea);
            String lineaOriginal = linea;
            linea = modificarLinea(linea);

            if (!(linea.isBlank()) && !(linea.contains("//"))) {

                if (linea.contains(">>[js]") || linea.contains(">>[css]") || linea.contains(">>[html]")) {
                    txtOptimizado.add("");
                    txtOptimizado.add(lineaOriginal);
                    contenidoOptimizadoJS.add("");
                } else {
                    txtOptimizado.add(lineaOriginal);
                    contenidoOptimizadoJS.add(lineaOriginal);
                }
            } else if (linea.contains("//")) {
                extraerToken(linea, indiceLinea, tokensEliminados);

            }
        }
    }

    public void imprimirTokens() {
        System.out.println("IMPRIMIENDO TOKENS");
        for (Token token : tokens) {
            System.out.println(token);
        }
    }

    public boolean isJS(String palabra) {


        if (palabra.equals("{")) {
        } else if (palabra.equals("}")) {
        } else if (palabra.contains("//")) {
        } else if ((palabra.equals("+") || palabra.equals("-")
                || palabra.equals("*") || palabra.equals("/"))) {
        } else if ((palabra.equals("==") || palabra.equals("<")
                || palabra.equals(">") || palabra.equals("<=")
                || palabra.equals(">=") || palabra.equals("!="))) {
        } else if (palabra.equals("||") || palabra.equals("&&")
                || palabra.equals("!") /*|| palabra.equals(" ")*/) {
        } else if (palabra.equals("++") || palabra.equals("--")) {
        } else if (!palabra.contains(".") && isWholeNumber(palabra)) {
        } else if (palabra.contains(".") && isDecimal(palabra)) {
        } else if ((palabra.startsWith("\"") && palabra.endsWith("\""))
                || (palabra.startsWith("'") && palabra.endsWith("'"))
                || (palabra.startsWith("`") && palabra.endsWith("´"))
                || (palabra.startsWith("“") && palabra.endsWith("”"))
                || (palabra.startsWith("‘") && palabra.endsWith("’"))
                || (palabra.startsWith("`") && palabra.endsWith("`"))
                || (palabra.startsWith("``") && palabra.endsWith("``"))
                || (palabra.startsWith("``") && palabra.endsWith("´´"))) {
        } else if (palabra.equals("true") || palabra.equals("false")) {
        } else if (palabra.equals("function") || palabra.equals("const")
                || palabra.equals("let") || palabra.equals("document")
                || palabra.equals("event") || palabra.equals("alert")
                || palabra.equals("for") || palabra.equals("while")
                || palabra.equals("if") || palabra.equals("else")
                || palabra.equals("return") || palabra.equals("console.log")
                || palabra.equals("null")) {
        } else if (palabra.equals("(") || palabra.equals(")")) {
        } else if (palabra.equals("[") || palabra.equals("]")) {
        } else if (palabra.equals("{") || palabra.equals("}")) {
        } else if (palabra.equals("=")) {
        } else if (palabra.equals(";")) {
        } else if (palabra.equals(",")) {
        } else if (palabra.equals(".")) {
        } else if (palabra.equals(":")) {
        } else if (automataIdentificadorJS.verificarPerteneceAlAutomata(palabra)) {
        } else if (palabra.isBlank()) {
        } else {
            return false;
        }

        return true;

    }
    
    public boolean isWholeNumber(String palabra) {
        try {
            int numero = Integer.parseInt(palabra);
            
            return true;
        } catch (Exception e) {
        }
        
        
        return false;
    }
    
    public boolean isDecimal(String palabra) {
        
        try {
            double numero = Double.parseDouble(palabra);
            
            return true;
        } catch (Exception e) {
        }
        
        return false;
    }
}
