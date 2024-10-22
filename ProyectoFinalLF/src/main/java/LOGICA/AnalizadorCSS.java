package LOGICA;

import Automatas.AutomataDeClase;
import Automatas.AutomataDeId;
import Automatas.AutomataHexadecimal;
import Automatas.AutomataIdentificadorCSS;
import Automatas.AutomataRGBA;
import java.util.ArrayList;

public class AnalizadorCSS {

    private ArrayList<String> contenidoCSS = new ArrayList<>();
    private ArrayList<String> contenidoOptimizadoCSS;
    //Abajo los token son para los reportes
    private ArrayList<Token> tokens = new ArrayList<>();
    private ArrayList<String> errores;
    private ArrayList<Token> erroresToken;

    //Automatas
    private final AutomataDeClase automataDeClase = new AutomataDeClase();
    private final AutomataDeId automataDeId = new AutomataDeId();
    private final AutomataHexadecimal automataHexa = new AutomataHexadecimal();
    private final AutomataRGBA automataRGBA = new AutomataRGBA();
    private final AutomataIdentificadorCSS automataIdentificadorCSS = new AutomataIdentificadorCSS();

    //Es para saber si está adentro de {}
    private boolean dentroLlaves = false;
    //Abajo es para saber si se está después de : dentro de {}
    private boolean despuesDosPuntos = false;

    public ArrayList<String> getContenidoOptimizadoCSS() {
        return contenidoOptimizadoCSS;
    }

