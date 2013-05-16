package prize;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;
import prize.processor.TxnProcessor;


/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
@ChannelPipelineCoverage("all")
public class MessageServerHandler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = Logger.getLogger(
            MessageServerHandler.class.getName());

    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
        if (!(e.getMessage() instanceof String)) {
            return;
        }
        String requestMessage = (String) e.getMessage();
        String responseMessage = "";
        System.err.println("requestMessage:" + requestMessage);

        /*
        1.MAC校验
        2.获取交易码
        3.调用业务逻辑处理程序
         */

        String mac = null;
        String txnCode = requestMessage.substring(0 + 6 + 32, 0 + 6 + 32 + 4);

        try {
            Class clazz = Class.forName("prize.processor.T" + txnCode + "processor");
            TxnProcessor processor = (TxnProcessor)clazz.newInstance();
            PosRequest request = new PosRequest();
            request.setTxnCode(txnCode);
            request.setRequestMessage(requestMessage);
            PosResponse response = new PosResponse();
            processor.execute(request,response);
            responseMessage = response.getResponseMessage();
        } catch (ClassNotFoundException e1) {
            logger.error("The txn processor class not found.");
        } catch (Exception e1) {
            logger.error("Get  txn processor instance error.");
        }
        e.getChannel().write(responseMessage);
    }

    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
