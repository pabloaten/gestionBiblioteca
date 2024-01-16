package views;

import entity.UsuarioDTO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        JMenuItem insertarItem = new JMenuItem("Insertar");
        JMenuItem editarItem = new JMenuItem("Editar");
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
        insertarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioInsercion(sessionFactory, table);
            }
        });
        editarItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarFormularioEdicion(sessionFactory, table);
            }
        });


        menuContextual.add(eliminarItem);
        menuContextual.add(editarItem);
        menuContextual.add(insertarItem);
        menuContextual.show(table, x, y);
    }
    private void mostrarFormularioInsercion(final SessionFactory sessionFactory, final JTable table) {
        // Crear un nuevo JFrame para el formulario de inserción
        final JFrame frameInsercion = new JFrame("Agregar Usuario");
        frameInsercion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameInsercion.setSize(300, 200);
        frameInsercion.setLocationRelativeTo(null);

        // Crear componentes del formulario
        JLabel lblNombre = new JLabel("Nombre:");
        final JTextField txtNombre = new JTextField();

        JLabel lblApellidos = new JLabel("Apellidos:");
        final JTextField txtApellidos = new JTextField();

        JButton btnGuardar = new JButton("Guardar");

        // Configurar el diseño del formulario
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblApellidos);
        panel.add(txtApellidos);
        panel.add(btnGuardar);

        // Agregar ActionListener para el botón Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los valores del formulario
                String nombre = txtNombre.getText();
                String apellidos = txtApellidos.getText();

                // Validar que se haya ingresado un nombre
                if (nombre.isEmpty()) {
                    JOptionPane.showMessageDialog(frameInsercion, "Por favor, ingrese un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear un nuevo usuario con los valores ingresados
                UsuarioDTO nuevoUsuario = new UsuarioDTO();
                nuevoUsuario.setNombre(nombre);
                nuevoUsuario.setApellidos(apellidos);

                // Guardar el nuevo usuario en la base de datos
                insertarUsuario(sessionFactory, nuevoUsuario);

                // Actualizar la tabla después de la inserción
                cargarUsuarios(sessionFactory, table);

                // Cerrar el formulario de inserción
                frameInsercion.dispose();
            }
        });

        // Agregar el panel al JFrame de inserción
        frameInsercion.add(panel);

        // Hacer visible el JFrame de inserción
        frameInsercion.setVisible(true);
    }
    private void mostrarFormularioEdicion(final SessionFactory sessionFactory, final JTable table) {
        // Verificar si se ha seleccionado una fila
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione un usuario para editar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Obtener el ID del usuario seleccionado
        int userId = (int) table.getValueAt(selectedRow, 0);

        // Obtener el usuario por ID
        final UsuarioDTO usuario = obtenerUsuarioPorId(sessionFactory, userId);

        if (usuario == null) {
            JOptionPane.showMessageDialog(this, "No se pudo obtener el usuario seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear un nuevo JFrame para el formulario de edición
        final JFrame frameEdicion = new JFrame("Editar Usuario");
        frameEdicion.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frameEdicion.setSize(300, 200);
        frameEdicion.setLocationRelativeTo(null);

        // Crear componentes del formulario de edición
        JLabel lblNombre = new JLabel("Nombre:");
        final JTextField txtNombre = new JTextField(usuario.getNombre());

        JLabel lblApellidos = new JLabel("Apellidos:");
        final JTextField txtApellidos = new JTextField(usuario.getApellidos());

        JButton btnGuardar = new JButton("Guardar");

        // Configurar el diseño del formulario de edición
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));
        panel.add(lblNombre);
        panel.add(txtNombre);
        panel.add(lblApellidos);
        panel.add(txtApellidos);
        panel.add(btnGuardar);

        // Agregar ActionListener para el botón Guardar
        btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Obtener los nuevos valores del formulario de edición
                String nuevoNombre = txtNombre.getText();
                String nuevosApellidos = txtApellidos.getText();

                // Validar que se haya ingresado un nombre
                if (nuevoNombre.isEmpty()) {
                    JOptionPane.showMessageDialog(frameEdicion, "Por favor, ingrese un nombre.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Actualizar el usuario con los nuevos valores
                usuario.setNombre(nuevoNombre);
                usuario.setApellidos(nuevosApellidos);

                // Guardar los cambios en la base de datos
                actualizarUsuario(sessionFactory, usuario);

                // Actualizar la tabla después de la edición
                cargarUsuarios(sessionFactory, table);

                // Cerrar el formulario de edición
                frameEdicion.dispose();
            }
        });

        // Agregar el panel al JFrame de edición
        frameEdicion.add(panel);

        // Hacer visible el JFrame de edición
        frameEdicion.setVisible(true);
    }

    public static void actualizarUsuario(SessionFactory sessionFactory, UsuarioDTO usuario) {
        Transaction transaction = null;

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            // Actualizar el usuario en la base de datos
            session.update(usuario);

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }


    public static UsuarioDTO obtenerUsuarioPorId(SessionFactory sessionFactory, int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Realizar la consulta para obtener el usuario por ID
            UsuarioDTO usuario = session.get(UsuarioDTO.class, userId);

            session.getTransaction().commit();

            return usuario;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void insertarUsuario(SessionFactory sessionFactory, UsuarioDTO usuario) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            // Guardar el nuevo usuario en la base de datos
            session.save(usuario);

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            // Manejar la excepción según tus necesidades
        }
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
