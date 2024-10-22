package Automatas;

public class AutomataRGBA {
    String[] palabras;
    
    
    
    
    public boolean verificarPertenerAlAutomata(String linea) {
        System.out.println("Linea: " + linea);
        //Modificar la línea
        try {
            linea = linea.substring(4);
            linea = linea.replace(",", " ");
            linea = linea.replace("(", "( ");
            linea = linea.replace(")", " )");

            this.palabras = linea.split(" ");


            return perteneceAlAutomata();
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean perteneceAlAutomata() {
        String estado;

        //"A" es el estado inicial
        estado = "A";

        for (String letra : palabras) {
           // System.out.println("Letra: " + letra + " estado:" + estado);

            switch (estado) {
                case "A":
                    //Abajo entra a la letra y en base a eso cambia de estado
                    switch (letra) {
                        case "(":
                            estado = "B";
                            break;
                        default:
                            estado = "E";
                    }
                    break;

                case "B":
                    if (entreValoresAceptados(letra)) {
                        estado = "C";
                    } else {
                        estado = "E";
                    }
                    break;
                case "C":
                    if (entreValoresAceptados(letra)) {
                        estado = "D";
                    } else {
                        estado = "E";
                    }
                    break;
                case "D":
                    if (entreValoresAceptados(letra)) {
                        estado = "F";
                    } else {
                        estado = "E";
                    }
                    break;
                case "F":
                    if (isDecimal(letra)) {
                        estado = "G";
                    } else if (letra.equals(")")) {
                        estado = "H";
                    } else {
                        estado = "E";
                    }
                    break;
                case "G":
                    if (letra.equals(")")) {
                        estado = "H";
                    } else {
                        estado = "E";
                    }
                    break;
                case "H":
                    estado = "E";
                    break;
                case "E":
                   // System.out.println("No perte");
                    return false;
                //break;
            }
        }

        if (estado.equals("H")) {
            //System.out.println("Perte");
            return true;
        }
        //System.out.println("No perte");
        return false;
    }
    
    
    
    public boolean entreValoresAceptados(String entrada) {
        int valor;

        try {
            valor = Integer.parseInt(entrada);

        } catch (Exception e) {
            return false;
        }

        if (valor >= 0 && valor <= 255) {
            return true;
        }

        return false;
    }

    public boolean isDecimal(String palabra) {

        try {
            double valor = Double.parseDouble(palabra);

            return true;
        } catch (Exception e) {
        }

        return false;
    }
    
}

