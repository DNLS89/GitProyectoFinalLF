package LOGICA;

import LOGICA.AnalizadorLexico;
import java.awt.Color;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class GestorPrograma {

    private String textoOriginal;
    private String[] lineasOriginal;
    private JTextPane panelTexto;
    private JTextPane txtColores;
    private StyledDocument doc;
    private SimpleAttributeSet attributeSet = new SimpleAttributeSet();
    private List<Token> tokens;
    private List<Token> tokensColores;
    private List<Token> erroresLexico;
    private List<Token> erroresSintacticos = new ArrayList<>();

    private List<List<Token>> todosLosComandos;
    private AnalizadorDDLyDML analizadorDDL;
    private Reportes reportes = new Reportes();
    

    private JButton btnOtrosReportes;
    private JButton btnReporteSintactico;
    private JButton btnGenerarGrafico;

    public GestorPrograma(JTextPane panelTexto, JTextPane txtColores, JButton btnOtrosReportes, JButton btnReporteSintactico, JButton btnGenerarGrafico) {
        this.panelTexto = panelTexto;
        this.txtColores = txtColores;
        this.doc = txtColores.getStyledDocument();
        this.btnOtrosReportes = btnOtrosReportes;
        this.btnReporteSintactico = btnReporteSintactico;
        this.btnGenerarGrafico = btnGenerarGrafico;
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
    
    public void procesar(String textoOriginal/*, JTextPane panelTexto, JTextPane txtColores, JButton btnOtrosReportes,
            JButton btnReporteSintactico, JButton btnGenerarGrafico*/) {
        this.textoOriginal = textoOriginal;
//        this.panelTexto = panelTexto;
//        this.txtColores = txtColores;
//        this.doc = txtColores.getStyledDocument();
//        this.btnOtrosReportes = btnOtrosReportes;
//        this.btnReporteSintactico = btnReporteSintactico;
//        this.btnGenerarGrafico = btnGenerarGrafico;

        obtenerMatrizLineas(textoOriginal);

        procesarAnalizadorLexico(this.textoOriginal);

        //imprimirValores();
        imprimirMatrizColores();

        procesarAnalizadorSintactico();

    }
    
    public void procesarDesdeArchivo (String[] matrizLineas, String textoArchivo) {
        
        obtenerMatrizLineas(textoArchivo);

        procesarAnalizadorLexico(textoArchivo);
        panelTexto.setText(textoArchivo);

        //imprimirValores();
        imprimirMatrizColores();

        procesarAnalizadorSintactico();
        
    }
    
    public void generarGrafico() {
        System.out.println("GRAFICOS -------------------------------------");
        //Generar Grafico CREATE
        if (!analizadorDDL.getComandosAceptadosCreate().isEmpty()) {
            analizadorDDL.getComandosAceptadosCreate();
            for (List<String> comando : analizadorDDL.getComandosAceptadosCreate()) {
                
                for (String elemento : comando) {
                    System.out.println(elemento);
                }
                
                
            }
            
            GraphViz graph = new GraphViz(analizadorDDL.getComandosAceptadosCreate());
            graph.generar("DiagramaCreacion.png", "DiagramaCreate.dot");
        }
        
        //Generar Grafico Modificadores
        if (!analizadorDDL.getComandosAceptadosModificacion().isEmpty()) {
            analizadorDDL.getComandosAceptadosModificacion();
            
            for (List<String> comando : analizadorDDL.getComandosAceptadosModificacion()) {
                
                for (String elemento : comando) {
                    System.out.println(elemento);
                }
                
                
            }
            
            
            
            GraphViz graph = new GraphViz(analizadorDDL.getComandosAceptadosModificacion());
            graph.generar("DiagramaModificacion.png", "DiagramaModificacion.dot");
        }
        

    }

    public void obtenerMatrizLineas(String textoOriginal) {

        this.lineasOriginal = textoOriginal.split("\n");

    }

    private void procesarAnalizadorLexico(String texto) {
        txtColores.setText("");
        try {

            AnalizadorLexico analizadorLexico = new AnalizadorLexico(new StringReader(texto));

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
            Logger.getLogger(GestorPrograma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void imprimirValores() {
        System.out.println("");
        System.out.println("Imprimiendo Tokens");
        System.out.println("");
        for (Token token : tokens) {
            System.out.println(token);
        }

//        System.out.println("");
//        System.out.println("Imprimiendo Tokens Colores");
//        System.out.println("");
//        for (Token token : tokensColores) {
//            System.out.println(token);
//        }

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
            Logger.getLogger(GestorPrograma.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void procesarAnalizadorSintactico() {

        this.analizadorDDL = new AnalizadorDDLyDML(tokens, lineasOriginal, btnOtrosReportes, btnReporteSintactico, btnGenerarGrafico);

        analizadorDDL.procesar();

    }
}
