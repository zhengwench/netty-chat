package chat.server.start;

import chat.server.handler.ChannelContextHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.FileInputStream;
import java.security.KeyStore;

/**
 * @auther a-de
 * @date 2018/11/5 12:28
 */

public class ChatWebSocketServer {
    private static Logger logger = LoggerFactory.getLogger(ChatWebSocketServer.class);

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    @Value("${websocket.port}")
    private Integer port;

    @Value("${https.status}")
    private boolean status;

    @Value("${https.filename}")
    private String filename;

    @Value("${https.keyPassword}")
    private String keyPassword;

    @Autowired
    private ChannelContextHandler channelContextHandler;

    /**
     * 启动服务器方法
     */
    public void init() {
        //接收客户端
        bossGroup = new NioEventLoopGroup();
        //处理具体读写
        workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //option用作bossgroup
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //childOption用作workerGroup
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                    //默认打开
                    //.childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            if (status){
                                SSLContext sslcontext = SSLContext.getInstance("TLS");
                                KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
                                KeyStore ks = KeyStore.getInstance("PKCS12");
                                ks.load(new FileInputStream(filename), keyPassword.toCharArray());
                                kmf.init(ks, keyPassword.toCharArray());
                                sslcontext.init(kmf.getKeyManagers(), null, null);
                                SSLEngine engine = sslcontext.createSSLEngine();
                                engine.setUseClientMode(false);
                                engine.setNeedClientAuth(false);
                                ch.pipeline().addFirst("ssl", new SslHandler(engine));
                            }
                            ch.pipeline().addLast(
                                    new HttpServerCodec(),   //请求解码器
                                    new HttpObjectAggregator(65536),//将多个消息转换成单一的消息对象
                                    new ChunkedWriteHandler(),  //支持异步发送大的码流，一般用于发送文件流
                                    //new IdleStateHandler(60, 0, 0), //检测链路是否读空闲
                                    channelContextHandler //消息处理
                            );
                        }
                    });
            // 绑定端口,开始接收进来的连接
            serverBootstrap.bind(port).sync();
            logger.info("netty服务启动: [port:" + port + "]");
        } catch (Exception e) {
            logger.error("netty服务启动异常-" + e.getMessage());
        }
    }

    public void shutdown(){
        try {
            if (bossGroup!=null){
                bossGroup.shutdownGracefully();
            }
            if (workerGroup!=null){
                workerGroup.shutdownGracefully();
            }
        } catch (Exception e) {
            logger.error("服务端关闭资源异常");
        }
    }
}
