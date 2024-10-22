package Automatas;

public class AutomataIdentificadorCSS extends Automata {

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
                    
                    if (Character.isLowerCase(letra)) {
                        estado = 'B';
                    } else {
                        estado = 'E';
                    }
                    break;

                case 'B':
                    if (Character.isLowerCase(letra)) {
                        estado = 'B';
                    } else if (Character.isDigit(letra)) {
                        estado = 'B';
                    } else if (letra == '-') {
                        estado = 'C';
                    } else {
                        estado = 'E';
                    }
                    break;

                case 'C':
                    if (Character.isLowerCase(letra)) {
                        estado = 'B';
                    } else if (Character.isDigit(letra)) {
                        estado = 'B';
                    } else {
                        estado = 'E';
                    }
                case 'E':
                    return false;

            }
        }
        
        if (estado == 'B') {
            return true;
        }

        return false;
    }
}
