package AutomatasDML;

import LOGICA.Token;
import java.util.List;

public class AutomataSelect {

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
    private boolean estaEnPuntosCorrectosDeWhere = false;

    private List<List<Token>> todosLosComandos;
    int indiceGENERAL;

    public AutomataSelect(List<List<Token>> todosLosComandos, int indiceGENERAL) {
        //this.tokens = tokens;
        this.todosLosComandos = todosLosComandos;
        this.indiceGENERAL = indiceGENERAL;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {
            Token token = comandoIndividual.get(indiceToken);
//            System.out.println("EVALUANDO token \"" + token.getNombre() + "\" tipo: " + token.getTipo() + " estados: 1 " + estado + " seleccion " + estadoSeleccionColumna + " sentencia " + estadoSentencia + 
//            " agregacion " + estadoFuncionAgregacion);

            switch (estado) {
                case "1" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "SELECT" ->
                            "2";
                        default ->
                            "E";
                    };
                }
                case "2" -> {

                    if (!comprobarFuncionAgregacion && !comprobarSeleccionColumna) {
                        if (token.getNombre().equals("SUM") || token.getNombre().equals("AVG")
                                || token.getNombre().equals("COUNT") || token.getNombre().equals("MIN")
                                || token.getNombre().equals("MAX")) {
                            comprobarFuncionAgregacion = true;
                        } else if (token.getTipo().equals("IDENTIFICADOR")) {
                            comprobarSeleccionColumna = true;
                        } else if (token.getNombre().equals("*")) {
                            estadoInicialSentencia = "4";
                            estadoSentencia = "4";
                            estadoFinalSentencia = "5";
                            estado = "7";
                        } else {
                            estado = "E";
                        }
                    }

                    if (comprobarSeleccionColumna) {
                        comprobarEsSeleccionColumna(token);
                    } else if (comprobarFuncionAgregacion) {
                        comprobarEsFuncionAgregacion(token);
                    }

                }
                case "7" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case "FROM" ->
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

                //SENTENCIA
                case "4" -> {
                    if (token.getNombre().equals(";")) {
                        estado = "6";
                    } else {
                        sentencia(token, indiceToken);
                    }

                }
                case "5" -> {
                    switch (token.getNombre()) {
                        case ";" -> {
                            estado = "6";
                        }
                        case "LIMIT" -> {
                            estado = "39";
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
                    if (token.getNombre().equals(";") && !estado.equals("E") && estaEnPuntosCorrectosDeWhere ) {
                        estado = "6";
                    } else if (token.getNombre().equals(";") && todosLosComandos.get(indiceGENERAL).get(indiceToken - 2).getNombre().equals("FROM")) {
                        estado = "6";
                    } else if (token.getNombre().equals("LIMIT") && estaEnPuntosCorrectosDeWhere) {
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

    private void comprobarEsSeleccionColumna(Token tokenIndividual) {
//        System.out.println("Comprobando es seleccion \"" + tokenIndividual.getNombre() + "\" estado seleccion columna " + estadoTipoDeDatos);
        switch (estadoSeleccionColumna) {
            case "2":
                //Abajo entra a la letra y en base a eso cambia de estado

                if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                    estadoSeleccionColumna = "13";
                } else {
                    estado = "E";
                }
                break;

            case "13":

                if (tokenIndividual.getNombre().equals(".") || tokenIndividual.getNombre().equals(",")) {
                    estadoSeleccionColumna = "14";
                } else if (tokenIndividual.getNombre().equals("FROM")) {
                    estado = "10";
                    estadoSeleccionColumna = "2";
                    comprobarSeleccionColumna = false;
                } else if (tokenIndividual.getNombre().equals("AS")) {
                    estadoSeleccionColumna = "15";
                } else {
                    estado = "E";
                }
                break;

            case "14":
                if (tokenIndividual.getTipo().equals("IDENTIFICADOR") && !comprobarFuncionAgregacion) {
                    estadoSeleccionColumna = "13";
                } else {
                    comprobarFuncionAgregacion = true;
                    funcionAgregacion2 = true;
                    comprobarEsFuncionAgregacion(tokenIndividual);
                }
                break;
            case "15":
                if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                    estadoSeleccionColumna = "16";
                } else {
                    estado = "E";
                }
                break;
            case "16":
                if (tokenIndividual.getNombre().equals("FROM")) {
                    estado = "10";
                    estadoSeleccionColumna = "2";
                    comprobarSeleccionColumna = false;
                } else {
                    estado = "E";
                }
                break;

        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en seleccioncolumna: " + tokenIndividual);
//            System.out.println("");
        }

    }

    private void comprobarEsFuncionAgregacion(Token tokenIndividual) {
//         System.out.println("Comprobando es funcionAgregacion " + tokenIndividual.getNombre() + " estado tipo de dato " + estadoTipoDeDatos);
        switch (estadoFuncionAgregacion) {
            case "2":
                //Abajo entra a la letra y en base a eso cambia de estado

                if (tokenIndividual.getNombre().equals("SUM") || tokenIndividual.getNombre().equals("AVG")
                        || tokenIndividual.getNombre().equals("COUNT") || tokenIndividual.getNombre().equals("MIN")
                        || tokenIndividual.getNombre().equals("MAX")) {
                    estadoFuncionAgregacion = "15";
                } else {
                    estado = "E";
                }
                break;

            case "15":

                if (tokenIndividual.getNombre().equals("(")) {
                    estadoFuncionAgregacion = "16";
                } else {
                    estado = "E";
                }
                break;

            case "16":
                if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                    estadoFuncionAgregacion = "17";
                } else {
                    estado = "E";
                }
                break;
            case "17":

                if (tokenIndividual.getNombre().equals(")")) {
                    if (!funcionAgregacion2) {
                        estado = "9";
                    } else {
                        estadoSeleccionColumna = "13";
                        funcionAgregacion2 = false;
                    }
                    
                    estadoFuncionAgregacion = "2";
                    comprobarFuncionAgregacion = false;
                } else {
                    estado = "E";
                }
                break;
        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en funcion agregacion: " + tokenIndividual);
//            System.out.println("");
        }
    }

    private void sentencia(Token token, int indiceToken) {
//         System.out.println("Comprobando es secuencia " + token.getNombre());
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

        if (comprobarEsJoin) {
            comprobarEsJoin(token, indiceToken);
        } else if (comprobarEsWhere) {
            comprobarEsWhere(token, indiceToken);
        } else if (comprobarEsGroup) {
            comprobarEsGroup(token, indiceToken);
        } else if (comprobarEsOrder) {
            comprobarEsOrder(token, indiceToken);
        } else if (comprobarEsLimit) {
            comprobarEsLimit(token, indiceToken);
        }
    }

    private void comprobarEsJoin(Token tokenIndividual, int indiceToken) {
//        System.out.println("Comprobando es seleccion JOIN " + tokenIndividual.getNombre() + " estado sentencia " + estadoSentencia);

        if (estadoSentencia.equals(estadoInicialSentencia)) {
            if (tokenIndividual.getNombre().equals("JOIN")) {
                estadoSentencia = "18";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("18")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = "19";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("19")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = "20";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("20")) {
            if (tokenIndividual.getNombre().equals("ON")) {
                estadoSentencia = "21";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("21")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = "22";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("22")) {
            if (tokenIndividual.getNombre().equals(".")) {
                estadoSentencia = "21";
            } else if (tokenIndividual.getNombre().equals("=")) {
                estadoSentencia = "23";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("23")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                if (todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals(".")) {
                    estadoSentencia = "24";
                } else if (todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals(";")) {
                    estadoSentencia = estadoInicialSentencia;
                    estado = estadoFinalSentencia;
                    comprobarEsJoin = false;
                } else {
                    estado = "E";
                }

            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("24")) {
            if (tokenIndividual.getNombre().equals(".")) {
                estadoSentencia = "23";
            } else {
                estado = "E";
            }
        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en JOIN: " + tokenIndividual);
//            System.out.println("");
        }
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
                estaEnPuntosCorrectosDeWhere = false;
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

    private void comprobarEsGroup(Token tokenIndividual, int indiceToken) {
//        System.out.println("Comprobando es seleccion GROUP " + tokenIndividual.getNombre() + " estado sentencia " + estadoSentencia);

        if (estadoSentencia.equals(estadoInicialSentencia)) {
            if (tokenIndividual.getNombre().equals("GROUP")) {
                estadoSentencia = "28";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("28")) {
            if (tokenIndividual.getNombre().equals("BY")) {
                estadoSentencia = "29";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("29")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {

                if (todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals(".")) {
                    estadoSentencia = "38";
                } else if (todosLosComandos.get(indiceGENERAL).get(indiceToken + 1).getNombre().equals(";")) {
                    estadoSentencia = estadoInicialSentencia;
                    estado = estadoFinalSentencia;
                    comprobarEsGroup = false;
                } else {
                    estado = "E";
                }

            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("38")) {
            if (tokenIndividual.getNombre().equals(".")) {
                estadoSentencia = "30";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("30")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = estadoInicialSentencia;
                estado = estadoFinalSentencia;
                comprobarEsGroup = false;
            } else {
                estado = "E";
            }
        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en GROUP: " + tokenIndividual);
//            System.out.println("");
        }
    }

    private void comprobarEsOrder(Token tokenIndividual, int indiceToken) {
//        System.out.println("Comprobando es seleccion ORDER " + tokenIndividual.getNombre() + " estado sentencia " + estadoSentencia);

        if (estadoSentencia.equals(estadoInicialSentencia)) {
            if (tokenIndividual.getNombre().equals("ORDER")) {
                estadoSentencia = "29";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("29")) {
            if (tokenIndividual.getNombre().equals("BY")) {
                estadoSentencia = "30";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("30")) {
            if (tokenIndividual.getTipo().equals("IDENTIFICADOR")) {
                estadoSentencia = "31";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("31")) {
            if (tokenIndividual.getNombre().equals(",")) {
                estadoSentencia = "30";
            } else if (tokenIndividual.getNombre().equals("DESC") || tokenIndividual.getNombre().equals("ASC")) {
                estadoSentencia = estadoInicialSentencia;
                estado = estadoFinalSentencia;
                comprobarEsOrder = false;
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

    private void comprobarEsLimit(Token tokenIndividual, int indiceToken) {
//        System.out.println("Comprobando es seleccion LIMIT " + tokenIndividual.getNombre() + " estado sentencia " + estadoSentencia);

        if (estadoSentencia.equals(estadoInicialSentencia)) {
            if (tokenIndividual.getNombre().equals("LIMIT")) {
                estadoSentencia = "32";
            } else {
                estado = "E";
            }
        } else if (estadoSentencia.equals("32")) {
            if (tokenIndividual.getTipo().equals("ENTERO")) {
                estadoSentencia = estadoInicialSentencia;
                estado = estadoFinalSentencia;
                comprobarEsLimit = false;
            } else {
                estado = "E";
            }
        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en LIMIT: " + tokenIndividual);
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
                            estaEnPuntosCorrectosDeWhere = true;
                        } else {
                            estaEnPuntosCorrectosDeWhere = true;
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
                            estaEnPuntosCorrectosDeWhere = true;
                            estadoTipoDeDatos = 'A';
                        } else {
                            estadoSentencia = "33";
                            estaEnPuntosCorrectosDeWhere = true;
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
