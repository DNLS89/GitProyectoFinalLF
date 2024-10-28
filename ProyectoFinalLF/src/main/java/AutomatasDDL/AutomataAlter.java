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

    public AutomataAlter(List<List<String>> comandosAceptadosModificacion) {
        //this.tokens = tokens;

        this.comandosAceptadosModificacion = comandosAceptadosModificacion;
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
                        default ->
                            estado = "E";
                    }
                }

                case "2" -> {
                    switch (token.getNombre()) {
                        case "TABLE" -> {
                            estado = "3";
                            datosTabla.add(token.getNombre() + " ");
                        }
                        default ->
                            estado = "E";
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

                        default ->
                            estado = "E";
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
                        default ->
                            estado = "E";
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
                        default ->
                            estado = "E";
                    }
                }

                case "6" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "7";
                            comandoEnAceptacion.add(token.getNombre());
                        }
                        default ->
                            estado = "E";
                    }
                }
                case "7" -> {
                    //Abajo entra a la letra y en base a eso cambia de estado
                    estado = switch (token.getNombre()) {
                        case ";" ->
                            "8";
                        default ->
                            "E";
                    };
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
                        default ->
                            estado = "E";
                    }
                }

                case "10" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "11";
                            datosTabla.add(token.getNombre() + ": ");
                        }
                        default ->
                            estado = "E";
                    }
                }

                case "11" ->
                    comprobarEsTipoDato(token);

                case "12" -> {
                    estado = switch (token.getNombre()) {
                        case ";" ->
                            "8";
                        default ->
                            "E";
                    };
                }

                case "13" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "14";
                            datosTabla.add(token.getNombre() + ": ");
                        }
                        default ->
                            estado = "E";
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
                    estado = switch (token.getNombre()) {
                        case "(" ->
                            "29";
                        default ->
                            "E";
                    };
                }
                case "29" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "30";
                        default ->
                            "E";
                    };
                }
                case "30" -> {
                    estado = switch (token.getNombre()) {
                        case ")" ->
                            "12";
                        default ->
                            "E";
                    };
                }
                case "16" -> {
                    estado = switch (token.getNombre()) {
                        case "KEY" ->
                            "17";
                        default ->
                            "E";
                    };
                }
                case "17" -> {
                    estado = switch (token.getNombre()) {
                        case "(" ->
                            "18";
                        default ->
                            "E";
                    };
                }
                case "18" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "19";
                        default ->
                            "E";
                    };
                }
                case "19" -> {
                    estado = switch (token.getNombre()) {
                        case ")" ->
                            "20";
                        default ->
                            "E";
                    };
                }
                case "20" -> {
                    estado = switch (token.getNombre()) {
                        case "REFERENCES" ->
                            "21";
                        default ->
                            "E";
                    };
                }
                case "21" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "22";
                        default ->
                            "E";
                    };
                }
                case "22" -> {
                    estado = switch (token.getNombre()) {
                        case "(" ->
                            "23";
                        default ->
                            "E";
                    };
                }
                case "23" -> {
                    estado = switch (token.getTipo()) {
                        case "IDENTIFICADOR" ->
                            "24";
                        default ->
                            "E";
                    };
                }
                case "24" -> {
                    estado = switch (token.getNombre()) {
                        case ")" ->
                            "31";
                        default ->
                            "E";
                    };
                }
                case "31" -> {
                    estado = switch (token.getNombre()) {
                        case ";" ->
                            "32";
                        case "ON" ->
                            "33";
                        default ->
                            "E";
                    };
                }
                case "33" -> {
                    estado = switch (token.getNombre()) {
                        case "DELETE" ->
                            "34";
                        default ->
                            "E";
                    };
                }
                case "34" -> {
                    estado = switch (token.getNombre()) {
                        case "SET" ->
                            "35";
                        default ->
                            "E";
                    };
                }
                case "35" -> {
                    estado = switch (token.getNombre()) {
                        case "NULL" ->
                            "36";
                        default ->
                            "E";
                    };
                }
                case "36" -> {
                    estado = switch (token.getNombre()) {
                        case "ON" ->
                            "37";
                        default ->
                            "E";
                    };
                }
                case "37" -> {
                    estado = switch (token.getNombre()) {
                        case "UPDATE" ->
                            "38";
                        default ->
                            "E";
                    };
                }
                case "38" -> {
                    estado = switch (token.getNombre()) {
                        case "CASCADE" ->
                            "12";
                        default ->
                            "E";
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
                        default ->
                            estado = "E";
                    }
                }
                case "26" -> {
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR" -> {
                            estado = "27";
                            datosTabla.add(token.getNombre() + ": ");
                        }
                        default ->
                            estado = "E";
                    }
                }
                case "27" -> {
                    estado = switch (token.getNombre()) {
                        case "TYPE" ->
                            "28";
                        default ->
                            "E";
                    };
                }

                case "28" ->
                    comprobarEsTipoDato(token);

                case "E" -> {
                    // System.out.println("Token en el que detectó error ALTER: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    return false;
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
