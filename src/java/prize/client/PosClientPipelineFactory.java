package prize.client;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import prize.MessageDecoder;
import prize.MessageEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-14
 */
public class PosClientPipelineFactory implements ChannelPipelineFactory {
    public ChannelPipeline getPipeline() throws Exception {
        ChannelPipeline pipeline = Channels.pipeline();

        pipeline.addLast("decoder", new MessageDecoder());
        pipeline.addLast("encoder", new MessageEncoder());
//        pipeline.addLast("T1001handler", new MessageClientT1001Handler());
//        pipeline.addLast("T1002handler", new MessageClientT1002Handler());
        pipeline.addLast("T2001handler", new MessageClientT2001Handler());

        return pipeline;
    }
}
