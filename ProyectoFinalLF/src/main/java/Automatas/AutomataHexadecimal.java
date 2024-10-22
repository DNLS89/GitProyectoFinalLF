package Automatas;

public class AutomataHexadecimal extends Automata {

    @Override
    public boolean verificarPerteneceAlAutomata(String entrada) {
        char estado;

        char[] letras = entrada.toCharArray();
        //"A" es el estado inicial
        estado = 'A';

        for (char letra : letras) {

            switch (estado) {
                case 'A':
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (letra) {
                        case '#':
                            estado = 'B';
                            break;
                        default:
                            estado = 'E';
                    }
                    break;

                case 'B':
                    if (Character.isLowerCase(letra)) {
                        estado = 'C';
                    } else if (Character.isDigit(letra)) {
                        estado = 'C';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'C':
                    if (Character.isLowerCase(letra)) {
                        estado = 'D';
                    } else if (Character.isDigit(letra)) {
                        estado = 'D';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'D':
                    if (Character.isLowerCase(letra)) {
                        estado = 'I';
                    } else if (Character.isDigit(letra)) {
                        estado = 'I';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'I':
                    if (Character.isLowerCase(letra)) {
                        estado = 'F';
                    } else if (Character.isDigit(letra)) {
                        estado = 'F';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'F':
                    if (Character.isLowerCase(letra)) {
                        estado = 'G';
                    } else if (Character.isDigit(letra)) {
                        estado = 'G';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'G':
                    if (Character.isLowerCase(letra)) {
                        estado = 'H';
                    } else if (Character.isDigit(letra)) {
                        estado = 'H';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'H':
                    estado = 'E';
                    break;

                case 'E':
                    return false;
                //break;

            }
        }

        if (estado == 'I' || estado == 'H') {
            return true;
        }
        
        return false;
    }
}
