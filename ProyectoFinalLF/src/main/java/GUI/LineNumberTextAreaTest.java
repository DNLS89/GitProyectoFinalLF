/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.Element;
public class LineNumberTextAreaTest {
   private static JTextArea textArea;
   private static JTextArea lines;
   private JScrollPane jsp;
   public LineNumberTextAreaTest(JScrollPane prueba) {
      this.jsp = prueba;
      //jsp = new JScrollPane();
      textArea = new JTextArea();
      lines = new JTextArea("1");
      lines.setBackground(Color.LIGHT_GRAY);
      lines.setEditable(false);
      //  Code to implement line numbers inside the JTextArea
      textArea.getDocument().addDocumentListener(new DocumentListener() {
         public String getText() {
            int caretPosition = textArea.getDocument().getLength();
            Element root = textArea.getDocument().getDefaultRootElement();
            String text = "1" + System.getProperty("line.separator");
               for(int i = 2; i < root.getElementIndex(caretPosition) + 2; i++) {
                  text += i + System.getProperty("line.separator");
               }
            return text;
         }
         @Override
         public void changedUpdate(DocumentEvent de) {
            lines.setText(getText());
         }
         @Override
         public void insertUpdate(DocumentEvent de) {
            lines.setText(getText());
         }
         @Override
         public void removeUpdate(DocumentEvent de) {
            lines.setText(getText());
         }
      });
      jsp.getViewport().add(textArea);
      jsp.setRowHeaderView(lines);
   }
   
   public String getText() {
       return textArea.getText();
   }
   
   public void setText(String textoIngresado) {
       textArea.setText(textoIngresado);
   }
   
   public JTextArea getTextArea() {
       return textArea;
   }
   
   
}