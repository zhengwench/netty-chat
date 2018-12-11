package chat.server.handler;

import chat.server.channel.ChannelContext;
import chat.server.channel.DomainChannelMap;
import chat.server.channel.NettyChannelInfo;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther a-de
 * @date 2018/11/5 20:38
 */
@Component
@ChannelHandler.Sharable
public class MessageHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(MessageHandler.class);

    @Autowired
    private MessageProcess messageProcess;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame frame) {
        try {
            String message = frame.text();
            messageProcess.execute(ctx.channel(),message);
        } catch (Exception e) {
            logger.error("消息发送异常",e);
        }
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        super.channelUnregistered(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            AttributeKey<NettyChannelInfo> infoKey = AttributeKey.valueOf("channelInfo");
            Attribute<NettyChannelInfo> attribute = ctx.channel().attr(infoKey);
            if (attribute.get()!=null) {
                NettyChannelInfo nettyChannelInfo = attribute.get();
                ChannelContext channelContext = DomainChannelMap.getAndRemoveSingleChannelContext(nettyChannelInfo.getDomain(),
                        nettyChannelInfo.getRoomId(), nettyChannelInfo.getZoneKey(),nettyChannelInfo.getContextKey(),nettyChannelInfo.getInfoId());
                if (channelContext!=null && channelContext.getChannel()!=null){
                    channelContext.getChannel().close();
                }
            }
        } catch (Exception e) {
            logger.error("处理channel异常发生异常",e);
        }
        logger.error("connection error and close the channel", cause);
    }
}
