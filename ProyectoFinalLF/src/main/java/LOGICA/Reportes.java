package LOGICA;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Reportes {

    private JTable tabla;

    public void reporte(String tipoReporte, ArrayList<Token> tokens, int longitud) {

        añadirDatosTabla(tipoReporte, tokens, longitud);
        mostrarTabla();

    }

    public void añadirDatosTabla(String tipoReporte, ArrayList<Token> tokens, int longitud) {

        this.tabla = new JTable(tokens.size(), longitud);

        JTableHeader HEADER = tabla.getTableHeader();
        TableColumnModel TMC = HEADER.getColumnModel();

        for (int indiceColumnaTabla = 0; indiceColumnaTabla < longitud; indiceColumnaTabla++) {

            if (tipoReporte.equals("Tokens") || tipoReporte.equals("Optimizacion")) {
                TableColumn TC = TMC.getColumn(indiceColumnaTabla);

                switch (indiceColumnaTabla) {
                    case 0:
                        TC.setHeaderValue("Token");
                        break;
                    case 1:
                        TC.setHeaderValue("Expresión Regular");
                        break;
                    case 2:
                        TC.setHeaderValue("Lenguaje");
                        break;
                    case 3:
                        TC.setHeaderValue("Tipo");
                        break;
                    case 4:
                        TC.setHeaderValue("Fila");
                        break;
                    case 5:
                        TC.setHeaderValue("Columna");
                        break;
                    default:
                        break;
                }

            } else {
                TableColumn TC = TMC.getColumn(indiceColumnaTabla);

                switch (indiceColumnaTabla) {
                    case 0:
                        TC.setHeaderValue("Token");
                        break;
                    case 1:
                        TC.setHeaderValue("Encontrado en el lenguaje");
                        break;
                    case 2:
                        TC.setHeaderValue("Lenguaje Sugerido");
                        break;
                    case 3:
                        TC.setHeaderValue("Fila");
                        break;
                    case 4:
                        TC.setHeaderValue("Columna");
                        break;
                    default:
                        break;
                }
            }

        }

        if (tipoReporte.equals("Tokens") || tipoReporte.equals("Optimizacion")) {

            DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();
            tableModel.setRowCount(0);

            for (Token tokenIndividual : tokens) {
                String token = tokenIndividual.getNombre();
                String tipo = tokenIndividual.getTipo();
                int fila = tokenIndividual.getFila();
                int columna = tokenIndividual.getColumna();

                tableModel.addRow(new Object[]{token, tipo, fila, columna});
            }
        } else {
            DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();
            tableModel.setRowCount(0);

            for (Token tokenIndividual : tokens) {
                String token = tokenIndividual.getNombre();
                //String tipo = tokenIndividual.getTipo();
                int fila = tokenIndividual.getFila();
                int columna = tokenIndividual.getColumna();

                tableModel.addRow(new Object[]{token, fila, columna});
            }
        }

    }

    public void mostrarTabla() {

        JOptionPane.showMessageDialog(null, new JScrollPane(tabla));
    }
}
