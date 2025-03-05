package io.kragelv.library.dao.connection;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionManager {
    private static final String PROPERTIES_FILE = "db.properties";
    private static String url;
    private static String user;
    private static String password;

    private ConnectionManager() { }

    static {
        try {
            
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL driver not found", e);
        }
        loadDatabaseProperties();
    }

    private static void loadDatabaseProperties() {
        Properties properties = new Properties();
        try (InputStream input = ConnectionManager.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE)) {
            if (input == null) {
                throw new PropertiesLoadException("Configuration file not found: " + PROPERTIES_FILE);
            }
            properties.load(input);
            url = properties.getProperty("db.url", System.getenv("DB_URL"));
            user = properties.getProperty("db.user", System.getenv("DB_USER"));
            password = properties.getProperty("db.password", System.getenv("DB_PASSWORD"));
        } catch (IOException e) {
            throw new PropertiesLoadException("Failed to load database properties", e);
        }
    }


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}
