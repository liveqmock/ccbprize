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
 * 3.1	��ǰ��Ч���ÿ����Ϣ��ѯ
 */
public class T1001processor extends TxnMainProcessor {
    private final static int LEN_EVENT_NAME = 20;
    @Override
    public void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException {
        String txnCode = request.getTxnCode();
        String posId = request.getRequestMessage().substring(0 + 6, 0 + 6 + 32);
        String errCode = "0000";

        String responseBody = "";
        try {
            Map<String, String> map = new HashMap<String, String>();
            responseBody = processTxn();
        } catch (Exception e) {
            logger.error("���״������.", e);
            errCode = "1000";
        }

        if ("1000".equals(errCode)) {
            response.setResponseMessage(getErrResponse(txnCode, posId, errCode));
        } else {
            String dataLength = null;
            try {
                dataLength = StringUtils.rightPad(("" + (LEN_MSG_HEADER + responseBody.getBytes("GBK").length)), 6, " ");
            } catch (UnsupportedEncodingException e) {
                logger.error("�������");
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
    }

    private String processTxn() throws UnsupportedEncodingException, SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate();

        String currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        final String sql = String.format("select act_no,act_name from B_M_ACT_POS_AWARD where act_begin_date <= '%s' and act_end_date >= '%s'", currDate, currDate);

        return (String) jdbcTemplate.executeQuery(new StatementCallback() {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException {
                String result = "";

                ResultSet rs = stmt.executeQuery(sql);
                try {
                    while (rs.next()) {
                        //��ѯ������ֶ���ʱ
                        String act_name = StringUtils.rightPad(rs.getString("act_name"), LEN_EVENT_NAME, " ");
                        byte[] bActName = act_name.getBytes("GBK");
                        if (bActName.length > LEN_EVENT_NAME) {
                            act_name = new String(bActName, 0 , LEN_EVENT_NAME);
                        }
                        result += rs.getString("act_no") + act_name;
                    }
                } catch (UnsupportedEncodingException e) {
                    logger.error("����ƴ�����ִ���", e);
                    throw new RuntimeException("����ƴ�����ִ���", e);
                }
                return result;
            }
        });
    }
}
