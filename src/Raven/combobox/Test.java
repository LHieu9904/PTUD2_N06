/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Raven.combobox;

import javax.swing.*;
import java.awt.*;

public class Test extends javax.swing.JFrame {

    public Test() {
        initComponents();
        getContentPane().setBackground(new Color(255, 255, 255));
        getContentPane().setLayout(null);
        
        JPanel panel = new JPanel();
        panel.setBounds(58, 5, 132, 52);
        getContentPane().add(panel);
        panel.setLayout(new BorderLayout(0, 0));
        
        Combobox combobox1 = new Combobox();
        panel.add(combobox1, BorderLayout.CENTER);
        combobox1.setSelectedIndex(-1);
        combobox1.setLabeText("Combo Custom");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jButton1 = new javax.swing.JButton();
        combobox2 = new Combobox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        combobox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item1", "Item2", "Item3", "Item1", "Item2", "Item3", "Item1", "Item2", "Item3" }));
        combobox2.setSelectedIndex(-1);
        combobox2.setLabeText("Combo Custom");

        setLocationRelativeTo(null);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        combobox1.setSelectedIndex(-1);
    }

    public static void main(String args[]) {  
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Test().setVisible(true);
            }
        });
    }
    private Combobox combobox1;
    private Combobox combobox2;
    private javax.swing.JButton jButton1;
}
