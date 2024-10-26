package AutomatasDML;

import LOGICA.Token;
import java.util.List;

public class AutomataInsert {

    private List<Token> tokens;
    private String estado = "1";
    private char estadoTipoDeDatos = 'A';

    public AutomataInsert(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {
            Token token = comandoIndividual.get(indiceToken);
            System.out.println("Evaluando token " + token.getNombre() + " tipo: " + token.getTipo() + " estado " + estado);

            switch (estado) {
                case "1" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "INSERT" ->
                            "2";
                        default ->
                            "E";
                    };
                }
                case "2" -> {
                    estado = switch (token.getNombre()) {
                        case "INTO" ->
                            "3";
                        default ->
                            "E";
                    };
                }
                case "3" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "4";
                        default ->
                            "E";
                    };
                }
                case "4" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "(" ->
                            "5";
                        default ->
                            "E";
                    };
                }
                case "5" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "6";
                        default ->
                            "E";
                    };
                }
                case "6" -> {
                    estado = switch (token.getNombre()) {
                        case ")" ->
                            "7";
                        case "," ->
                            "5";
                        default ->
                            "E";
                    };
                }
                case "7" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "VALUES" ->
                            "8";
                        default ->
                            "E";
                    };
                }
                case "8" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "(" ->
                            "9";
                        default ->
                            "E";
                    };
                }
                case "9" ->
                    comprobarEsEstructuraDato(token, indiceToken);

                case "10" -> {
                    estado = switch (token.getNombre()) {
                        case "," ->
                            "9";
                        case ")" ->
                            "11";
                        default ->
                            "E";
                    };
                }
                case "11" -> {
                    estado = switch (token.getNombre()) {
                        case ";" ->
                            "12";
                        case "," ->
                            "8";
                        default ->
                            "E";
                    };
                }
                case "E" -> {
                    System.out.println("Token en el que detectó error DML INSERT: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    return false;
                    //break;
                }
                //break;

            }
        }

        if (estado.equals("12")) {
            System.out.println("Cumpre con formato INSERT");
            return true;
        } else {
            System.out.println("No cumple con formato INSERT");
        }

        return false;
    }

    private void comprobarEsEstructuraDato(Token tokenIndividual, int indice) {
        System.out.println("Comprobando estructura de dato " + tokenIndividual.getNombre() + " estado tipo de dato " + estadoTipoDeDatos);
        switch (estadoTipoDeDatos) {
            case 'A':
                //Abajo entra a la letra y en base a eso cambia de estado

                if (tokenIndividual.getTipo().equals("ENTERO") || tokenIndividual.getTipo().equals("DECIMAL")) {

                    if (!(tokens.get(indice + 1).getNombre().equals("+") || tokens.get(indice + 1).getNombre().equals("*")
                            || tokens.get(indice + 1).getNombre().equals("-") || tokens.get(indice + 1).getNombre().equals("/")
                            || tokens.get(indice + 1).getNombre().equals("OR") || tokens.get(indice + 1).getNombre().equals("<")
                            || tokens.get(indice + 1).getNombre().equals(">"))) {
                        estado = "10";
                        estadoTipoDeDatos = 'A';
                    } else {
                        estadoTipoDeDatos = 'B';
                    }

                } else if (tokenIndividual.getNombre().equals("(")) {
                    estadoTipoDeDatos = 'C';
                } else if (tokenIndividual.getTipo().equals("FECHA")) {
                    
                    if (!(tokens.get(indice + 1).getNombre().equals("<") || tokens.get(indice + 1).getNombre().equals(">"))) {
                        estadoTipoDeDatos = 'A';
                        estado = "10";
                    } else {
                        estadoTipoDeDatos = 'F';
                    }
                    
                } else if (tokenIndividual.getNombre().equals("TRUE") || tokenIndividual.getNombre().equals("FALSE")
                         || tokenIndividual.getTipo().equals("CADENA")) {
                    estadoTipoDeDatos = 'A';
                    estado = "10";
                } else {
                    estado = "E";
                }

                break;

            case 'B':

                if (tokenIndividual.getNombre().equals("+") || tokenIndividual.getNombre().equals("*")
                        || tokenIndividual.getNombre().equals("-") || tokenIndividual.getNombre().equals("/")
                        || tokenIndividual.getNombre().equals("OR") || tokenIndividual.getNombre().equals("<")
                        || tokenIndividual.getNombre().equals(">")) {
                    estadoTipoDeDatos = 'A';
                } else {
                    estado = "E";
                }
                break;
                
            case 'C':
                if (tokenIndividual.getTipo().equals("ENTERO") || tokenIndividual.getTipo().equals("DECIMAL")) {
                    estadoTipoDeDatos = 'D';
                } else {
                    estado = "E";
                }
                break;
            case 'D':
                if (tokenIndividual.getNombre().equals("+") || tokenIndividual.getNombre().equals("*")
                        || tokenIndividual.getNombre().equals("-") || tokenIndividual.getNombre().equals("/")
                        || tokenIndividual.getNombre().equals("OR") || tokenIndividual.getNombre().equals("<")
                        || tokenIndividual.getNombre().equals(">")) {
                    estadoTipoDeDatos = 'C';
                } else if (tokenIndividual.getNombre().equals(")")) {
                    
                    
                    
                    if (!(tokens.get(indice + 1).getNombre().equals("+") || tokens.get(indice + 1).getNombre().equals("*")
                            || tokens.get(indice + 1).getNombre().equals("-") || tokens.get(indice + 1).getNombre().equals("/")
                            || tokens.get(indice + 1).getNombre().equals("OR") || tokens.get(indice + 1).getNombre().equals("<")
                            || tokens.get(indice + 1).getNombre().equals(">"))) {
                        estado = "10";
                        estadoTipoDeDatos = 'A';
                    } else {
                        estadoTipoDeDatos = 'B';
                    }
                    
                    
                    
                    
                } else {
                    estado = "E";
                }
                break;
                
            case 'F':
                if (tokenIndividual.getNombre().equals("<") || tokenIndividual.getNombre().equals(">")) {
                    estadoTipoDeDatos = 'G';
                } else {
                    estado = "E";
                }
                break;
            case 'G':
                switch (tokenIndividual.getTipo()) {
                    case "FECHA":
                        
                        if (!(tokens.get(indice + 1).getNombre().equals("+") || tokens.get(indice + 1).getNombre().equals("*")
                                || tokens.get(indice + 1).getNombre().equals("-") || tokens.get(indice + 1).getNombre().equals("/")
                                || tokens.get(indice + 1).getNombre().equals("OR") || tokens.get(indice + 1).getNombre().equals("<")
                                || tokens.get(indice + 1).getNombre().equals(">"))) {
                            estado = "10";
                            estadoTipoDeDatos = 'A';
                        } else {
                            estadoTipoDeDatos = 'B';
                        }
                        break;
                    default:
                        estado = "E";
                }
                break;
            
        }

        if (estado.equals("E")) {
            System.out.println("");
            System.out.println("Token error en estructura de datos: " + tokenIndividual);
            System.out.println("");
        }
    }
}
