package prize.processor;

import org.apache.commons.lang.StringUtils;
import prize.PosRequest;
import prize.PosResponse;
import prize.helper.MD5Helper;
import prize.helper.jdbctemplate.JdbcTemplate;
import prize.helper.jdbctemplate.StatementCallback;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 3.2	兑奖资格查询交易
 */
public class T1002processor extends TxnMainProcessor {

    @Override
    public void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException {
        String txnCode = request.getTxnCode();
        String posId = request.getRequestMessage().substring(0 + 6, 0 + 6 + 32);
        String errCode = "XXXX";

        String cardNo = request.getRequestMessage().substring(LEN_MSG_HEADER, LEN_MSG_HEADER + LEN_CARD_NO).trim();
        String eventNo = request.getRequestMessage().substring(LEN_MSG_HEADER + LEN_CARD_NO, LEN_MSG_HEADER + LEN_CARD_NO + 2);
        String responseBody = "";
        String dataLength = "";
        try {
            Map<String, String> resultMap = new HashMap<String, String>();
            responseBody = processTxn(cardNo, eventNo);
            dataLength = StringUtils.rightPad(("" + (LEN_MSG_HEADER + (responseBody.getBytes("GBK").length))), 6, " ");
            errCode = "0000";
        } catch (Exception e) {
            logger.error("交易处理错误.", e);
            errCode = "1000";
        }


        String mac = MD5Helper.getMD5String(responseBody + new SimpleDateFormat("yyyyMMdd").format(new Date()) + clientUserId);

        String message = dataLength
                + posId
                + txnCode
                + errCode
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + mac
                + responseBody;


        response.setResponseMessage(message);
    }

    private String processTxn(String cardNo, String eventNo) throws UnsupportedEncodingException, SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        final String sql = String.format("select award_flag from B_S_ACT_INFO where crd_no = '%s' and act_no = '%s'", cardNo, eventNo);
        String result = (String) jdbcTemplate.executeQuery(new StatementCallback() {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException {
                String result = "XX";
                int count = 0;

                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    //查询结果出现多条时
                    result = rs.getString("award_flag");
                    count++;
                }

                if (count != 1) {
                    logger.error("兑奖资格查询时出现重复记录。");
                    throw new RuntimeException("兑奖资格查询时出现重复记录");
                }

                return result;
            }
        });
        return  StringUtils.rightPad(cardNo, LEN_CARD_NO, " ") + StringUtils.rightPad(eventNo, 2, " ") + StringUtils.rightPad(result, 2, " ");
    }
}
