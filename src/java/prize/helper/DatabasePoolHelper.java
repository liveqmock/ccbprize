package prize.helper;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-5-16
 * Time: ÏÂÎç12:05
 */
public class DatabasePoolHelper {
    private BasicDataSource dataSource;

    public Connection conn;

    public BasicDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(BasicDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Connection getConnection() {
        try {
            conn = dataSource.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return conn;
    }

    public void closeConnection(Connection conn) {
        if (null != conn) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public PreparedStatement getPrepStatement(Connection conn, String sql) {
        PreparedStatement prepStmt = null;
        try {
            prepStmt = conn.prepareStatement(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prepStmt;
    }


    public void closePrepStatement(PreparedStatement prepStmt) {
        if (null != prepStmt) {
            try {
                prepStmt.close();
                prepStmt = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
