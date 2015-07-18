package listener;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import db.ChemieTestenDatabase;
import db.DatabaseInterface;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import service.Facade;

/**
 * Web application lifecycle listener.
 *
 * @author r0431118
 */
@WebListener()
public class ContextListener implements ServletContextListener {
    private Facade facade;
    private DatabaseInterface db;
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        //create database
        db = new ChemieTestenDatabase();
        db.connect();
        facade = new Facade(db);
        context.setAttribute("facade", facade);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
       db.disconnect();
    }
}
