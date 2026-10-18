package ru.itis.karimov.listener;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.postgresql.ds.PGSimpleDataSource;
import ru.itis.karimov.dao.DormitoryDao;
import ru.itis.karimov.dao.UniversityDao;
import ru.itis.karimov.dao.impl.DormitoryDaoImpl;
import ru.itis.karimov.dao.impl.UniversityDaoImpl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@WebListener
public class AppContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        Properties props = loadProperties();

        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setUrl(props.getProperty("db.url"));
        dataSource.setUser(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.password"));

        UniversityDao universityDao = new UniversityDaoImpl(dataSource);
        DormitoryDao dormitoryDao = new DormitoryDaoImpl(dataSource);

        ServletContext context = sce.getServletContext();
        context.setAttribute("universityDao", universityDao);
        context.setAttribute("dormitoryDao", dormitoryDao);
    }

    private Properties loadProperties() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new IllegalStateException("Не найден db.properties в classpath");
            }
            Properties props = new Properties();
            props.load(in);
            return props;
        } catch (IOException e) {
            throw new IllegalStateException("Не удалось прочитать db.properties", e);
        }
    }
}