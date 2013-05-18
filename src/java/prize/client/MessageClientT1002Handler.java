package prize.client;

import org.apache.log4j.Logger;
import org.jboss.netty.channel.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
@ChannelPipelineCoverage("all")
public class MessageClientT1002Handler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = Logger.getLogger(MessageClientT1002Handler.class);


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        String message = "114   " + "12345678901234567890123456789012"
                + "1002"
                + "9999"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + "MAC12345678901234567890123456789"
                + "CARD001             "   //���ÿ����� 20
                + "01"   //����  2
                + "";

        e.getChannel().write(message);
        logger.info("�ͻ��˷��ͱ��� T1002��" + message);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        logger.info("�ͻ����յ����� T1002��" + e.getMessage());
        try {
            Thread.sleep(1000*10);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //e.getChannel().write(e.getMessage() + "" + new Date());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
