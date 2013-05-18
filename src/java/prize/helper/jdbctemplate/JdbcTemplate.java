package prize.helper.jdbctemplate;

import org.apache.log4j.Logger;
import prize.helper.DBHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * User: zhanrui
 * Date: 13-5-16
 * Time: ÏÂÎç1:56
 */
public class JdbcTemplate {
    public static Logger logger = Logger.getLogger(JdbcTemplate.class);

    public final Object execute(StatementCallback callback) throws SQLException {
        Connection con = DBHelper.getConnection();
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            Object result = callback.doInStatement(stmt);
            return result;
        } catch (SQLException ex) {
            logger.error(ex);
            throw ex;
        } finally {
            try {
                stmt.close();
            } catch (SQLException e) {
                logger.error(e);
            }
            try {
                if (!con.isClosed()) {
                    try {
                        con.close();
                    } catch (SQLException e) {
                        logger.error(e);
                    }
                }
            } catch (SQLException e) {
                logger.error(e);
            }

        }
    }
}
