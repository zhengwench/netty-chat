package chat.udp.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

/**
 * @Author: zhengwenchao
 * @Date: 2019 2019-05-23 21:09
 */
public class Client {
    public static void main(String[] args){
        try {
            NioEventLoopGroup group = new NioEventLoopGroup();
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ChannelInitializer<NioDatagramChannel>() {
                        @Override
                        protected void initChannel(NioDatagramChannel ch) throws Exception {
                            ch.pipeline().addLast(new ClientHandler() );
                        }
                    });
            ChannelFuture future = bootstrap.bind(9100).sync();
            future.channel().writeAndFlush(
                    new DatagramPacket(Unpooled.copiedBuffer("123", CharsetUtil.UTF_8),
                            new InetSocketAddress("localhost", 9000)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        Channel channel = future.channel();
//        channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("hello", CharsetUtil.UTF_8), new InetSocketAddress("localhost", 9000)));
//        channel.closeFuture().syncUninterruptibly();
//        group.shutdownGracefully().syncUninterruptibly();
    }
}
