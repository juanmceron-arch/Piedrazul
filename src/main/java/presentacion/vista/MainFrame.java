/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package presentacion.vista;


import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame(AgendarCitaForm agendarCitaForm, ConsultarCitasForm consultarCitasForm) {
        setTitle("Sistema de Gestión de Citas");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Agendar cita", agendarCitaForm);
        tabbedPane.addTab("Consultar citas", consultarCitasForm);

        add(tabbedPane, BorderLayout.CENTER);
    }
}