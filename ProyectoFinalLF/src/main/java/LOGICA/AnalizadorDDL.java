package LOGICA;

import LOGICA.Token;
import java.util.ArrayList;
import java.util.List;

public class AnalizadorDDL {

    private List<Token> tokens;
    private String[] lineasOriginal;

    private List<List<Token>> todosLosComandos = new ArrayList<>();
    // private List<List<Token>> comandosNoAceptados = new ArrayList<>();

    public AnalizadorDDL(List<Token> tokens, String[] lineasOriginal) {

        this.tokens = tokens;
        this.lineasOriginal = lineasOriginal;

    }

    public void procesar() {
        extraerElementosDDL();

        imprimirComandos();

        for (List<Token> comandoIndividual : todosLosComandos) {
            if (comandoCumpleRequisitos(comandoIndividual)) {
                //Genrar la imagen
            }
        }

    }

    public void extraerElementosDDL() {

        boolean guardarElementos = false;

        List<Token> comandoIndividual = null;

        for (Token token : tokens) {
            if (token.getNombre().equals("CREATE") || token.getNombre().equals("ALTER")) {
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

    public boolean comandoCumpleRequisitos(List<Token> comando) {

        boolean dentroParentesis = false;
        boolean dentroEstrucDeclaracion = false;
        boolean dentroEstrucLlaves = false;
        
        
        
        if (!comando.get(1).getNombre().equals("TABLE") 
                || !comando.get(1).getNombre().equals("DATABASE") 
                || !comando.get(2).getTipo().equals("IDENTIFICADOR")) {
            return false;
        }
        
        for (Token token : comando) {
            
            
            
        }

        return true;
    }

}
