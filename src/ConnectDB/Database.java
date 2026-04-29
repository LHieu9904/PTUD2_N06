package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private static Database instance;
    private Connection con;

    private static final String URL =
            "jdbc:sqlserver://localhost:1433;databaseName=QLKSLuxury2;encrypt=true;trustServerCertificate=true;";
    private static final String USER = "sa";
    private static final String PASS = "sapassword";

    private Database() {}

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    // 🔥 KẾT NỐI
    public void connect() {
        try {
            if (con != null && !con.isClosed()) return;

            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("Kết nối DB thành công!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔥 LUÔN ĐẢM BẢO CÓ CONNECTION
    public Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return con;
    }
}