package views;

import entity.UsuarioDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FormMain extends JFrame {
    private DefaultTableModel tableModel;
    private static FormMain main=null;

    public FormMain() {
        setTitle("Gestion de Biblioteca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Configuración de Hibernate
        final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

        // Crear el modelo de la tabla
        tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Nombre");
        tableModel.addColumn("Apellidos");

        // Crear la barra de menú
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Crear el menú "Usuarios"
        JMenu menuUsuarios = new JMenu("Usuarios");
        menuBar.add(menuUsuarios);

        // Crear la opción "Mostrar Tabla" dentro del menú
        JMenuItem mostrarTablaItem = new JMenuItem("Mostrar Tabla");
        menuUsuarios.add(mostrarTablaItem);

        // Agregar ActionListener para la opción "Mostrar Tabla"
        mostrarTablaItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para cargar y mostrar la tabla en una nueva ventana
                mostrarTabla(sessionFactory);
            }
        });

        // Crear la opción "Editar" dentro del menú
        JMenuItem editarItem = new JMenuItem("Editar");
        menuUsuarios.add(editarItem);

        // Agregar ActionListener para la opción "Editar"
        editarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Lógica para mostrar el formulario de edición
                mostrarFormularioEdicion();
            }
        });

        // Hacer visible la ventana principal
        setVisible(true);
        // Agregar este bloque de código al constructor de FormMain antes de hacer visible la ventana principal


    }


    public static FormMain getInstance(){
        if (main==null) {
            main = new FormMain();
            //main.loginPassword();
        }
        return main;
    }
    private void loginPassword() {
        new LoginPass(this,"Conectar BD:",true).setVisible(true);
    }
    public void actualizaFormulario(boolean conectado) {

    }


    private void cargarUsuarios(SessionFactory sessionFactory, JTable table) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            List<UsuarioDTO> usuarios = session.createQuery("FROM UsuarioDTO", UsuarioDTO.class).list();

            // Limpiar la tabla antes de cargar nuevos datos
            tableModel.setRowCount(0);

            for (UsuarioDTO usuario : usuarios) {
                Object[] row = {usuario.getId(), usuario.getNombre(), usuario.getApellidos()};
                tableModel.addRow(row);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mostrarTabla(final SessionFactory sessionFactory) {
        // Crear un nuevo JFrame para la tabla
        final JFrame frameTabla = new JFrame("Tabla de Usuarios");
        frameTabla.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameTabla.setSize(600, 400);
        frameTabla.setLocationRelativeTo(this);

        // Crear la tabla con el modelo
        final JTable table = new JTable(tableModel);

        // Agregar un MouseListener para manejar los clics en las filas
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    int row = table.rowAtPoint(e.getPoint());
                    table.getSelectionModel().setSelectionInterval(row, row);
                    mostrarMenuContextualEliminar(e.getX(), e.getY(), table,sessionFactory);
                }
            }
        });

        // Agregar la tabla a un JScrollPane para permitir desplazamiento si hay muchos registros
        JScrollPane scrollPane = new JScrollPane(table);

        // Agregar el JScrollPane al panel principal del JFrame de la tabla
        frameTabla.add(scrollPane, BorderLayout.CENTER);

        // Cargar datos de la base de datos y actualizar la tabla
        cargarUsuarios(sessionFactory, table);

        // Hacer visible el JFrame de la tabla
        frameTabla.setVisible(true);
    }

    private void mostrarMenuContextualEliminar(int x, int y, final JTable table, final SessionFactory sessionFactory) {
        JPopupMenu menuContextual = new JPopupMenu();

        JMenuItem eliminarItem = new JMenuItem("Eliminar");
        eliminarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Obtener el ID del usuario a eliminar
                    int userId = (int) table.getValueAt(selectedRow, 0);
                    System.out.println(userId);

                    // Mostrar un JOptionPane de confirmación
                    int confirmacion = JOptionPane.showConfirmDialog(
                            table,
                            "¿Estás seguro de querer borrar este usuario?",
                            "Confirmar eliminación",
                            JOptionPane.YES_NO_OPTION);

                    if (confirmacion == JOptionPane.YES_OPTION) {
                        // Realizar la acción de eliminación si el usuario confirma
                        eliminarUsuario(sessionFactory, userId);

                        // Actualizar la tabla después de la eliminación
                        cargarUsuarios(sessionFactory, table);
                    }
                }
            }
        });

        menuContextual.add(eliminarItem);

        menuContextual.show(table, x, y);
    }

    private void eliminarUsuario(SessionFactory sessionFactory, int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Cargar el usuario a eliminar
            UsuarioDTO usuario = session.get(UsuarioDTO.class, userId);

            // Verificar si el usuario existe antes de intentar eliminar
            if (usuario != null) {
                // Eliminar el usuario
                session.delete(usuario);

                // Confirmar la transacción
                session.getTransaction().commit();

                // Informar sobre la eliminación exitosa
                JOptionPane.showMessageDialog(
                        this,
                        "Usuario eliminado exitosamente",
                        "Eliminación exitosa",
                        JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Informar que el usuario no existe
                JOptionPane.showMessageDialog(
                        this,
                        "El usuario no existe",
                        "Error al eliminar",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // Manejar cualquier excepción que pueda ocurrir durante la eliminación
            JOptionPane.showMessageDialog(
                    this,
                    "Error al eliminar el usuario",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }




    private void mostrarFormularioEdicion() {
        // Implementa la lógica para mostrar el formulario de edición como lo hiciste anteriormente
    }
}
