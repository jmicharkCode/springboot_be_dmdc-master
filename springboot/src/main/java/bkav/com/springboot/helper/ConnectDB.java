package bkav.com.springboot.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {

    private static final Logger logger = LoggerFactory.getLogger(ConnectDB.class);

    public static Connection getConnection(String dbURL, String userName, String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, userName, password);
            logger.info("connect successfully!");
        } catch (Exception ex) {
            logger.error("connect failure!");
            ex.printStackTrace();
        }
        return conn;
    }
}
