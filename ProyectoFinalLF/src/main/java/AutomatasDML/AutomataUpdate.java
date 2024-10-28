package AutomatasDML;

import LOGICA.Token;
import java.util.List;

public class AutomataUpdate {
    
    
    //private List<Token> tokens;
    private String estado = "1";
    private String estadoSeleccionColumna = "2";
    private String estadoFuncionAgregacion = "2";

    //Lo de abajo cambian si se sigue la rama superior (ver estados)
    private String estadoInicialSentencia = "11";
    private String estadoFinalSentencia = "12";
    private String estadoSentencia = "11";

    private char estadoTipoDeDatos = 'A';

    private boolean comprobarSeleccionColumna = false;
    private boolean comprobarFuncionAgregacion = false;
    private boolean comprobarEsJoin = false;
    private boolean comprobarEsWhere = false;
    private boolean comprobarEsGroup = false;
    private boolean comprobarEsOrder = false;
    private boolean comprobarEsLimit = false;
    
    
    
    private boolean funcionAgregacion2 = false;

    private boolean terminaEnRamaSuperior = false;

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
//            System.out.println("EVALUANDO token \"" + token.getNombre() + "\" tipo: " + token.getTipo() + " estados: 1 " + estado + " seleccion " + estadoSeleccionColumna + " sentencia " + estadoSentencia + 
            //" agregacion " + estadoFuncionAgregacion);

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

//                    if (comprobarSeleccionColumna) {
//                        comprobarEsSeleccionColumna(token);
//                    } else if (comprobarFuncionAgregacion) {
//                        comprobarEsFuncionAgregacion(token);
//                    }

                }
                case "3" -> {
                    estado = switch (token.getNombre()) {
                        case "SET" ->
                            "4";
                        default ->
                            "E";
                    };
                }

                //SENTENCIA
                case "4" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "5";
                        default ->
                            "E";
                    };

                }
                case "5" -> {
                    switch (token.getNombre()) {
                        case "=" -> {
                            estado = "6";
                        }
                        default ->
                            estado = "E";
                    }
                }
                case "9" -> {
                    estado = switch (token.getNombre()) {
                        case "FROM" ->
                            "10";
                        default ->
                            "E";
                    };
                }
                case "10" -> {
//                    estado = switch (token.getTipo()) {
//                        case "IDENTIFICADOR" ->
//                            "11";
//                        default ->
//                            "E";
//                    };

                    if (token.getTipo().equals("IDENTIFICADOR")) {

                        if (todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getTipo().equals("IDENTIFICADOR")) {
                            estado = "10";
                        } else {
                            estado = "11";
                        }

                    } else {
                        estado = "E";
                    }
                }
                case "11" -> {
                    if (token.getNombre().equals(";")) {
                        estado = "6";
                    } else if (token.getNombre().equals("LIMIT")) {
                        estadoSentencia = "39";
                        sentencia(token, indiceToken);
                    }
                        else {
                        sentencia(token, indiceToken);
                    }
                }
                case "12" -> {
                    if (token.getNombre().equals(";")) {
                        estado = "6";
                    } else if (token.getNombre().equals("LIMIT")) {
                        estado = "39";
                    } else {
                        estado = "E";
                        //sentencia(token, indiceToken);
                    }
                }
                case "39" -> {
                    if (token.getTipo().equals("ENTERO")) {
                        estado = "6";
                    } else {
                        sentencia(token, indiceToken);
                    }
                }
                case "E" -> {
//                    System.out.println("Token en el que detectó error DML SELECT: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    return false;
                    //break;
                }
                //break;

            }
        }

        if (estado.equals("6")) {
            System.out.println("Cumpre con formato SELECT");
            return true;
        } else {
            System.out.println("No cumple con formato SELECT");
        }

        return false;
    }

    private void sentencia(Token token, int indiceToken) {
        // System.out.println("Comprobando es secuencia " + token.getNombre());
        if (!(comprobarEsJoin || comprobarEsWhere || comprobarEsGroup || comprobarEsOrder || comprobarEsLimit)) {
            if (token.getNombre().equals("JOIN")) {
                comprobarEsJoin = true;
            } else if (token.getNombre().equals("WHERE")) {
                comprobarEsWhere = true;
            } else if (token.getNombre().equals("GROUP")) {
                comprobarEsGroup = true;
            } else if (token.getNombre().equals("ORDER")) {
                comprobarEsOrder = true;
            } else if (token.getNombre().equals("LIMIT")) {
                comprobarEsLimit = true;
            } else {
                estado = "E";
            }
        }

//        if (comprobarEsJoin) {
//            comprobarEsJoin(token, indiceToken);
//        } else if (comprobarEsWhere) {
//            comprobarEsWhere(token, indiceToken);
//        } else if (comprobarEsGroup) {
//            comprobarEsGroup(token, indiceToken);
//        } else if (comprobarEsOrder) {
//            comprobarEsOrder(token, indiceToken);
//        } else if (comprobarEsLimit) {
//            comprobarEsLimit(token, indiceToken);
//        }
    }

    private void comprobarEsWhere(Token tokenIndividual, int indiceToken) {
//        System.out.println("PREUBA WHERE");

        if (estadoSentencia.equals(estadoInicialSentencia)) {
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
                estadoSentencia = estadoInicialSentencia;
                estado = estadoFinalSentencia;
                comprobarEsWhere = false;
            } else {
                estado = "E";
            }

        } else if (estadoSentencia.equals("33")) {
//            System.out.println("PRUEBA ESTADO 33 token:" + tokenIndividual.getNombre());
            if (tokenIndividual.getNombre().equals("AND")) {
                estadoSentencia = "34";
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

//            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
//                estadoSentencia = "37";
//            } else {
//                estado = "E";
//            }

//            if (tokenIndividual.getTipo().equals("IDENTIFICADOR") || tokenIndividual.getTipo().equals("CADENA")) {
//
//                if (tokenIndividual.getTipo().equals("IDENTIFICADOR") && todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals(".")) {
//                    estadoSentencia = "37";
//                } else if (todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals(";")) {
//                    estadoSentencia = estadoInicialSentencia;
//                    estado = estadoFinalSentencia;
//                    comprobarEsWhere = false;
//                } else {
//                    estado = "E";
//                }
//
//            } else {
//                estado = "E";
//            }
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
//        System.out.println("Comprobando estructura de dato " + tokenIndividual.getNombre() + " estado tipo de dato " + estadoTipoDeDatos);
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

                        if (terminaEnRamaSuperior) {
                            estadoSentencia = "37";
                            estadoTipoDeDatos = 'A';
                        } else {
                            estadoSentencia = "33";
                            estadoTipoDeDatos = 'A';
                            terminaEnRamaSuperior = true;
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

                        if (terminaEnRamaSuperior) {
                            estadoSentencia = "37";
                            estadoTipoDeDatos = 'A';
                        } else {
                            estadoSentencia = "33";
                            estadoTipoDeDatos = 'A';
                            terminaEnRamaSuperior = true;
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
