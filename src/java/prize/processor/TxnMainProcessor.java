package prize.processor;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import prize.PosRequest;
import prize.PosResponse;
import prize.helper.MD5Helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public abstract class TxnMainProcessor implements TxnProcessor {
    protected final static int LEN_MSG_HEADER = 92;
    protected final static int LEN_CARD_NO = 20;


    public Logger logger = Logger.getLogger(this.getClass());
    protected String clientUserId = "POS10001";

	public abstract void execute(PosRequest request, PosResponse response, Map<String, Object> model) throws TxnRunTimeException;

	public void execute(PosRequest request, PosResponse response) throws TxnRunTimeException {
		Map<String, Object> model = new HashMap<String, Object>();
		execute(request, response, model);
	}

    protected String getErrResponse(String txnCode, String posId, String errCode) {
        String dataLength = StringUtils.rightPad(("" + LEN_MSG_HEADER), 6, " ");
        String mac = MD5Helper.getMD5String("" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + clientUserId);

        String message = dataLength
                + posId
                + txnCode
                + errCode
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + mac;
        return message;
    }


}
