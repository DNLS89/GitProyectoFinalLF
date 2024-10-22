package LOGICA;

import GUI.Pantalla;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GestorTexto {

    private String textoOriginal;
    private String[] lineasOriginal;
    private JTextPane panelTexto;
    private StyledDocument doc;
    private SimpleAttributeSet attributeSet = new SimpleAttributeSet();

    private AnalizadorHTML analizadorHTML;
    private AnalizadorJS analizadorJS;
    private AnalizadorCSS analizadorCSS;

    public GestorTexto() {
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }

    public void setTextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public void procesar(String textoOriginal, JTextPane panelTexto) {
        this.textoOriginal = textoOriginal;
        this.panelTexto = panelTexto;
        this.doc = panelTexto.getStyledDocument();

        obtenerMatrizLineas(textoOriginal);

        procesarPalabras();

    }

    public void obtenerMatrizLineas(String textoOriginal) {

        //System.out.println("Obteniendo matriz Lineas");
        this.lineasOriginal = textoOriginal.split("\n");

    }

    private void procesarPalabras() {
        try {
            panelTexto.setText("");
            cambiarColor();
            
            int indiceActual = 0;

            for (int indice = 0; indice < lineasOriginal.length; indice++) {
                
                if (indiceActual != indice) {
                    agregarCambioLinea();
                    indiceActual = indice;
                }
                
                StyleConstants.setForeground(attributeSet, cambiarColor());
                
                panelTexto.setCharacterAttributes(attributeSet, true);
                
                doc.insertString(doc.getLength(), lineasOriginal[indice], attributeSet);
                
                //Falta definir el cambio de línea

            }

            StyleConstants.setForeground(attributeSet, Color.red);

            panelTexto.setCharacterAttributes(attributeSet, true);
            doc.insertString(0, "nada", attributeSet);

            StyleConstants.setForeground(attributeSet, Color.blue);

            panelTexto.setCharacterAttributes(attributeSet, true);

            doc.insertString(doc.getLength(), " bueno", attributeSet);
            

            doc.insertString(doc.getLength(), "manana", attributeSet);

            doc.insertString(doc.getLength(), "\n", attributeSet);
            doc.insertString(doc.getLength(), "ahora", attributeSet);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Color cambiarColor() {
        //Hay que ver la lógica según el analizador
        
        return Color.red;
    }
    
    public void agregarCambioLinea() {
        try {
            doc.insertString(doc.getLength(), "\n", attributeSet);
        } catch (BadLocationException ex) {
            Logger.getLogger(GestorTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generarHTML() {

        ArrayList<String> contenidoHTML = analizadorHTML.getContenidoOptimizadoHTML();
        ArrayList<String> contenidoCSS = analizadorCSS.getContenidoOptimizadoCSS();
        ArrayList<String> contenidoJS = analizadorJS.getContenidoOptimizadoJS();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("doc.html"));

            writer.write("<!DOCTYPE html>");
            writer.write("\n<html lang=\"en\">");
            writer.write("\n<head>");
            writer.write("\n    <meta charset=\"UTF-8\" />");
            writer.write("\n    <meta name=\"viewport\" content=\"width-device-width, initial-scale=1.0\">");
            writer.write("\n    <title>Documento</title>");
            writer.write("\n    <style>");
            /* Código CSS */
            for (String linea : contenidoCSS) {
                writer.write("\n" + linea);
            }

            writer.write("\n    </style>");
            writer.write("\n    <script>");
            /* Código JS */
            for (String linea : contenidoJS) {
                writer.write("\n" + linea);
            }

            writer.write("\n    </script>");
            writer.write("\n</head>");
            writer.write("\n<body>");
            /* Código HTML */
            for (String linea : contenidoHTML) {
                writer.write("\n" + linea);
                //System.out.println("Linea: " + linea);
            }

            writer.write("\n</body>");
            writer.write("\n</html>");

            writer.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void reporteTokens() {
        ArrayList<Token> tokens = new ArrayList<>();

    }

}
