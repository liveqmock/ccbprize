package prize.helper;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-5-16
 * Time: ����1:51
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper {
    public static Logger logger = Logger.getLogger(DBHelper.class);

    private DBHelper(){
    }

    public static Connection getConnection(){
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "ccb", "ccb");
            return con;
        } catch (ClassNotFoundException e) {
            logger.error("���ݿ����ӳ�ʼ��ʧ�ܣ���������δ�ҵ���", e);
        } catch (SQLException e) {
            logger.error("���ݿ����ӳ�ʼ��ʧ�ܣ�", e);
        }
        return null;
    }

}
