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
        logger.info("�������յ����ģ�" + requestMessage);

        try {
            //1.MACУ��  ʵʱ��ȡ�Ƿ�У���־���������
            String macFlag = (String) ProjectConfigManager.getInstance().getProperty("posserver_mac_flag");
            if (macFlag != null && "1".equals(macFlag)) {//��У��
                //TODO
            }

            //2.��ȡ������
            String txnCode = requestMessage.substring(0 + 6 + 32, 0 + 6 + 32 + 4);
            logger.info("�������յ����ģ����׺�:" + txnCode);

            //3.����ҵ���߼��������
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
        logger.info("���������ر��ģ�" + responseMessage);
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
