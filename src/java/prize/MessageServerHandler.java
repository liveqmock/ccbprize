package prize;

import org.apache.commons.lang.StringUtils;
import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prize.helper.MD5Helper;
import prize.helper.ProjectConfigManager;
import prize.processor.TxnProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
@ChannelPipelineCoverage("all")
public class MessageServerHandler extends SimpleChannelUpstreamHandler implements MessageConfig{
    private static final Logger logger = LoggerFactory.getLogger(MessageServerHandler.class);

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        if (!(e.getMessage() instanceof String)) {
            return;
        }
        String requestMessage = (String) e.getMessage();
        String responseMessage = "";
        logger.info("服务器收到报文：" + requestMessage);

        try {
            //1.MAC校验  实时获取是否校验标志，方便更新
            String macFlag = (String) ProjectConfigManager.getInstance().getProperty("posserver_mac_flag");
            if (macFlag != null && "1".equals(macFlag)) {//需校验
                //TODO
            }

            //2.获取交易码
            String txnCode = requestMessage.substring(0 + 6 + 32, 0 + 6 + 32 + 4);
            logger.info("服务器收到报文，交易号:" + txnCode);

            //3.调用业务逻辑处理程序
            Class clazz = Class.forName("prize.processor.T" + txnCode + "processor");
            TxnProcessor processor = (TxnProcessor)clazz.newInstance();

            PosRequest request = new PosRequest();
            request.setTxnCode(txnCode);
            request.setRequestMessage(requestMessage);

            PosResponse response = new PosResponse();

            processor.execute(request,response);
            responseMessage = response.getResponseMessage();
        }catch (Exception ex) {
            logger.error("Get  txn processor instance error.", ex);
            responseMessage = getErrResponse("1000");
        }

        e.getChannel().write(responseMessage);
        logger.info("服务器返回报文：" + responseMessage);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }

    private String getErrResponse(String errCode) {
        String dataLength = StringUtils.rightPad(("" + LEN_MSG_HEADER ), 6, " ");
        String currDate = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String mac = MD5Helper.getMD5String("" + currDate + clientUserId);
        String message =  dataLength
                + StringUtils.rightPad("", LEN_MSG_POSNO, " ")
                + "9999"
                + errCode
                + currDate
                + mac;
        return message;
    }
}
