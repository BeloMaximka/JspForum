package itstep.learning.services;

import com.google.inject.Singleton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Singleton
public class MySqlDbService {
    private Connection connection;

    public Connection getConnection() throws SQLException {
        if( connection == null ) {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            String connectionUrl = "jdbc:mysql://localhost:3306/jspforum?useUnicode=true&characterEncoding=utf8&useSSL=false";
            String username = "root";
            String password = "password";
            connection = DriverManager.getConnection(connectionUrl, username, password);
        }

        return connection;
    }
}