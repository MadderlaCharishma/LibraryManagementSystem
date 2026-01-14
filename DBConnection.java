package librarymanagement;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_db", // your database name
                "root", // username
                "1234567" // password
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
