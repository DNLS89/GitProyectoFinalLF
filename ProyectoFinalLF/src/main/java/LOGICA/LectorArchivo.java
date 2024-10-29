package LOGICA;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class LectorArchivo {

    private String pathEntrada;
    private int totalLineas = 0;
    private String[] matrizLineas;
    private String textoArchivo = "";
    private GestorPrograma gestorTexto;

    public LectorArchivo(GestorPrograma gestorTexto) {
        this.gestorTexto = gestorTexto;
    }

    public void abrirArchivo() {
        seleccionarPath();

        if (!estaVacio()) {
            contarLineas();

            conseguirMatrizLineas();

            gestorTexto.procesarDesdeArchivo(matrizLineas, textoArchivo);
        }
    }
    
    public boolean estaVacio() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathEntrada));
            String linea;

            while (((linea = reader.readLine()) != null) && !(linea = reader.readLine()).isBlank()) {
                return false;
            }
        } catch (Exception e) {
            //System.out.println("Error abriendo archivo");
            JOptionPane.showMessageDialog(null, "Error abriendo el documento", "ERROR", JOptionPane.PLAIN_MESSAGE);
            //e.printStackTrace();
        }
        
        return true;
    }

    public void conseguirMatrizLineas() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathEntrada));
            String linea;

            int fila = 0;
            while (((linea = reader.readLine()) != null)) {
                //System.out.println("LInea: " + linea);
                matrizLineas[fila] = linea;
                fila++;
                //System.out.println(linea);
            }
            obtenerStringArchivo();
        } catch (Exception e) {
            System.out.println("Error abriendo archivo");
            //e.printStackTrace();
        }
    }

    public void seleccionarPath() {
        JFileChooser fileChooser = new JFileChooser();
        int response = fileChooser.showOpenDialog(null); //select file to open
        if (response == JFileChooser.APPROVE_OPTION) {
            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
            this.pathEntrada = file.toString();
        }
    }

    public void contarLineas() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(pathEntrada));
            String linea;

            while ((linea = reader.readLine()) != null) {
                totalLineas++;
            }
        } catch (Exception e) {
            System.out.println("Error abriendo archivo");
            e.printStackTrace();
        }
        matrizLineas = new String[totalLineas];
    }
    
    public void obtenerStringArchivo() {
        for (int indiceLinea = 0 ; indiceLinea < matrizLineas.length; indiceLinea++) {
            if (indiceLinea < matrizLineas.length - 1) {
                textoArchivo += matrizLineas[indiceLinea] + "\n";
            } else {
                textoArchivo += matrizLineas[indiceLinea];
            }
            
        }
        
//        System.out.println("Texto archivo");
//        System.out.println(textoArchivo);
    }
}
