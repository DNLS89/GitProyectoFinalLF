
package AutomatasDDL;

public class AutomataCreate {
    
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
                        case '.':
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
                        estado = 'C';
                    } else if (Character.isDigit(letra)) {
                        estado = 'C';
                    } else if (letra == '-') {
                        estado = 'D';
                    } else {
                        estado = 'E';
                    }
                    break;

                case 'D':
                    if (Character.isLowerCase(letra)) {
                        estado = 'C';
                    } else if (Character.isDigit(letra)) {
                        estado = 'C';
                    } else {
                        estado = 'E';
                    }
                    break;
                case 'E':
                    return false;
                //break;

            }
        }
        
        if (estado == 'C') {
            return true;
        }

        return false;
    }
    
}
