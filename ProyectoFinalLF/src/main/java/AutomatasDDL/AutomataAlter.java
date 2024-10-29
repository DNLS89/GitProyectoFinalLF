package AutomatasDDL;

import LOGICA.Token;
import java.util.ArrayList;
import java.util.List;

public class AutomataAlter {

    //private List<Token> tokens;
    private String estado = "1";
    private char estadoTipoDeDatos = 'A';

    private List<List<String>> comandosAceptadosModificacion;
    private List<String> comandoEnAceptacion = new ArrayList<>();
    private List<String> datosTabla;
    private List<Token> erroresSintacticos;

    public AutomataAlter(List<List<String>> comandosAceptadosModificacion, List<Token> erroresSintacticos) {
        //this.tokens = tokens;

        this.comandosAceptadosModificacion = comandosAceptadosModificacion;
        this.erroresSintacticos = erroresSintacticos;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        datosTabla = new ArrayList<>();
        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {
            Token token = comandoIndividual.get(indiceToken);
            //System.out.println("Evaluando token " + token.getNombre() + " tipo: " + token.getTipo() + " estado " + estado);

            switch (estado) {
                case "1" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "ALTER" -> {
                            estado = "2";
                            datosTabla.add(token.getNombre() + " ");
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"ALTER\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }

                case "2" -> {
                    switch (token.getNombre()) {
                        case "TABLE" -> {
                            estado = "3";
                            datosTabla.add(token.getNombre() + " ");
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"TABLE\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }

                case "3" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            datosTabla.add(token.getNombre() + " ");

                            String stringDatosTabla = "";
                            for (String palabra : datosTabla) {
                                stringDatosTabla += palabra;

                            }
                            comandoEnAceptacion.add(stringDatosTabla);
                            datosTabla = new ArrayList<>();

                            estado = "4";
                        }

                        default -> {
                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                    break;
                }
                case "4" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "DROP" -> {
                            estado = "5";
                            datosTabla.add(token.getNombre() + " ");
                        }
                        case "ADD" -> {
                            estado = "9";
                            datosTabla.add(token.getNombre() + " ");
                        }
                        case "ALTER" -> {
                            estado = "25";
                            datosTabla.add(token.getNombre() + " ");
                        }
                        default -> {
                            token.setDescripcionTokenError("Se esperaba \"DROP | ADD | ALTER\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                    break;
                }

                case "5" -> {
                    switch (token.getNombre()) {
                        case "COLUMN" -> {
                            estado = "6";
                            datosTabla.add(token.getNombre() + " ");
                            String stringDatosTabla = "";
                            for (String palabra : datosTabla) {
                                stringDatosTabla += palabra;

                            }
                            comandoEnAceptacion.add(stringDatosTabla);
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"COLUMN\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }

                case "6" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "7";
                            comandoEnAceptacion.add(token.getNombre());
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "7" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case ";" -> {
                            estado = "8";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \",\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }

                case "9" -> {
                    switch (token.getNombre()) {
                        case "COLUMN" -> {
                            estado = "10";
                            datosTabla.add(token.getNombre() + " ");
                            String stringDatosTabla = "";
                            for (String palabra : datosTabla) {
                                stringDatosTabla += palabra;

                            }
                            comandoEnAceptacion.add(stringDatosTabla);
                            datosTabla = new ArrayList<>();
                        }
                        case "CONSTRAINT" -> {
                            estado = "13";
                            datosTabla.add(token.getNombre() + " ");
                            String stringDatosTabla = "";
                            for (String palabra : datosTabla) {
                                stringDatosTabla += palabra;

                            }
                            comandoEnAceptacion.add(stringDatosTabla);
                            datosTabla = new ArrayList<>();
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"COLUMN | CONSTRAINT\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }

                case "10" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "11";
                            datosTabla.add(token.getNombre() + ": ");
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }

                case "11" ->
                    comprobarEsTipoDato(token);

                case "12" -> {
                    switch (token.getNombre()) {
                        case ";" -> {
                            estado = "8";
                        }
                        default -> {
                            estado = "E";
                        }
                    }
                }

                case "13" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "14";
                            datosTabla.add(token.getNombre() + ": ");
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "14" -> {

                    if (token.getTipo().equals("TIPO DE DATO")) {
                        estado = "12";
                    } else if (token.getNombre().equals("UNIQUE")) {
                        estado = "15";
                    } else if (token.getNombre().equals("FOREIGN")) {
                        estado = "16";
                    }
                }
                case "15" -> {
                    switch (token.getNombre()) {
                        case "(" -> {
                            estado = "29";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \")\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "29" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "30";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "30" -> {
                    switch (token.getNombre()) {
                        case ")" -> {
                            estado = "12";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \")\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "16" -> {
                    switch (token.getNombre()) {
                        case "KEY" -> {
                            estado = "17";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"KEY\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "17" -> {
                    switch (token.getNombre()) {
                        case "(" -> {
                            estado = "18";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \",\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "18" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "19";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "19" -> {
                    switch (token.getNombre()) {
                        case ")" -> {
                            estado = "20";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \")\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "20" -> {
                    switch (token.getNombre()) {
                        case "REFERENCES" -> {
                            estado = "21";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"REFERENCES\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "21" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "22";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "22" -> {
                    switch (token.getNombre()) {
                        case "(" -> {
                            estado = "23";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"(\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "23" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "24";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "24" -> {
                    switch (token.getNombre()) {
                        case ")" -> {
                            estado = "31";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \")\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "31" -> {
                    switch (token.getNombre()) {
                        case ";" -> {
                            estado = "32";
                        }
                        case "ON" -> {
                            estado = "33";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"; | ON\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "33" -> {
                    switch (token.getNombre()) {
                        case "DELETE" -> {
                            estado = "34";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"DELETE\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "34" -> {
                    switch (token.getNombre()) {
                        case "SET" -> {
                            estado = "35";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"SET\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "35" -> {
                    switch (token.getNombre()) {
                        case "NULL" -> {
                            estado = "36";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"NULL\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "36" -> {
                    switch (token.getNombre()) {
                        case "ON" -> {
                            estado = "37";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"ON\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "37" -> {
                    switch (token.getNombre()) {
                        case "UPDATE" -> {
                            estado = "38";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"UPDATE\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "38" -> {
                    switch (token.getNombre()) {
                        case "CASCADE" -> {
                            estado = "12";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"CASCADE\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }
                case "25" -> {
                    switch (token.getNombre()) {
                        case "COLUMN" -> {
                            estado = "26";
                            datosTabla.add(token.getNombre() + " ");
                            String stringDatosTabla = "";
                            for (String palabra : datosTabla) {
                                stringDatosTabla += palabra;

                            }
                            comandoEnAceptacion.add(stringDatosTabla);
                            datosTabla = new ArrayList<>();
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"COLUMN\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "26" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "27";
                            datosTabla.add(token.getNombre() + ": ");
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    }
                }
                case "27" -> {
                    switch (token.getNombre()) {
                        case "TYPE" -> {
                            estado = "28";
                        }
                        default -> {

                            token.setDescripcionTokenError("Se esperaba \"Type\"");
                            erroresSintacticos.add(token);
                            estado = "E";
                        }
                    };
                }

                case "28" ->
                    comprobarEsTipoDato(token);

                case "E" -> {
                    // System.out.println("Token en el que detectó error ALTER: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    //return false;
                    //break;
                }
                //break;

            }
        }

        if (estado.equals("8") || estado.equals("32")) {
            System.out.println("Cumpre con formato Alter");
            comandosAceptadosModificacion.add(comandoEnAceptacion);
            return true;
        } else {
            System.out.println("No cumple con formato Alter");
        }

        return false;
    }

    private void comprobarEsTipoDato(Token tokenIndividual) {
        //System.out.println("Comprobando tipo de dato " + tokenIndividual.getNombre() + " estado tipo de dato " + estadoTipoDeDatos);
        switch (estadoTipoDeDatos) {
            case 'A':
                //Abajo entra a la letra y en base a eso cambia de estado
                if (tokenIndividual.getNombre().equals("SERIAL") || tokenIndividual.getNombre().equals("INTEGER")
                        || tokenIndividual.getNombre().equals("BIGINT") || tokenIndividual.getNombre().equals("DATE")
                        || tokenIndividual.getNombre().equals("TEXT") || tokenIndividual.getNombre().equals("BOOLEAN")) {
                    estado = "12";
                    estadoTipoDeDatos = 'A';
                    datosTabla.add(tokenIndividual.getNombre() + " ");

                } else if (tokenIndividual.getNombre().equals("VARCHAR")) {
                    estadoTipoDeDatos = 'B';
                    datosTabla.add(tokenIndividual.getNombre() + " ");
                    //System.out.println("ESTADO B");
                } else if (tokenIndividual.getNombre().equals("DECIMAL") || tokenIndividual.getNombre().equals("NUMERIC")) {
                    estadoTipoDeDatos = 'G';
                    datosTabla.add(tokenIndividual.getNombre() + " ");
                } else {
                    tokenIndividual.setDescripcionTokenError("Se esperaba un Tipo de Dato");
                    erroresSintacticos.add(tokenIndividual);
                    estado = "E";
                }

                break;

            case 'B':
                switch (tokenIndividual.getNombre()) {
                    case "(":
                        estadoTipoDeDatos = 'C';
                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un \"(\"");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'C':
                switch (tokenIndividual.getTipo()) {
                    case "ENTERO":
                        estadoTipoDeDatos = 'D';
                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un ENTERO");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'D':
                switch (tokenIndividual.getNombre()) {
                    case ")":
                        estadoTipoDeDatos = 'A';

                        estado = "12";

                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        String stringDatosTabla = "";
                        for (String palabra : datosTabla) {
                            stringDatosTabla += palabra;

                        }
                        comandoEnAceptacion.add(stringDatosTabla);

                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un \")\"");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'G':
                switch (tokenIndividual.getNombre()) {
                    case "(":
                        estadoTipoDeDatos = 'H';
                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un \"(\"");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'H':
                switch (tokenIndividual.getTipo()) {
                    case "ENTERO":
                        estadoTipoDeDatos = 'I';
                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un ENTERO");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'I':
                switch (tokenIndividual.getNombre()) {
                    case ",":
                        estadoTipoDeDatos = 'J';
                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un \",\"");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'J':
                switch (tokenIndividual.getTipo()) {
                    case "ENTERO":
                        estadoTipoDeDatos = 'K';
                        datosTabla.add(tokenIndividual.getNombre() + " ");
                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un ENTERO");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
            case 'K':
                switch (tokenIndividual.getNombre()) {
                    case ")":
                        estadoTipoDeDatos = 'A';

                        estado = "12";

                        datosTabla.add(tokenIndividual.getNombre() + " ");

                        String stringDatosTabla = "";
                        for (String palabra : datosTabla) {
                            stringDatosTabla += palabra;

                        }
                        comandoEnAceptacion.add(stringDatosTabla);

                        break;
                    default:
                        tokenIndividual.setDescripcionTokenError("Se esperaba un \")\"");
                        erroresSintacticos.add(tokenIndividual);
                        estado = "E";
                }
                break;
        }

        if (estado.equals("E")) {
//            System.out.println("");
//            System.out.println("Token error en tipo de datos: " + tokenIndividual);
//            System.out.println("");
        }
    }
}
