package prize;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-13
 */
public class MessageDecoder  extends FrameDecoder {
    @Override
    protected Object decode(
            ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (buffer.readableBytes() < 6) {
            return null;
        }

        byte[] lengthBuffer = new byte[6];
        //buffer.readBytes(lengthBuffer);
        buffer.getBytes(0,lengthBuffer);

        int dataLength = Integer.parseInt(new String(lengthBuffer).trim());
        if (buffer.readableBytes() < dataLength) {
            return null;
        }

        byte[] decoded = new byte[dataLength];
        buffer.readBytes(decoded);
        String msg = new String(decoded);
        return msg;
    }
}
