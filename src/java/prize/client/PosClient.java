package prize.client;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import prize.helper.ProjectConfigManager;

import java.net.InetSocketAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zhanrui
 * Date: 13-4-14
 */
public class PosClient {
    public static void main(String[] args) throws Exception {
        String host = "127.0.0.1";
        int port = Integer.valueOf((String) ProjectConfigManager.getInstance().getProperty("posserver_listener_port"));

        ClientBootstrap bootstrap = new ClientBootstrap(
                new NioClientSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new PosClientPipelineFactory());

        //bootstrap.setOption("child.tcpNoDelay", true);
        //bootstrap.setOption("child.keepAlive", true);

        ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        Channel channel = future.awaitUninterruptibly().getChannel();

        String message = "92    " + "12345678901234567890123456789012"
                + "1001"
                + "9999"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + "MAC12345678901234567890123456789";
        channel.write(ChannelBuffers.wrappedBuffer(message.getBytes("GBK"))).awaitUninterruptibly();

        future = bootstrap.connect(new InetSocketAddress(host, port));
        channel = future.awaitUninterruptibly().getChannel();
        message = "114   " + "12345678901234567890123456789012"
                + "1002"
                + "9999"
                + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())
                + "MAC12345678901234567890123456789"
                + "CARD001             "   //信用卡卡号 20
                + "01"   //活动序号  2
                + "";
        channel.write(ChannelBuffers.wrappedBuffer(message.getBytes("GBK"))).awaitUninterruptibly();

        future.getChannel().getCloseFuture().awaitUninterruptibly();
        bootstrap.releaseExternalResources();
        System.exit(0);
    }
}
