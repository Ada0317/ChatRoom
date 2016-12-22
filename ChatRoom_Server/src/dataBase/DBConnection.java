package dataBase;

import java.sql.*;

/**
 * Created by hcyue on 2016/12/3.
 */

class ConnectionImpl extends DBConnection {
    ConnectionImpl() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:db/test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
    }
}


public abstract class DBConnection {

    protected Connection conn;

    public static DBConnection getInstance() {
        return new ConnectionImpl();
    }

    public void close() throws SQLException {
        conn.close();
    }

    public ResultSet query (String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        return rs;
    }

    public int update(String sql) throws SQLException {
        Statement stmt = conn.createStatement();
        int res = stmt.executeUpdate(sql);
//        conn.commit();
        return res;
    }
    public int insertAndGet(String sql) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pstmt.executeUpdate();
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            int id = rs.getInt(1);
            return id;
        }
        return -1;
    }

}
