package prize;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import java.util.concurrent.Executors;

/**
 * Created with IntelliJ IDEA.
 * User: zr
 * Date: 13-4-13
 */
public class PosServer {
    public static void main(String[] args) throws Exception {
        //System.out.println(System.getProperty("user.dir"));
        //System.setProperty("LINKING_HOME", webappHome);

        initenv();

        ServerBootstrap bootstrap = new ServerBootstrap(
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newCachedThreadPool()));

        bootstrap.setPipelineFactory(new PosServerPipelineFactory());

        bootstrap.bind(new InetSocketAddress(50005));
        System.out.println("POS兑奖服务器启动完成...");
    }

    private static void initenv() throws IOException {
        Properties prop = new Properties();
        InputStream fis = PosServer.class.getClassLoader().getResourceAsStream("prjcfg.properties");
        prop.load(fis);
        //prop.list(System.out);
        //System.out.println("===" + ProjectConfigManager.getInstance().getProperty("prj_root_dir"));
    }
}
