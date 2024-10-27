package LOGICA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphViz {

    private List<List<String>> comandosAceptadosCreate;

    public GraphViz(List<List<String>> comandosAceptadosCreate) {
        this.comandosAceptadosCreate = comandosAceptadosCreate;
    }

    public void generar(String nombreArchivo) {
        escribirArchivo(obtenerCodigoGraphViz(), nombreArchivo);
        ejecutarArchivoGraphViz(nombreArchivo);
    }

    public void ejecutarArchivoGraphViz(String nombreArchivo) {
        ProcessBuilder process = new ProcessBuilder("dot", "-Tpng", "-o", "Diagrama.png", nombreArchivo);
        process.redirectErrorStream(true);
        try {
            process.start();
        } catch (IOException ex) {
            System.out.println("Error ejecutando doc GraphViz");
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String obtenerCodigoGraphViz() {

        String codigoPorAgregar = "";

        for (List<String> comando : comandosAceptadosCreate) {

            for (String elemento : comando) {
                codigoPorAgregar += "  <tr>  <td> " + elemento + "</td></tr>\n";
            }
            codigoPorAgregar += "  <tr>  <td> </td></tr>\n";
            codigoPorAgregar += "  <tr>  <td> </td></tr>\n";
        }

        String codigo = "digraph {\n"
                + "  graph [pad=\"0.5\", nodesep=\"0.5\", ranksep=\"2\" ]  //  splines=ortho]\n"
                + "  node  [shape=plain]\n"
                + " // rankdir=LR;  // makes a very small difference\n"
                + "\n"
                + "Foo [label=<\n"
                + "<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">\n"
                + codigoPorAgregar
                + "</table>>];\n"
                + "\n"
                + "\n"
                + "}";

        System.out.println(codigo);
        
        return codigo;
    }

    public void escribirArchivo(String codigo, String nombreArchivo) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo));
            writer.write(codigo);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