    public ArrayList<String> getContenidoCSS() {
        return contenidoCSS;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setErrores(ArrayList<String> errores, ArrayList<Token> erroresToken) {
        this.errores = errores;
        this.erroresToken = erroresToken;
    }

    public void procesarLinea(String linea, int indiceLinea) {
        contenidoCSS.add(linea);
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
        linea = linea.replace(":", " :");
        linea = linea.replace(";", " ;");
        linea = linea.replace(",", " ,");

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
        boolean isRGBA = false;
        String expresionRGBA = "";
        int indiceRGBA = 0;

        String tipo = "";
        for (int indicePalabra = 0; indicePalabra < palabras.length; indicePalabra++) {
            String palabra = palabras[indicePalabra];
            
            if (isRGBA) {
                expresionRGBA+=palabra;
            }

            if (isComment) {
                tipo = "Comentario";
            } else if (palabra.equals(">>[css]")) {
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
            } else if (palabra.equals("*")) {
                tipo = "Universal";
            } else if (palabra.equals("body") || palabra.equals("header")
                    || palabra.equals("main") || palabra.equals("nav")
                    || palabra.equals("aside") || palabra.equals("div")
                    || palabra.equals("ul") || palabra.equals("ol")
                    || palabra.equals("li") || palabra.equals("a")
                    || palabra.equals("p") || palabra.equals("h1")
                    || palabra.equals("h2") || palabra.equals("h3")
                    || palabra.equals("h4") || palabra.equals("h5")
                    || palabra.equals("h6") || palabra.equals("parrafo")
                    || palabra.equals("span") || palabra.equals("label")
                    || palabra.equals("textarea") || palabra.equals("button")
                    || palabra.equals("section") || palabra.equals("article")
                    || palabra.equals("footer")) {

                tipo = "Etiqueta";
            } else if (palabra.equals(">") || palabra.equals("+")
                    || palabra.equals("~") /*|| palabra.equals(" ")*/) {
                tipo = "Combinadores";
            } else if (dentroLlaves && (palabra.equals("color") || palabra.equals("background-color")
                    || palabra.equals("background") || palabra.equals("font-size")
                    || palabra.equals("font-weight") || palabra.equals("font-family")
                    || palabra.equals("font-align") || palabra.equals("width")
                    || palabra.equals("height") || palabra.equals("min-width")
                    || palabra.equals("min-height") || palabra.equals("max-width")
                    || palabra.equals("max-height") || palabra.equals("display")
                    || palabra.equals("inline") || palabra.equals("block")
                    || palabra.equals("inline-block") || palabra.equals("flex")
                    || palabra.equals("grid") || palabra.equals("none")
                    || palabra.equals("margin") || palabra.equals("border")
                    || palabra.equals("padding") || palabra.equals("content")
                    || palabra.equals("border-color") || palabra.equals("border-style")
                    || palabra.equals("border-width") || palabra.equals("border-top")
                    || palabra.equals("border-bottom") || palabra.equals("border-left")
                    || palabra.equals("border-right") || palabra.equals("box-sizing")
                    || palabra.equals("border-box") || palabra.equals("position")
                    || palabra.equals("static") || palabra.equals("relative")////////////////////////////
                    || palabra.equals("absolute") || palabra.equals("sticky")
                    || palabra.equals("fixed") || palabra.equals("top")
                    || palabra.equals("bottom") || palabra.equals("left")
                    || palabra.equals("right") || palabra.equals("z-index")
                    || palabra.equals("justify-content") || palabra.equals("align-items")
                    || palabra.equals("border-radius") || palabra.equals("auto")
                    || palabra.equals("float") || palabra.equals("list-style")
                    || palabra.equals("text-align") || palabra.equals("box-shadow"))) {
                tipo = "Reglas";
            } else if (automataHexa.verificarPerteneceAlAutomata(palabra)) {
                tipo = "Colores Hex.";
            } else if (palabra.startsWith("rgba")) {
                isRGBA = true;
                expresionRGBA+=palabra;
                
            } else if (palabra.equals(")") && isRGBA && automataRGBA.verificarPertenerAlAutomata(expresionRGBA)) {
                tipo = "Colores RGBA";
                indiceRGBA = indiceLinea;
                
                if (palabra.equals(")")) {
                    isRGBA = false;
                }
                
                tokenNuevo = new Token(expresionRGBA, expresionRGBA, "CSS", tipo, indiceRGBA + 1, indicePalabra + 1);
                tokens.add(tokenNuevo);
                
            } else if (palabra.equals("px") || palabra.equals("%")
                    || palabra.equals("rem") || palabra.equals("em")
                    || palabra.equals("vw") || palabra.equals("vh")
                    || palabra.equals(":hover") || palabra.equals(":active")
                    || palabra.equals(":not()") || palabra.equals(":nth-child()")
                    || palabra.equals("odd") || palabra.equals("even")
                    || palabra.equals("::before") || palabra.equals("::after")
                    || palabra.equals(":") || palabra.equals(";")
                    || palabra.equals("::before") || palabra.equals("::after")
                    || palabra.equals(":") || palabra.equals(";")
                    || palabra.equals(",") || palabra.equals("(")
                    || palabra.equals(")")) {
                tipo = "Otros";
            } else if (/*estadoAbierto &&*/(palabra.startsWith("'") && palabra.endsWith("'"))) {
                tipo = "Cadena";
            } else if (palabra.isBlank()) {
            } else if (automataDeClase.verificarPerteneceAlAutomata(palabra)) {
                tipo = "De clase";
            } else if (automataDeId.verificarPerteneceAlAutomata(palabra)) {
                tipo = "De Id";
            } else if (automataIdentificadorCSS.verificarPerteneceAlAutomata(palabra)) {
                tipo = "Identificador";
            } else {
                tipo = "Error";
                //System.out.println("Texto con el error: " + palabra);
                String error = "ERROR en CSS: \"" + palabra + "\", Fila: " + (indiceLinea + 1) + ", Columna: " + (indicePalabra + 1);
                errores.add(error);
                tokenError = new Token(palabra, palabra, "CSS", tipo, indiceLinea + 1, indicePalabra + 1);
                erroresToken.add(tokenError);
            }

            //Crear Token
            tokenNuevo = new Token(palabra, palabra, "CSS", tipo, indiceLinea + 1, indicePalabra + 1);

            tokens.add(tokenNuevo);

        }
    }

    public void optimizarCodigo(ArrayList<String> txtOptimizado, ArrayList<Token> tokensEliminados/*, String[] lineasOriginal*/) {

        contenidoOptimizadoCSS = new ArrayList<>();

        for (int indiceLinea = 0; indiceLinea < contenidoCSS.size(); indiceLinea++) {
            //for (int indiceLinea = 0; indiceLinea < contenidoHTML.size(); indiceLinea++) {
            String linea = contenidoCSS.get(indiceLinea);
            String lineaOriginal = linea;
            linea = modificarLinea(linea);

            if (!(linea.isBlank()) && !(linea.contains("//"))) {

                if (linea.contains(">>[js]") || linea.contains(">>[css]") || linea.contains(">>[html]")) {
                    txtOptimizado.add("");
                    txtOptimizado.add(lineaOriginal);
                    contenidoOptimizadoCSS.add("");
                } else {
                    txtOptimizado.add(lineaOriginal);
                    contenidoOptimizadoCSS.add(lineaOriginal);
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

    public boolean isCSS(String palabra) {

        if (palabra.equals("{")) {
        } else if (palabra.equals("}")) {
        } else if (palabra.contains("//")) {
        } else if (palabra.equals("*")) {
        } else if (palabra.equals("body") || palabra.equals("header")
                || palabra.equals("main") || palabra.equals("nav")
                || palabra.equals("aside") || palabra.equals("div")
                || palabra.equals("ul") || palabra.equals("ol")
                || palabra.equals("li") || palabra.equals("a")
                || palabra.equals("p") || palabra.equals("h1")
                || palabra.equals("h2") || palabra.equals("h3")
                || palabra.equals("h4") || palabra.equals("h5")
                || palabra.equals("h6") || palabra.equals("parrafo")
                || palabra.equals("span") || palabra.equals("label")
                || palabra.equals("textarea") || palabra.equals("button")
                || palabra.equals("section") || palabra.equals("article")
                || palabra.equals("footer")) {
        } else if (palabra.equals(">") || palabra.equals("+")
                || palabra.equals("~") /*|| palabra.equals(" ")*/) {
        } else if ((palabra.equals("color") || palabra.equals("background-color")
                || palabra.equals("background") || palabra.equals("font-size")
                || palabra.equals("font-weight") || palabra.equals("font-family")
                || palabra.equals("font-align") || palabra.equals("width")
                || palabra.equals("height") || palabra.equals("min-width")
                || palabra.equals("min-height") || palabra.equals("max-width")
                || palabra.equals("max-height") || palabra.equals("display")
                || palabra.equals("inline") || palabra.equals("block")
                || palabra.equals("inline-block") || palabra.equals("flex")
                || palabra.equals("grid") || palabra.equals("none")
                || palabra.equals("margin") || palabra.equals("border")
                || palabra.equals("padding") || palabra.equals("content")
                || palabra.equals("border-color") || palabra.equals("border-style")
                || palabra.equals("border-width") || palabra.equals("border-top")
                || palabra.equals("border-bottom") || palabra.equals("border-left")
                || palabra.equals("border-right") || palabra.equals("box-sizing")
                || palabra.equals("border-box") || palabra.equals("position")
                || palabra.equals("static") || palabra.equals("relative")////////////////////////////
                || palabra.equals("absolute") || palabra.equals("sticky")
                || palabra.equals("fixed") || palabra.equals("top")
                || palabra.equals("bottom") || palabra.equals("left")
                || palabra.equals("right") || palabra.equals("z-index")
                || palabra.equals("justify-content") || palabra.equals("align-items")
                || palabra.equals("border-radius") || palabra.equals("auto")
                || palabra.equals("float") || palabra.equals("list-style")
                || palabra.equals("text-align") || palabra.equals("box-shadow"))) {
        } else if (palabra.equals("px") || palabra.equals("%")
                || palabra.equals("rem") || palabra.equals("em")
                || palabra.equals("vw") || palabra.equals("vh")
                || palabra.equals(":hover") || palabra.equals(":active")
                || palabra.equals(":not()") || palabra.equals(":nth-child()")
                || palabra.equals("odd") || palabra.equals("even")
                || palabra.equals("::before") || palabra.equals("::after")
                || palabra.equals(":") || palabra.equals(";")
                || palabra.equals("::before") || palabra.equals("::after")
                || palabra.equals(":") || palabra.equals(";")
                || palabra.equals(",") || palabra.equals("(")
                || palabra.equals(")")) {
        } else if ((palabra.startsWith("'") && palabra.endsWith("'"))) {
        } else if (palabra.isBlank()) {
        } else if (palabra.startsWith("#") || palabra.startsWith("rgba")) {
        } else if (automataDeClase.verificarPerteneceAlAutomata(palabra)) {
        } else if (automataDeId.verificarPerteneceAlAutomata(palabra)) {
        } else if (automataIdentificadorCSS.verificarPerteneceAlAutomata(palabra)) {
        } else {
            return false;
        }
        
        return true;
    }
}
