package singleton;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Esta clase solo permitirá la instanciación de un único objeto
 * para definir y mantener la conexión con una BD MySQL
 * para su correcto funcionamiento requerirá de otro objeto configuración
 * @author AGE
 * @version 2
 */
public class ConexionMySQL {
    private static ConexionMySQL conexionHibernate = null;
    private SessionFactory sessionFactory;

    private ConexionMySQL(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    /**
     * Obtiene la sesión actual de Hibernate.
     * @return un objeto Session con la sesión actual de Hibernate
     */
    public Session getConexion() {
        return sessionFactory.getCurrentSession();
    }

    /**
     * Implementación del patrón de diseño Singleton para esta clase
     * @return instancia única del objeto de esta clase para la aplicación actual
     * @throws Exception posibles excepciones durante la inicialización de Hibernate
     */
    public static ConexionMySQL getInstance() throws Exception {
        if (conexionHibernate == null) {
            Configuracion myConf = Configuracion.getInstance();

            // Configuración de Hibernate
            Configuration config = new Configuration();
            config.configure("hibernate.cfg.xml");
            config.setProperty("hibernate.connection.driver_class", myConf.getDriver());
            config.setProperty("hibernate.connection.url", myConf.getUrl());
            config.setProperty("hibernate.connection.username", myConf.getUser());
            config.setProperty("hibernate.connection.password", myConf.getPassword());

            // Agregar clases mapeadas
            // config.addAnnotatedClass(TuClaseEntity.class);

            SessionFactory sessionFactory = config.buildSessionFactory();

            conexionHibernate = new ConexionMySQL(sessionFactory);
            System.out.println("Se ha conectado");
            Runtime.getRuntime().addShutdownHook(new MiApagado());
        }
        return conexionHibernate;
    }

    private void desconectar() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }

    private static class MiApagado extends Thread {
        @Override
        public void run() {
            try {
                if (conexionHibernate != null) {
                    conexionHibernate.desconectar();
                    conexionHibernate = null;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
