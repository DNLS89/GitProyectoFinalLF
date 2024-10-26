package LOGICA;

import AutomatasDDL.AutomataAlter;
import AutomatasDDL.AutomataCreate;
import AutomatasDDL.AutomataDrop;
import AutomatasDML.AutomataInsert;
import AutomatasDML.AutomataSelect;
import LOGICA.Token;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorDDLyDML {

    private List<Token> tokens;
    private String[] lineasOriginal;

    private List<List<Token>> todosLosComandos = new ArrayList<>();
    // private List<List<Token>> comandosNoAceptados = new ArrayList<>();

    public AnalizadorDDLyDML(List<Token> tokens, String[] lineasOriginal) {

        this.tokens = tokens;
        this.lineasOriginal = lineasOriginal;

    }

    public void procesar() {
        extraerElementosDDL();

        imprimirComandos();

        for (List<Token> comandoIndividual : todosLosComandos) {
            if (comandoCumpleRequisitos(comandoIndividual)) {
                //Genrerar la imagen
                
            }
        }

    }

    public void extraerElementosDDL() {

        boolean guardarElementos = false;

        List<Token> comandoIndividual = null;

        for (Token token : tokens) {
            if ((token.getNombre().equals("CREATE") || token.getNombre().equals("ALTER") || token.getNombre().equals("DROP")) && !guardarElementos
                    || (token.getNombre().equals("INSERT")) || (token.getNombre().equals("SELECT"))) {
                comandoIndividual = new ArrayList<>();
                guardarElementos = true;
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
        for (List<Token> comandoIndividual : todosLosComandos) {
            for (Token token : comandoIndividual) {
                System.out.print(token.getNombre() + "");

            }
            System.out.println(" ");
        }
    }

    public boolean comandoCumpleRequisitos(List<Token> comandoIndividual) {

        AutomataCreate automataCreate = new AutomataCreate(tokens);
        AutomataDrop automataDrop = new AutomataDrop();
        AutomataAlter automataAlter = new AutomataAlter(tokens);
        AutomataInsert automataInsert = new AutomataInsert(tokens);
        AutomataSelect automataSelect = new AutomataSelect(tokens);
        
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
        } else {
            System.out.println("NO cumple con los formatos anteriores");
            return false;
        }

        
    }

}
