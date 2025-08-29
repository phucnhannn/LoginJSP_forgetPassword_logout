package utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private final String serverName = "JOHNNYBUIII";
    private final String dbName = "loginjsp";
    private final String portNumber = "1433";
    private final String userID = "sa";
    private final String password = "1";

    public Connection getConnection() throws Exception {
        String url = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, userID, password);
    }
}
