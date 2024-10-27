package LOGICA;

import GUI.Pantalla;
import com.yeferal.main.AnalizadorLexico;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
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
    private JTextPane txtColores;
    private StyledDocument doc;
    private SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    private List<Token> tokens;
    private List<Token> tokensColores;
    private List<Token> erroresLexico;

    private List<List<Token>> todosLosComandos;
    private AnalizadorDDLyDML analizadorDDL;
    private Reportes reportes = new Reportes();

    private JButton btnOtrosReportes;
    private JButton btnReporteSintactico;

    public GestorTexto() {
    }

    public String getTextoOriginal() {
        return textoOriginal;
    }

    public void setTextoOriginal(String textoOriginal) {
        this.textoOriginal = textoOriginal;
    }

    public AnalizadorDDLyDML getAnalizadorDDL() {
        return analizadorDDL;
    }

    public void setAnalizadorDDL(AnalizadorDDLyDML analizadorDDL) {
        this.analizadorDDL = analizadorDDL;
    }

    public List<Token> getErroresLexico() {
        return erroresLexico;
    }
    
    public void procesar(String textoOriginal, JTextPane panelTexto, JTextPane txtColores, JButton btnOtrosReportes,
            JButton btnReporteSintactico) {
        this.textoOriginal = textoOriginal;
        this.panelTexto = panelTexto;
        this.txtColores = txtColores;
        this.doc = txtColores.getStyledDocument();
        this.btnOtrosReportes = btnOtrosReportes;
        this.btnReporteSintactico = btnReporteSintactico;

        obtenerMatrizLineas(textoOriginal);

        procesarAnalizadorLexico();

        imprimirValores();
        imprimirMatrizColores();
        
        procesarAnalizadorSintactico();

    }

    public void obtenerMatrizLineas(String textoOriginal) {

        this.lineasOriginal = textoOriginal.split("\n");

    }

    private void procesarAnalizadorLexico() {
        txtColores.setText("");
        try {

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(new StringReader(textoOriginal));

            while (analizadorLexico.yylex() != AnalizadorLexico.YYEOF) {
            }
//            analizadorLexico.yylex();

            tokens = new ArrayList<>();
            tokensColores = new ArrayList<>();
            erroresLexico = new ArrayList<>();

            tokens = analizadorLexico.getLista();
            tokensColores = analizadorLexico.getListaColores();
            erroresLexico = analizadorLexico.getListaErrores();

        } catch (Exception ex) {
            System.out.println("Error extrayendo valores analizador léxico");
            ex.printStackTrace();
        }
    }

    public Color cambiarColor(Token token) {
        //Hay que ver la lógica según el analizador
        return token.getColorToken();
    }

    public void agregarCambioLinea() {
        try {
            doc.insertString(doc.getLength(), "\n", attributeSet);
        } catch (BadLocationException ex) {
            Logger.getLogger(GestorTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void imprimirValores() {
        System.out.println("");
        System.out.println("Imprimiendo Tokens");
        System.out.println("");
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println("");
        System.out.println("Imprimiendo Tokens Colores");
        System.out.println("");
        for (Token token : tokensColores) {
            System.out.println(token);
        }

        System.out.println("");
        System.out.println("Imprimiendo Errores");
        System.out.println("");
        for (Token tokenError : erroresLexico) {
            System.out.println(tokenError);
        }
    }

    private void imprimirMatrizColores() {
        //Colores

        int indice = 0;

        try {
            for (Token token : tokensColores) {

                //Lo de abajo es para imprimir el número de Línea
                if (indice != token.getFila()) {
                    doc.insertString(doc.getLength(), "|" + token.getFila() + "|  ", attributeSet);
                    indice = token.getFila();
                }

                StyleConstants.setForeground(attributeSet, token.getColorToken());
                txtColores.setCharacterAttributes(attributeSet, true);

                doc.insertString(doc.getLength(), token.getNombre(), attributeSet);

                //doc.insertString(doc.getLength(), lineasOriginal[indice], attributeSet);
                doc.insertString(doc.getLength(), " ", attributeSet);
            }
        } catch (BadLocationException ex) {
            System.out.println("Error imprimiendo colores");
            Logger.getLogger(GestorTexto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void procesarAnalizadorSintactico() {
        
        this.analizadorDDL = new AnalizadorDDLyDML(tokens, lineasOriginal, btnOtrosReportes, btnReporteSintactico);

        analizadorDDL.procesar();

    }
}
