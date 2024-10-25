package AutomatasDDL;

import LOGICA.Token;
import java.util.List;

public class AutomataCreate {

    //"A" es el estado 
    private char estado = 'A';
    //'1'es el estado Inicial de la estructura de declaracion
    private char estadoDeclaracion = '1';
    //"A" es el estado  del Tipo de Datos
    private char estadoTipoDeDatos = 'A';
    //'A'es el estado iniical de Estructura de Llaves
    private char estadoEstructuraLlave = 'A';

    private List<Token> tokens;

    private boolean comprobarEsEstructuraDeclaracion = false;
    private boolean comprobarEsEstructuraLlaves = false;

    public AutomataCreate(List<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {

            Token token = comandoIndividual.get(indiceToken);
            System.out.println("");
            System.out.println("ESTADOS: normal " + estado + " declaracion " + estadoDeclaracion
                    + " datos " + estadoTipoDeDatos + " estructu " + estadoEstructuraLlave);
            System.out.println("Evaluando token automata " + token.getNombre());

            switch (estado) {
                case 'A':
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "CREATE":
                            estado = 'B';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;

                case 'B':
                    switch (token.getNombre()) {
                        case "DATABASE":
                            estado = 'B';
                            break;
                        case "TABLE":
                            estado = 'F';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;

                case 'C':
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR":
                            estado = 'L';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;
                case 'L':
                    switch (token.getNombre()) {
                        case ";":
                            estado = 'D';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;
                case 'F':
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR":
                            estado = 'G';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;
                case 'G':
                    switch (token.getNombre()) {
                        case "(":
                            estado = 'H';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;

                //Abajo debe comprobar que tenga una estructura de declaración
                case 'H':
                    comprobarEsEstructuraDeclaracion(token, indiceToken);

                    break;

                case 'I':
                    switch (token.getNombre()) {
                        case ",":
                            estado = 'J';
                            break;
                        case ")":
                            estado = 'L';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;
                case 'J':
                    
                    if (token.getTipo().equals("IDENTIFICADOR") && !comprobarEsEstructuraLlaves) {
                        comprobarEsEstructuraDeclaracion(token, indiceToken);
                        comprobarEsEstructuraDeclaracion = true;
                        break;
                    } else if (token.getNombre().equals("CONSTRAINT")) {
                        //comprobarEsEstructuraLlaves(token);
                        comprobarEsEstructuraLlaves = true;
                    }

                    if (comprobarEsEstructuraDeclaracion) {
                        comprobarEsEstructuraDeclaracion(token, indiceToken);
                    } else if (comprobarEsEstructuraLlaves) {
                        comprobarEsEstructuraLlaves(token);
                    } else if (token.getNombre().equals(")")) {
                        estado = 'L';
                    } else {
                        estado = 'E';
                    }

                    break;
                case 'K':
                    switch (token.getNombre()) {
                        case ")":
                            estado = 'L';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;
                case 'E':
                    System.out.println("Token en el que detectó error: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    return false;
                //break;

            }
        }

        if (estado == 'D') {
            return true;
        }

        return false;
    }

    private void comprobarEsEstructuraDeclaracion(Token tokenIndividual, int indiceToken) {
        System.out.println("Comprobando estructura declaracion el token " + tokenIndividual.getNombre() + " estaod declara " + estadoDeclaracion);
        switch (estadoDeclaracion) {
            case '1':
                //Abajo entra a la letra y en base a eso cambia de estado
                switch (tokenIndividual.getTipo()) {
                    case "IDENTIFICADOR":
                        estadoDeclaracion = '2';
                        break;
                    default:
                        estado = 'E';
                }
                break;

            case '2':
                comprobarEsTipoDato(tokenIndividual, indiceToken);
                break;
            case '3':
                //En este caso también agrega los elementos de salida de I (ver la gráfica).
                //Añadir estos valores permite saber si 3 es un estado final o no
                switch (tokenIndividual.getNombre()) {
                    case "PRIMARY":
                        estadoDeclaracion = '4';
                        break;
                    case "NOT":
                        estadoDeclaracion = '5';
                        break;
                    case "UNIQUE":
                        estado = 'I';
                        comprobarEsEstructuraDeclaracion = false;
                        break;
                    case ")":
                        comprobarEsEstructuraDeclaracion = false;
                        estado = 'L';
                        break;
                    case ",":
                        comprobarEsEstructuraDeclaracion = false;
                        estado = 'J';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case '4':
                switch (tokenIndividual.getNombre()) {
                    case "KEY":
                        estado = 'I';
                        estadoDeclaracion = '1';
                        comprobarEsEstructuraDeclaracion = false;
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case '5':
                switch (tokenIndividual.getNombre()) {
                    case "NULL":
                        estado = 'I';
                        estadoDeclaracion = '1';
                        comprobarEsEstructuraDeclaracion = false;
                        break;
                    default:
                        estado = 'E';
                }
                break;
        }

        if (estado == 'E') {
            System.out.println("");
            System.out.println("Token error en declaracion: " + tokenIndividual);
            System.out.println("");
        }
    }

    private void comprobarEsTipoDato(Token tokenIndividual, int indiceToken) {
        System.out.println("Comprobando tipo de dato " + tokenIndividual.getNombre() + " estado tipo de dato " + estadoTipoDeDatos);
        switch (estadoTipoDeDatos) {
            case 'A':
                //Abajo entra a la letra y en base a eso cambia de estado

                if (tokenIndividual.getNombre().equals("SERIAL") || tokenIndividual.getNombre().equals("INTEGER")
                        || tokenIndividual.getNombre().equals("BIGINT") || tokenIndividual.getNombre().equals("DATE")
                        || tokenIndividual.getNombre().equals("TEXT") || tokenIndividual.getNombre().equals("BOOLEAN")) {

                    estadoTipoDeDatos = 'A';

                    if (tokens.get(indiceToken + 1).getNombre().equals("PRIMARY")
                                || tokens.get(indiceToken + 1).getNombre().equals("NOT")
                                || tokens.get(indiceToken + 1).getNombre().equals("UNIQUE")) {
                            estadoDeclaracion = '3';
                        } else {
                            estado = 'I';
                            estadoDeclaracion = '1';
                        }

                } else if (tokenIndividual.getNombre().equals("VARCHAR")) {
                    estadoTipoDeDatos = 'B';
                    System.out.println("ESTADO B");
                } else if (tokenIndividual.getNombre().equals("DECIMAL")) {
                    estadoTipoDeDatos = 'G';
                } else {
                    estado = 'E';
                }

                break;

            case 'B':
                switch (tokenIndividual.getNombre()) {
                    case "(":
                        estadoTipoDeDatos = 'C';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'C':
                switch (tokenIndividual.getTipo()) {
                    case "ENTERO":
                        estadoTipoDeDatos = 'D';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'D':
                switch (tokenIndividual.getNombre()) {
                    case ")":
                        estadoTipoDeDatos = 'A';

                        if (tokens.get(indiceToken + 1).getNombre().equals("PRIMARY")
                                || tokens.get(indiceToken + 1).getNombre().equals("NOT")
                                || tokens.get(indiceToken + 1).getNombre().equals("UNIQUE")) {
                            estadoDeclaracion = '3';
                        } else {
                            estado = 'I';
                            estadoDeclaracion = '1';
                        }

                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'G':
                switch (tokenIndividual.getNombre()) {
                    case "(":
                        estadoTipoDeDatos = 'H';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'H':
                switch (tokenIndividual.getTipo()) {
                    case "ENTERO":
                        estadoTipoDeDatos = 'I';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'I':
                switch (tokenIndividual.getNombre()) {
                    case ",":
                        estadoTipoDeDatos = 'J';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'J':
                switch (tokenIndividual.getTipo()) {
                    case "ENTERO":
                        estadoTipoDeDatos = 'K';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'K':
                switch (tokenIndividual.getNombre()) {
                    case ")":
                        estadoTipoDeDatos = 'A';

                        if (tokens.get(indiceToken + 1).getNombre().equals("PRIMARY")
                                || tokens.get(indiceToken + 1).getNombre().equals("NOT")
                                || tokens.get(indiceToken + 1).getNombre().equals("UNIQUE")) {
                            estadoDeclaracion = '3';
                        } else {
                            estado = 'I';
                            estadoDeclaracion = '1';
                        }

                        break;
                    default:
                        estado = 'E';
                }
                break;
        }

        if (estado == 'E') {
            System.out.println("");
            System.out.println("Token error en tipo de datos: " + tokenIndividual);
            System.out.println("");
        }
    }

    private void comprobarEsEstructuraLlaves(Token tokenIndividual) {
        System.out.println("Comprobando estructura llaves " + tokenIndividual.getNombre() + " estaod declara " + estadoEstructuraLlave);
        switch (estadoEstructuraLlave) {
            case 'A':
                switch (tokenIndividual.getNombre()) {
                    case "CONSTRAINT":
                        estadoEstructuraLlave = 'B';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'B':
                switch (tokenIndividual.getTipo()) {
                    case "IDENTIFICADOR":
                        estadoEstructuraLlave = 'C';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'C':
                switch (tokenIndividual.getNombre()) {
                    case "FOREIGN":
                        estadoEstructuraLlave = 'D';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'D':
                switch (tokenIndividual.getNombre()) {
                    case "KEY":
                        estadoEstructuraLlave = 'F';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'F':
                switch (tokenIndividual.getNombre()) {
                    case "(":
                        estadoEstructuraLlave = 'G';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'G':
                switch (tokenIndividual.getTipo()) {
                    case "IDENTIFICADOR":
                        estadoEstructuraLlave = 'H';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'H':
                switch (tokenIndividual.getNombre()) {
                    case ")":
                        estadoEstructuraLlave = 'I';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'I':
                switch (tokenIndividual.getNombre()) {
                    case "REFERENCES":
                        estadoEstructuraLlave = 'J';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'J':
                switch (tokenIndividual.getTipo()) {
                    case "IDENTIFICADOR":
                        estadoEstructuraLlave = 'P';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'P':
                switch (tokenIndividual.getNombre()) {
                    case "(":
                        estadoEstructuraLlave = 'L';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'L':
                switch (tokenIndividual.getTipo()) {
                    case "IDENTIFICADOR":
                        estadoEstructuraLlave = 'M';
                        break;
                    default:
                        estado = 'E';
                }
                break;
            case 'M':
                switch (tokenIndividual.getNombre()) {
                    case ")":
                        comprobarEsEstructuraLlaves = true;
                        estado = 'K';
                        break;
                    default:
                        estado = 'E';
                }
                break;
        }

        if (estado == 'E') {
            System.out.println("");
            System.out.println("Token error en estructura llaves: " + tokenIndividual);
            System.out.println("");
        }
    }

}
