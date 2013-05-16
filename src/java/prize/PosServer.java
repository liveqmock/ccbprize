package prize;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zr
 * Date: 13-4-13
 */
public class PosServer {
    public static void main(String[] args) throws Exception {
        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new PosServerPipelineFactory());

        bootstrap.bind(new InetSocketAddress(50005));
        System.out.println("Pos Server started...");
    }
}
