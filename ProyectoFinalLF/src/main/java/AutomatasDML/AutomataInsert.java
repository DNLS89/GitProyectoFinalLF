package AutomatasDML;

import LOGICA.Token;
import java.util.List;

public class AutomataInsert {

    //private List<Token> tokens;
    private String estado = "1";
    private char estadoTipoDeDatos = 'A';
    private List<List<Token>> todosLosComandos;
    int indiceGENERAL;
    private List<Token> erroresSintacticos;

    public AutomataInsert(List<List<Token>> todosLosComandos, int indiceGENERAL, List<Token> erroresSintacticos) {
        //this.tokens = tokens;
        this.todosLosComandos = todosLosComandos;
        this.indiceGENERAL = indiceGENERAL;
        this.erroresSintacticos = erroresSintacticos;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {
            Token token = comandoIndividual.get(indiceToken);
            //System.out.println("Evaluando token " + token.getNombre() + " tipo: " + token.getTipo() + " estado " + estado);

            switch (estado) {
                case "1" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "INSERT" -> {
                            estado = "2";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"INSERT\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "2" -> {
                    switch (token.getNombre()) {
                        case "INTO" -> {
                            estado = "3";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"INTO\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "3" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "4";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "4" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "(" -> {
                            estado = "5";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"(\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "5" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "6";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "6" -> {
                    switch (token.getNombre()) {
                        case ")" -> {
                            estado = "7";
                        }
                        case "," -> {
                            estado = "5";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \") | ,\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "7" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "VALUES" -> {
                            estado = "8";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"VALUES\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "8" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "(" -> {
                            estado = "9";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"(\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "9" ->
                    comprobarEsEstructuraDato(token, indiceToken);

                case "10" -> {
                    switch (token.getNombre()) {
                        case "," -> {
                            estado = "9";
                        }
                        case ")" -> {
                            estado = "11";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \", | )\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "11" -> {
                    switch (token.getNombre()) {
                        case ";" -> {
                            estado = "12";
                        }
                        case "," -> {
                            estado = "8";
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"; | ,\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "E" -> {
                    //System.out.println("Token en el que detectó error DML INSERT: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    //return false;
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
        // System.out.println("Comprobando estructura de dato " + tokenIndividual.getNombre() + " estado tipo de dato " + estadoTipoDeDatos);
        switch (estadoTipoDeDatos) {
            case 'A':
                //Abajo entra a la letra y en base a eso cambia de estado

                if (tokenIndividual.getTipo().equals("ENTERO") || tokenIndividual.getTipo().equals("DECIMAL")) {

                    if (!(todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("+") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("*")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("-") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("/")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("OR") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("<")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(">"))) {
                        estado = "10";
                        estadoTipoDeDatos = 'A';
                    } else {
                        estadoTipoDeDatos = 'B';
                    }

                } else if (tokenIndividual.getNombre().equals("(")) {
                    estadoTipoDeDatos = 'C';
                } else if (tokenIndividual.getTipo().equals("FECHA")) {

                    if (!(todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("<") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(">"))) {
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

                    tokenIndividual.setDescripcionTokenError("Se esperaba una Estruc. de Dato");
                    erroresSintacticos.add(tokenIndividual);
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
                    tokenIndividual.setDescripcionTokenError("Se esperaba una Estruc. de Dato");
                    erroresSintacticos.add(tokenIndividual);
                    estado = "E";
                }
                break;

            case 'C':
                if (tokenIndividual.getTipo().equals("ENTERO") || tokenIndividual.getTipo().equals("DECIMAL")) {
                    estadoTipoDeDatos = 'D';
                } else {
                    tokenIndividual.setDescripcionTokenError("Se esperaba un ENTERO o DECIMAL");
                    erroresSintacticos.add(tokenIndividual);
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

                    if (!(todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("+") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("*")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("-") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("/")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("OR") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("<")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(">"))) {
                        estado = "10";
                        estadoTipoDeDatos = 'A';
                    } else {
                        estadoTipoDeDatos = 'B';
                    }

                } else {
                    tokenIndividual.setDescripcionTokenError("Se esperaba una Estruc. de Dato");
                    erroresSintacticos.add(tokenIndividual);
                    estado = "E";
                }
                break;

            case 'F':
                if (tokenIndividual.getNombre().equals("<") || tokenIndividual.getNombre().equals(">")) {
                    estadoTipoDeDatos = 'G';
                } else {
                    tokenIndividual.setDescripcionTokenError("Se esperaba < o >");
                    erroresSintacticos.add(tokenIndividual);
                    estado = "E";
                }
                break;
            case 'G':
                switch (tokenIndividual.getTipo()) {
                    case "FECHA":

                        if (!(todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("+") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("*")
                                || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("-") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("/")
                                || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("OR") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("<")
                                || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(">"))) {
                            estado = "10";
                            estadoTipoDeDatos = 'A';
                        } else {
                            estadoTipoDeDatos = 'B';
                        }
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba una Estruc. de Dato");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;

        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en estructura de datos: " + tokenIndividual);
//            System.out.println("");
        }
    }
}
