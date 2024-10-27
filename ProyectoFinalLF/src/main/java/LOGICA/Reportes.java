package LOGICA;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class Reportes {

    private List<List<Token>> todosLosComandos;
    private List<String> reporteModificadores = new ArrayList<>();
    private JTable tabla;

    public void setTodosLosComandos(List<List<Token>> todosLosComandos) {
        System.out.println("REPORTES");
        this.todosLosComandos = todosLosComandos;
    }

    public void obtenerOtrosReportes() {

        //Primer reporte
        String todoOtrosReportes = "TABLAS ENCONTRADAS--------------------------------------------\n";
        todoOtrosReportes+= "Se encontraron \"" + todosLosComandos.size() + "\" tablas \n"
                + "DESCRIPCIÓN:\n";

        int totalCreate = 0;
        int totalDelete = 0;
        int totalUpdate = 0;
        int totalSelect = 0;
        int totalAlter = 0;

        if (!todosLosComandos.isEmpty()) {
            for (List<Token> comando : todosLosComandos) {

                //Primer reporte
                todoOtrosReportes += "Tabla en la fila:\"" + comando.get(0).getFila()
                        + "\" y en la columna:\"" + comando.get(0).getColumna() + "\"\n";

                //Lo de abajo es del 3er reporte
                if (comando.get(0).getNombre().equals("CREATE")) {
                    totalCreate++;
                } else if (comando.get(0).getNombre().equals("DELETE")) {
                    totalDelete++;
                } else if (comando.get(0).getNombre().equals("UPDATE")) {
                    totalUpdate++;
                } else if (comando.get(0).getNombre().equals("SELECT")) {
                    totalSelect++;
                } else if (comando.get(0).getNombre().equals("ALTER")) {
                    totalAlter++;

                    //2do reporte, extraer datos de los modificadores
                    String datosModificacion = ("Tabla (fila: " + comando.get(0).getFila() + " columna: "+ comando.get(0).getColumna()+ ") modificada con ALTER: TABLA \""
                            + comando.get(2).getNombre() + "\""
                            + " modificada con: " + comando.get(3).getNombre() + " " + comando.get(4).getNombre() + " " + comando.get(5).getNombre());
                    reporteModificadores.add(datosModificacion);

                } else if (comando.get(0).getNombre().equals("DROP")) {
                    totalAlter++;
                    //2do reporte, extraer datos de los modificadores
                    String datosModificacion = ("Tabla (fila: " + comando.get(0).getFila() + " columna: "+ comando.get(0).getColumna()+ ") modificada con DROP: \"" + comando.get(4).getNombre() + "\""
                            + " tipo de modificación: DROP");
                    reporteModificadores.add(datosModificacion);
                }
            }

            
            todoOtrosReportes += "\n";
                todoOtrosReportes += "\nTablas modificadas y el tipo de modificación-------------------------------\n"
                        + "DESCRIPCION:\n";

            for (String modificacion : reporteModificadores) {
                
                todoOtrosReportes += modificacion;
                todoOtrosReportes += "\n";
            }

            //3er reporte
            todoOtrosReportes += "\n\nNúmero de operación por sección-------------------------------\n";
            todoOtrosReportes += "Total Elementos: " + " CREATE:" + totalCreate
                    + " DELETE:" + totalDelete + " UPDATE:" + totalUpdate + " SELECT:" + totalSelect
                    + " ALTER:" + totalAlter;
        }

        JTextArea texto = new JTextArea(30, 70);
        texto.setText(todoOtrosReportes);

        JOptionPane.showMessageDialog(null, new JScrollPane(texto));

    }

    public void reporte(String tipoReporte, List<Token> tokens, int longitud) {

        añadirDatosTabla(tipoReporte, tokens, longitud);
        mostrarTabla();

    }

    public void añadirDatosTabla(String tipoReporte, List<Token> tokens, int longitud) {

        this.tabla = new JTable(tokens.size(), longitud);

        JTableHeader HEADER = tabla.getTableHeader();
        TableColumnModel TMC = HEADER.getColumnModel();

        for (int indiceColumnaTabla = 0; indiceColumnaTabla < longitud; indiceColumnaTabla++) {

            if (tipoReporte.equals("ERRORESTokens")) {
                TableColumn TC = TMC.getColumn(indiceColumnaTabla);

                switch (indiceColumnaTabla) {
                    case 0:
                        TC.setHeaderValue("Token");
                        break;
                    case 1:
                        TC.setHeaderValue("Línea");
                        break;
                    case 2:
                        TC.setHeaderValue("Columna");
                        break;
                    case 3:
                        TC.setHeaderValue("Descripción");
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
                        TC.setHeaderValue("Tipo Token");
                        break;
                    case 2:
                        TC.setHeaderValue("Línea");
                        break;
                    case 3:
                        TC.setHeaderValue("Columna");
                        break;
                    case 4:
                        TC.setHeaderValue("Descripción");
                        break;
                    default:
                        break;
                }
            }

        }

        
        //LO de abajo añade los datos a la tabla
        if (tipoReporte.equals("ERRORESTokens")) {

            DefaultTableModel tableModel = (DefaultTableModel) tabla.getModel();
            tableModel.setRowCount(0);

            for (Token tokenIndividual : tokens) {
                String token = tokenIndividual.getNombre();
                int fila = tokenIndividual.getFila();
                int columna = tokenIndividual.getColumna();
                String descripcion = "Elemento no reconocido";
                

                tableModel.addRow(new Object[]{token, fila, columna, descripcion});
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
