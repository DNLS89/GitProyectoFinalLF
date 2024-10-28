package LOGICA;

import AutomatasDDL.AutomataAlter;
import AutomatasDDL.AutomataCreate2;
import AutomatasDDL.AutomataCreate;
import AutomatasDDL.AutomataDrop;
import AutomatasDML.AutomataDelete;
import AutomatasDML.AutomataInsert;
import AutomatasDML.AutomataSelect;
import LOGICA.Token;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

public class AnalizadorDDLyDML {

    private List<Token> tokens;
    private String[] lineasOriginal;
    private JButton btnOtrosReportes;
    private JButton btnReporteSintactico;
    private JButton btnGenerarGrafico;

    private List<List<Token>> todosLosComandos = new ArrayList<>();
    private List<List<String>> comandosAceptadosCreate = new ArrayList<>();
    private List<List<String>> comandosAceptadosModificacion = new ArrayList<>();
    
    private List<Token> erroresSintacticos = new ArrayList<>();
    
    // private List<List<Token>> comandosNoAceptados = new ArrayList<>();

    public AnalizadorDDLyDML(List<Token> tokens, String[] lineasOriginal, JButton btnOtrosReportes,
            JButton btnReporteSintactico, JButton btnGenerarGrafico) {

        this.tokens = tokens;
        this.lineasOriginal = lineasOriginal;
        this.btnOtrosReportes = btnOtrosReportes;
        this.btnReporteSintactico = btnReporteSintactico;
        this.btnGenerarGrafico = btnGenerarGrafico;

    }

    public List<List<Token>> getTodosLosComandos() {
        return todosLosComandos;
    }

    public List<List<String>> getComandosAceptadosCreate() {
        return comandosAceptadosCreate;
    }

    public List<List<String>> getComandosAceptadosModificacion() {
        return comandosAceptadosModificacion;
    }

    public List<Token> getErroresSintacticos() {
        return erroresSintacticos;
    }
    
    public void procesar() {
        extraerElementosDDL();

        imprimirComandos();

        int indice = 0;
        for (List<Token> comandoIndividual : todosLosComandos) {
            
            if (comandoCumpleRequisitos(comandoIndividual, indice)) {
                //Guarda los comandos para luego generar la imagen
                btnGenerarGrafico.setVisible(true);
                //comandosAceptadosCreate.add(comandoIndividual);
            }
            indice++;
        }

    }

    public void extraerElementosDDL() {

        boolean guardarElementos = false;

        List<Token> comandoIndividual = null;

        for (Token token : tokens) {
            if ((token.getNombre().equals("CREATE") || token.getNombre().equals("ALTER") || token.getNombre().equals("DROP")) && !guardarElementos
                    || (token.getNombre().equals("INSERT")) || (token.getNombre().equals("SELECT")) || (token.getNombre().equals("DELETE"))) {
                comandoIndividual = new ArrayList<>();
                guardarElementos = true;

                btnOtrosReportes.setVisible(true);
                

                //Muestra botones reportes
            } else {
                token.setDescripcionTokenError("Se esperaba CREATE | ALTER | DROP | INSERT | SELECT");
                btnReporteSintactico.setVisible(true);
                erroresSintacticos.add(token);
            }

            if (guardarElementos) {
                comandoIndividual.add(token);
            }

            if (token.getNombre().equals(";")) {
                guardarElementos = false;
                todosLosComandos.add(comandoIndividual);
            }
        }

    }

    public void imprimirComandos() {
        if (!todosLosComandos.isEmpty()) {
            for (List<Token> comandoIndividual : todosLosComandos) {
                for (Token token : comandoIndividual) {
                    System.out.print(token.getNombre() + "");

                }
                System.out.println(" ");
            }
        }
    }

    public boolean comandoCumpleRequisitos(List<Token> comandoIndividual, int indice) {

        //AutomataCreate automataCreate = new AutomataCreate(tokens, comandosAceptadosCreate);
        AutomataCreate automataCreate = new AutomataCreate(todosLosComandos, indice, comandosAceptadosCreate, erroresSintacticos);
        AutomataDrop automataDrop = new AutomataDrop(comandosAceptadosModificacion);
        //AutomataAlter automataAlter = new AutomataAlter(tokens, comandosAceptadosModificacion);
        AutomataAlter automataAlter = new AutomataAlter(comandosAceptadosModificacion);
        AutomataInsert automataInsert = new AutomataInsert(todosLosComandos, indice);
        AutomataSelect automataSelect = new AutomataSelect(todosLosComandos, indice);
        
        AutomataDelete automataDelete = new AutomataDelete(todosLosComandos, indice);

        if (comandoIndividual.get(0).getNombre().equals("CREATE")) {

            return automataCreate.verificarPerteneceAlAutomata(comandoIndividual);
        } else if (comandoIndividual.get(0).getNombre().equals("DROP")) {

            return automataDrop.verificarPerteneceAlAutomata(comandoIndividual);
        } else if (comandoIndividual.get(0).getNombre().equals("ALTER")) {

            return automataAlter.verificarPerteneceAlAutomata(comandoIndividual);
        } else if (comandoIndividual.get(0).getNombre().equals("INSERT")) {

            return automataInsert.verificarPerteneceAlAutomata(comandoIndividual);
        } else if (comandoIndividual.get(0).getNombre().equals("SELECT")) {
            return automataSelect.verificarPerteneceAlAutomata(comandoIndividual);
        } else if (comandoIndividual.get(0).getNombre().equals("DELETE")) {
            
            return automataDelete.verificarPerteneceAlAutomata(comandoIndividual);
        } else {
            System.out.println("NO cumple con los formatos anteriores");
            return false;
        }

    }

}
