package prize.client;

import org.jboss.netty.channel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
@ChannelPipelineCoverage("all")
public class MessageClientHandler extends SimpleChannelUpstreamHandler {
    private static final Logger logger = LoggerFactory.getLogger(MessageClientHandler.class);


    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        logger.info("客户端收到报文：" + e.getMessage());
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
