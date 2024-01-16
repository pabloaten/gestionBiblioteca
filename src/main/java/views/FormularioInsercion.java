package views;

import org.hibernate.SessionFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormularioInsercion extends JFrame {
    private SessionFactory sessionFactory;

    public FormularioInsercion(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

        setTitle("Inserción de Usuario");
        setSize(300, 150);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
    }

    private void initComponents() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel nombreLabel = new JLabel("Nombre:");
        final JTextField nombreField = new JTextField();

        JLabel apellidosLabel = new JLabel("Apellidos:");
        final JTextField apellidosField = new JTextField();

        JButton insertarButton = new JButton("Insertar");
        insertarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los datos del formulario
                String nombre = nombreField.getText();
                String apellidos = apellidosField.getText();

                // Lógica para insertar el nuevo usuario en la base de datos
                insertarUsuario(nombre, apellidos);

                // Cerrar el formulario después de la inserción
                dispose();
            }
        });

        panel.add(nombreLabel);
        panel.add(nombreField);
        panel.add(apellidosLabel);
        panel.add(apellidosField);
        panel.add(insertarButton);

        add(panel);
        setVisible(true);
    }

    private void insertarUsuario(String nombre, String apellidos) {
        // Lógica para insertar el usuario en la base de datos usando Hibernate
        // Debes implementar esta lógica según tu configuración y estructura de la base de datos
    }
}
