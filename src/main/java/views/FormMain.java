package views;



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Formulario principal de la aplicación, en el se implementarán las opciones
 * de menú necesarias para poder utilizar la aplicación de BIBLIOTECA
 * @author AGE
 * @version 2
 */
public class FormMain extends JFrame implements ActionListener, FocusListener, WindowListener,KeyListener {
    private static FormMain main=null;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 756;
    private JDesktopPane desktopPane = new JDesktopPane();

    private JMenu mArchivo;{
        mArchivo=new JMenu("Archivo");
        mArchivo.setMnemonic('A');
    }

    private JMenuItem miAbrir;{
        miAbrir=new JMenuItem("Abrir..");
        miAbrir.setMnemonic('A');
        miAbrir.setFocusable(true);
        miAbrir.addActionListener(this);
        miAbrir.addFocusListener(this);
        //mArchivo.add(miAbrir); TODO pendiente de implementar
    }
    private JMenuItem miGuardarLibro;{
        miGuardarLibro =new JMenuItem("Guardar libros..");
        miGuardarLibro.setMnemonic('G');
        miGuardarLibro.setFocusable(true);
        miGuardarLibro.addActionListener(this);
        miGuardarLibro.addFocusListener(this);
        mArchivo.add(miGuardarLibro);
    }
    private JMenuItem miConexion;{
        miConexion =new JMenuItem("Conectar");
        miConexion.setMnemonic('C');
        miConexion.addActionListener(this);
        mArchivo.addSeparator();
        mArchivo.add(miConexion);
    }
    private JMenuItem miSalir;{
        miSalir=new JMenuItem("Salir");
        miSalir.setMnemonic('S');
        miSalir.setFocusable(true);
        miSalir.addActionListener(this);
        miSalir.addFocusListener(this);
        mArchivo.addSeparator();
        mArchivo.add(miSalir);
    }
    private JMenu mCategorias;{
        mCategorias =new JMenu("Categorias");
        mCategorias.setMnemonic('U');
        mCategorias.setFocusable(true);
        mCategorias.addFocusListener(this);
    }
    private JMenuItem miListaCategorias;{
        miListaCategorias=new JMenuItem("Lista");
        miListaCategorias.setMnemonic('L');
        miListaCategorias.setFocusable(true);
        miListaCategorias.addActionListener(this);
        miListaCategorias.addFocusListener(this);
        mCategorias.add(miListaCategorias);

    }
    private JMenuItem miNuevaCategoria;{
        miNuevaCategoria =new JMenuItem("Nuevo");
        miNuevaCategoria.setMnemonic('N');
        miNuevaCategoria.setFocusable(true);
        miNuevaCategoria.addActionListener(this);
        miNuevaCategoria.addFocusListener(this);
        mCategorias.add(miNuevaCategoria);
    }
    private JMenu mUsuarios;{
        mUsuarios =new JMenu("Usuarios");
        mUsuarios.setMnemonic('U');
        mUsuarios.setFocusable(true);
        mUsuarios.addFocusListener(this);
    }
    private JMenuItem miListaUsuarios;{
        miListaUsuarios=new JMenuItem("Lista");
        miListaUsuarios.setMnemonic('L');
        miListaUsuarios.setFocusable(true);
        miListaUsuarios.addActionListener(this);
        miListaUsuarios.addFocusListener(this);
        mUsuarios.add(miListaUsuarios);

    }
    private JMenuItem miNuevoUsuario;{
        miNuevoUsuario=new JMenuItem("Nuevo");
        miNuevoUsuario.setMnemonic('N');
        miNuevoUsuario.setFocusable(true);
        miNuevoUsuario.addActionListener(this);
        miNuevoUsuario.addFocusListener(this);
        mUsuarios.add(miNuevoUsuario);

    }
    private JMenu mLibros;{
        mLibros =new JMenu("Libros");
        mLibros.setMnemonic('L');
        mLibros.setFocusable(true);
        mLibros.addFocusListener(this);
    }
    private JMenuItem miListaLibros;{
        miListaLibros=new JMenuItem("Lista");
        miListaLibros.setMnemonic('L');
        miListaLibros.setFocusable(true);
        miListaLibros.addActionListener(this);
        miListaLibros.addFocusListener(this);
        mLibros.add(miListaLibros);

    }
    private JMenuItem miNuevoLibro;{
        miNuevoLibro=new JMenuItem("Nuevo");
        miNuevoLibro.setMnemonic('N');
        miNuevoLibro.setFocusable(true);
        miNuevoLibro.addActionListener(this);
        miNuevoLibro.addFocusListener(this);
        mLibros.add(miNuevoLibro);
    }
    private JMenu mPrestamos;{
        mPrestamos =new JMenu("Préstamos");
        mPrestamos.setMnemonic('P');
        mPrestamos.setFocusable(true);
        mPrestamos.addFocusListener(this);
    }
    private JMenuItem miListaPrestamos;{
        miListaPrestamos=new JMenuItem("Lista");
        miListaPrestamos.setMnemonic('L');
        miListaPrestamos.setFocusable(true);
        miListaPrestamos.addActionListener(this);
        miListaPrestamos.addFocusListener(this);
        mPrestamos.add(miListaPrestamos);
    }
    private JMenuItem miNuevoPrestamo;{
        miNuevoPrestamo = new JMenuItem("Nuevo");
        miNuevoPrestamo.setMnemonic('N');
        miNuevoPrestamo.setFocusable(true);
        miNuevoPrestamo.addActionListener(this);
        miNuevoPrestamo.addFocusListener(this);
        mPrestamos.add(miNuevoPrestamo);
    }
    private JMenuBar jMenuBar;{
        jMenuBar = new JMenuBar();
        jMenuBar.add(mArchivo);
        jMenuBar.add(mCategorias);
        jMenuBar.add(mUsuarios);
        jMenuBar.add(mLibros);
        jMenuBar.add(mPrestamos);
        jMenuBar.addFocusListener(this);
    }

    private FormMain(){
        setVentana();
        setContenedores();
        actualizaFormulario(false);
        addEventos();
    }

    private void addEventos() {
        addWindowListener(this);
        getContentPane().setFocusable(true);
        getContentPane().addKeyListener(this);
        getContentPane().addFocusListener(this);
    }

    private void setContenedores() {
        setLayout(new BorderLayout());
        add(jMenuBar,BorderLayout.NORTH);
        add(desktopPane,BorderLayout.CENTER);

    }

    private void setVentana() {
        setTitle("Aplicación de gestión de una biblioteca: ");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(0,0,WIDTH,HEIGHT);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    public JDesktopPane getDesktopPane() {
        return desktopPane;
    }

    /**
     * Este método habilitará o desactivará las distintas
     * opciones de menú del programa según corresponda
     */
    public void actualizaFormulario(boolean conectado) {
        miConexion.setEnabled(!conectado);
        miAbrir.setEnabled(conectado);
        mCategorias.setEnabled(conectado);
        mUsuarios.setEnabled(conectado);
        mLibros.setEnabled(conectado);
        mPrestamos.setEnabled(conectado);
    }
    /**
     * Método para la implementación del Singleton del formulario principal
     * @return el objeto global donde se instancia el formulario de la aplicación
     */
    public static FormMain getInstance(){
        if (main==null) {
            main = new FormMain();

        }
        return main;
    }



    private void salir() {
        if (JOptionPane.showConfirmDialog(FormMain.getInstance(),
                "¿Seguro que desea SALIR?",
                "Atención:",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION)
                System.exit(0);
    }


    @Override
    public void actionPerformed(ActionEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }
}
