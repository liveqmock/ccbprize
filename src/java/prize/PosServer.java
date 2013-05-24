package prize;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import prize.helper.ProjectConfigManager;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zr
 * Date: 13-4-13
 */
public class PosServer {
    private static final Logger logger = LoggerFactory.getLogger(PosServer.class);

    public static void main(String[] args) throws Exception {
        //System.out.println(System.getProperty("user.dir"));
        //System.setProperty("LINKING_HOME", webappHome);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap(
                    new NioServerSocketChannelFactory(
                            Executors.newCachedThreadPool(),
                            Executors.newCachedThreadPool()));

            bootstrap.setPipelineFactory(new PosServerPipelineFactory());

            int port = Integer.valueOf((String) ProjectConfigManager.getInstance().getProperty("posserver_listener_port"));
            bootstrap.bind(new InetSocketAddress(port));
            logger.info("POS兑奖服务器启动完成...");
        } catch (Exception e) {
            logger.error("服务器启动失败！", e);
            System.exit(-1);
        }
    }

}
