package chat.udp.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.Date;

/**
 * @Author: zhengwenchao
 * @Date: 2019 2019-05-23 21:11
 */
public class ClientHandler extends SimpleChannelInboundHandler<Object> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

        DatagramPacket datagramPacket = (DatagramPacket)o;
        ByteBuf byteBuf = datagramPacket.content();
        System.out.println("客户端接收到消息:"+byteBuf.toString(CharsetUtil.UTF_8)+"当前时间"+new Date());
        channelHandlerContext.writeAndFlush(
                new DatagramPacket(Unpooled.copiedBuffer("123", CharsetUtil.UTF_8),
                        datagramPacket.sender()));
    }
}
