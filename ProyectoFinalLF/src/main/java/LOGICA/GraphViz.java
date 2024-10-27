package LOGICA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GraphViz {

    public GraphViz() {
    }

    public void generar() {
        escribirArchivo(obtenerCodigoGraphViz());
        ejecutarArchivoGraphViz();
    }

    public void ejecutarArchivoGraphViz() {
        ProcessBuilder process = new ProcessBuilder("dot", "-Tpng", "-o", "Diagrama.png", "Diagrama.dot");
        process.redirectErrorStream(true);
        try {
            process.start();
        } catch (IOException ex) {
            System.out.println("Error ejecutando doc GraphViz");
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String obtenerCodigoGraphViz() {

//        String[] letras = token.split("");
//
//        String codigoPorAgregar = "";
//
//        if (letras.length == 1) {
//            codigoPorAgregar += ("        \"" + letras[0] + "\"\n");
//        } else {
//            for (int indiceLetra = 0; indiceLetra < letras.length - 1; indiceLetra++) {
//                codigoPorAgregar += ("        \"" + letras[indiceLetra] + "\"->\"" + letras[indiceLetra + 1] + "\" [label = \"\"]\n");
//            }
//        }

        String codigo = "digraph {\n"
                + "  graph [pad=\"0.5\", nodesep=\"0.5\", ranksep=\"2\" ]  //  splines=ortho]\n"
                + "  node  [shape=plain]\n"
                + " // rankdir=LR;  // makes a very small difference\n"
                + "\n"
                + "Foo [label=<\n"
                + "<table border=\"0\" cellborder=\"1\" cellspacing=\"0\">\n"
                
                + "  <tr>  <td><i>Input Foo</i></td></tr>\n"
                + "  <tr>  <td port=\"1\">one</td></tr>\n"
                + "  <tr>  <td port=\"2\">two</td></tr>\n"
                + "  <tr>  <td port=\"3\">three</td></tr>\n"
                + "  <tr>  <td port=\"4\">four</td></tr>\n"
                + "  <tr>  <td port=\"5\">five</td></tr>\n"
                + "  <tr>  <td port=\"6\">six</td></tr>\n"
                
                + "</table>>];\n"
                + "\n"
                + "\n"
                + "}";

        //System.out.println("Codigo del botón: " + codigo);
        return codigo;
    }

    public void escribirArchivo(String codigo) {

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("Diagrama.dot"));
            writer.write(codigo);
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Token.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
