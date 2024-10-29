package AutomatasDDL;

import LOGICA.Token;
import java.util.ArrayList;
import java.util.List;

public class AutomataDrop {

    private char estado = 'A';

    private List<List<String>> comandosAceptadosModificacion;
    private List<String> comandoEnAceptacion = new ArrayList<>();
    private List<String> datosTabla;
    private List<Token> erroresSintacticos;

    public AutomataDrop(List<List<String>> comandosAceptadosModificacion, List<Token> erroresSintacticos) {
        this.comandosAceptadosModificacion = comandosAceptadosModificacion;
        this.erroresSintacticos = erroresSintacticos;
    }

    public boolean verificarPerteneceAlAutomata(List<Token> comandoIndividual) {

        for (int indiceToken = 0; indiceToken < comandoIndividual.size(); indiceToken++) {
            Token token = comandoIndividual.get(indiceToken);

            switch (estado) {
                case 'A':
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (token.getNombre()) {
                        case "DROP":
                            estado = 'B';
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba \"DROP\"");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;

                case 'B':
                    switch (token.getNombre()) {
                        case "TABLE":
                            estado = 'C';
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba \"TABLE\"");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;

                case 'C':
                    switch (token.getNombre()) {
                        case "IF":
                            estado = 'D';
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba \"IF\"");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;
                case 'D':
                    switch (token.getNombre()) {
                        case "EXISTS":
                            estado = 'F';
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba \"EXISTS\"");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;
                case 'F':
                    switch (token.getTipo()) {
                        case "IDENTIFICADOR":
                            estado = 'G';
                            comandoEnAceptacion.add("DROP TABLE " + token.getNombre());
                            comandoEnAceptacion.add("DROP " + token.getNombre());
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba un Ident.");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;
                case 'G':
                    switch (token.getNombre()) {
                        case "CASCADE":
                            estado = 'H';
                            comandoEnAceptacion.add(token.getNombre());
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba \"CASCADE\"");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;

                //Abajo debe comprobar que tenga una estructura de declaración
                case 'H':
                    switch (token.getNombre()) {
                        case ";":
                            estado = 'I';
                            break;
                        default:
                            token.setDescripcionTokenError("Se esperaba \";\"");
                            erroresSintacticos.add(token);
                            estado = 'E';
                    }
                    break;

                case 'E':
                    // System.out.println("Token en el que detectó error DROP: " + comandoIndividual.get(indiceToken - 1) + " fila y columna " + token.getFila() + " " + token.getColumna());
                    //return false;
                //break;

            }
        }

        if (estado == 'I') {
            System.out.println("Cumpre con formato Drop");
            comandosAceptadosModificacion.add(comandoEnAceptacion);
            return true;
        } else {
            System.out.println("No cumple con formato Drop");
        }

        return false;
    }

}
