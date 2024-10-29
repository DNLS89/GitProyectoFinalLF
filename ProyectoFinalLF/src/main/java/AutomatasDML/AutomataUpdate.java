package AutomatasDML;

import LOGICA.Token;
import java.util.List;

public class AutomataUpdate {

    //private List<Token> tokens;
    private String estado = "1";
    private final String estadoSeleccionColumna = "2";
    private final String estadoFuncionAgregacion = "2";

    private String estadoSentencia = "7";

    private char estadoTipoDeDatos = 'A';

    private boolean terminaEnRamaSuperior = false;

    private boolean esPrevioAWhere = true;
    private boolean dentroDeWhere = false;
    private boolean estaEnPuntosCorrectosDeWhere = false;

    private List<List<Token>> todosLosComandos;
    int indiceGENERAL;

    public AutomataUpdate(List<List<Token>> todosLosComandos, int indiceGENERAL) {
        //this.tokens = tokens;
        this.todosLosComandos = todosLosComandos;
        this.indiceGENERAL = indiceGENERAL;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {
            Token token = comandoIndividual.get(indiceToken);
//            System.out.println("EVALUANDO token \"" + token.getNombre() + "\" tipo: " + token.getTipo() + " estados: 1 " + estado + " where " + estadoSentencia);

            switch (estado) {
                case "1" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "UPDATE" ->
                            "2";
                        default ->
                            "E";
                    };
                }
                case "2" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "3";
                        default ->
                            "E";
                    };
                }

                case "3" -> {

                    if (token.getNombre().equals("SET")) {
                        estado = "4";

                    } else {
                        comprobarEsWhere(token, indiceToken);
                    }
                }
                case "4" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "5";
                        default ->
                            "E";
                    };
                }
                case "5" -> {

                    if (token.getNombre().equals("=")) {
                        estado = "6";

                    } else {
                        comprobarEsWhere(token, indiceToken);
                    }
                }
                case "6" -> {
                    esPrevioAWhere = true;
                    comprobarEsEstructuraDato(token, indiceToken);
                }

                case "7" -> {

//                    if (token.getNombre().equals(";")) {
//                        estado = "9";
//
//                    } else if (token.getNombre().equals(",")) {
//                        estado = "4";
//                    } else {
//                        comprobarEsWhere(token, indiceToken);
//                    }

                    if (token.getNombre().equals(";") && (!dentroDeWhere || estaEnPuntosCorrectosDeWhere)) {

                        estado = "9";

                    } else if (token.getNombre().equals(",")) {
                        estado = "4";
                    } else {
                        dentroDeWhere = true;
                        comprobarEsWhere(token, indiceToken);
                    }

                }
                case "E" -> {
//                    System.out.println("Token en el que detectó error DML SELECT: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    return false;
                }

            }
        }

        if (estado.equals("9")) {
            System.out.println("Cumpre con formato UPDATE");
            return true;
        } else {
            System.out.println("No cumple con formato UPDATE");
        }

        return false;
    }

    private void comprobarEsWhere(Token tokenIndividual, int indiceToken) {
//        System.out.println("PREUBA WHERE");

        if (estadoSentencia.equals("7")) {
            if (tokenIndividual.getNombre().equals("WHERE")) {
                estadoSentencia = "24";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("24")) {

            if ((tokenIndividual.getTipo().equals("IDENTIFICADOR") && todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals("=")) /*&& !ramaSuperior*/) {
                estadoSentencia = "25";
                terminaEnRamaSuperior = true;
            } else {
                comprobarEsEstructuraDato(tokenIndividual, indiceToken);
            }

        } else if (estadoSentencia.equals("25")) {
            if (tokenIndividual.getNombre().equals("=")) {
                estadoSentencia = "26";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("26")) {
            comprobarEsEstructuraDato(tokenIndividual, indiceToken);
        } else if (estadoSentencia.equals("27")) {

            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = "4";
                //estado = estadoFinalSentencia;
                dentroDeWhere = false;
            } else {
                estado = "E";
            }

        } else if (estadoSentencia.equals("33")) {
//            System.out.println("PRUEBA ESTADO 33 token:" + tokenIndividual.getNombre());
            if (tokenIndividual.getNombre().equals("AND")) {
                estadoSentencia = "34";
                estaEnPuntosCorrectosDeWhere = false;
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("34")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = "35";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("35")) {
            if (tokenIndividual.getNombre().equals("=")) {
                estadoSentencia = "36";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("36")) {

            comprobarEsEstructuraDato(tokenIndividual, indiceToken);

        } else if (estadoSentencia.equals("37")) {
            if (tokenIndividual.getNombre().equals(".")) {
                estadoSentencia = "27";
            } else {
                estado = "E";
            }
        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en ORDER: " + tokenIndividual);
//            System.out.println("");
        }

    }

    private void comprobarEsEstructuraDato(Token tokenIndividual, int indice) {
//        System.out.println("Comprobando estructura de dato " + tokenIndividual.getNombre() + " estado " + estadoTipoDeDatos);
        switch (estadoTipoDeDatos) {
            case 'A':
                //Abajo entra a la letra y en base a eso cambia de estado
                if (tokenIndividual.getTipo().equals("ENTERO") || tokenIndividual.getTipo().equals("DECIMAL")
                        || tokenIndividual.getTipo().equals("FECHA") || tokenIndividual.getNombre().equals("TRUE")
                        || tokenIndividual.getNombre().equals("FALSE") || tokenIndividual.getTipo().equals("CADENA")
                        || tokenIndividual.getTipo().equals("IDENTIFICADOR")) {

                    if (!(todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("+") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("*")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("-") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("/")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("OR") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("<")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(">"))) {

                        //LO de abajo es para cuando sea DATO previo a WHERE
                        if (!esPrevioAWhere) {

                            if (terminaEnRamaSuperior && !todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(".")) {
                                dentroDeWhere = false;
                            }

                            if (terminaEnRamaSuperior) {
                                estadoSentencia = "37";
                                estadoTipoDeDatos = 'A';
                            } else {
                                estaEnPuntosCorrectosDeWhere = true;
                                estadoSentencia = "33";
                                estadoTipoDeDatos = 'A';
                                terminaEnRamaSuperior = true;
                            }

                        } else {
                            estado = "7";
                            esPrevioAWhere = false;
                            estadoTipoDeDatos = 'A';
                        }

                    } else {
                        estadoTipoDeDatos = 'B';
                    }

                } else if (tokenIndividual.getNombre().equals("(")) {
                    estadoTipoDeDatos = 'C';
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
                if (tokenIndividual.getTipo().equals("ENTERO") || tokenIndividual.getTipo().equals("DECIMAL")
                        || tokenIndividual.getTipo().equals("FECHA") || tokenIndividual.getNombre().equals("TRUE")
                        || tokenIndividual.getNombre().equals("FALSE") || tokenIndividual.getTipo().equals("CADENA")
                        || tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
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

                    if (!(todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("+") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("*")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("-") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("/")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("OR") || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals("<")
                            || todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(">"))) {

                        if (!esPrevioAWhere) {
                            if (terminaEnRamaSuperior && !todosLosComandos.get(indiceGENERAL).get(indice + 1).getNombre().equals(".")) {
                                dentroDeWhere = false;
                            }

                            if (terminaEnRamaSuperior) {
                                estadoSentencia = "37";
                                estadoTipoDeDatos = 'A';
                            } else {
                                estadoSentencia = "33";
                                estaEnPuntosCorrectosDeWhere = true;
                                estadoTipoDeDatos = 'A';
                                terminaEnRamaSuperior = true;
                            }

                        } else {
                            estado = "7";
                            esPrevioAWhere = false;
                            estadoTipoDeDatos = 'A';
                        }

//                        estado = "10";
//                        estadoTipoDeDatos = 'A';
                    } else {
                        estadoTipoDeDatos = 'B';
                    }

                } else {
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
