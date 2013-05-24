package prize.client;

import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
@ChannelPipelineCoverage("all")
public class MessageClientT2001Handler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageClientT2001Handler.class);


    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        String message = "114   " + "12345678901234567890123456789012"
                + "2001"
                + "9999"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + "MAC12345678901234567890123456789"
                + "CARD001             "   //信用卡卡号 20
                + "01"   //活动序号  2
                + "";

        e.getChannel().write(message);
        logger.info("客户端发送报文 T2001：" + message);
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        logger.info("客户端收到报文 T2001：" + e.getMessage());
        try {
            Thread.sleep(1000*1);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        //e.getChannel().write(e.getMessage() + "" + new Date());
        e.getChannel().close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        logger.error("Unexpected exception from downstream.", e.getCause());
        e.getChannel().close();
    }
}
