package chat.server.handler;

import com.alibaba.fastjson.JSONObject;
import chat.core.db.manager.BlackIpManager;
import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.RoleManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.UserManager;
import chat.core.db.model.BlackIp;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Role;
import chat.core.db.model.Room;
import chat.core.db.model.User;
import chat.server.channel.ChannelContext;
import chat.server.channel.DomainChannelMap;
import chat.server.channel.NettyChannelInfo;
import chat.server.command.ServerCommandEnum;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.InetSocketAddress;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static io.netty.handler.codec.http.HttpHeaderNames.HOST;

/**
 * @auther a-de
 * @date 2018/11/5 20:35
 */
@Component
@ChannelHandler.Sharable
public class ChannelContextHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger logger = LoggerFactory.getLogger(ChannelContextHandler.class);

    @Autowired
    private UserManager userManager;

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private BlackIpManager blackIpManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private MessageProcess messageProcess;

    private WebSocketServerHandshaker handshaker;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            try {
                handleHttpRequest(ctx, (FullHttpRequest) msg);
            } catch (Exception e) {
                logger.error("http处理异常",e);
            }
        } else if (msg instanceof WebSocketFrame) {
            try {
                handleWebSocket(ctx, (WebSocketFrame) msg);
            } catch (Exception e) {
                logger.error("websocket处理异常",e);
            }
        }
    }

    private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) {
        if (!request.decoderResult().isSuccess() || !"websocket".equals(request.headers().get("Upgrade"))) {
            logger.warn("protobuf don't support websocket");
            ctx.channel().close();
            return;
        }
        WebSocketServerHandshakerFactory handshakerFactory = new WebSocketServerHandshakerFactory(
                getWebSocketLocation(request), null, true);
        handshaker = handshakerFactory.newHandshaker(request);
        if (handshaker == null) {
            WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
        } else {
            // 动态加入websocket的编解码处理
            handshaker.handshake(ctx.channel(), request);

            String ip = getRemoteIP(ctx.channel(),request);

            Map<String,String> queryMap = getQuery(request.uri());
            if (queryMap==null){
                messageProcess.connectError(ctx.channel(),ServerCommandEnum.S_CONNECT_ERROR);
                ctx.channel().close();
                return;
            }

            String domain = "";
            String roomId = "";
            String token = "";
            if (queryMap.containsKey("domain")){
                domain = queryMap.get("domain");
            }
            if (queryMap.containsKey("roomId")){
                roomId = queryMap.get("roomId");
            }
            if (queryMap.containsKey("token")){
                token = queryMap.get("token");
            }
            if (StringUtils.isEmpty(domain) || StringUtils.isEmpty(roomId)){
                messageProcess.connectError(ctx.channel(),ServerCommandEnum.S_CONNECT_ERROR);
                ctx.channel().close();
                return;
            }

            DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
            if (domainConfig == null || domainConfig.getStatus() != 1 || new Date().before(domainConfig.getStartTime()) || new Date().after(domainConfig.getEndTime())){
                messageProcess.connectError(ctx.channel(),ServerCommandEnum.S_DOMAIN_ERROR);
                ctx.channel().close();
                return;
            }

            //校验ip
            BlackIp blackIp = blackIpManager.queryByIpCache(domainConfig.getId(),ip);
            if (blackIp!=null){
                messageProcess.connectError(ctx.channel(),ServerCommandEnum.S_IP_ERROR);
                ctx.channel().close();
                return;
            }

            //校验房间
            Room room = roomManager.queryByIdCache(Long.parseLong(roomId));
            if (room == null || !domainConfig.getId().equals(room.getDomainId())){
                messageProcess.connectError(ctx.channel(),ServerCommandEnum.S_ROOM_ERROR);
                ctx.channel().close();
                return;
            }
            String userId = null;
            String userName = null;
            String userIcon = null;
            String roleIcon = null;
            //已登录
            if (!StringUtils.isEmpty(token)){
                User user = userManager.queryByDomainIdAndToken(domainConfig.getId(),token);
                if (user == null){
                    messageProcess.connectError(ctx.channel(),ServerCommandEnum.S_LOGIN_INFO_LOSE);
                    ctx.channel().close();
                    return;
                }
                try {
                    userManager.updateLoginIp(user.getId(),ip);
                } catch (Exception e) {
                    logger.error("更新登录ip异常");
                }
                Role role = null;
                try {
                    role = roleManager.queryById(user.getRoleId());
                } catch (Exception e) {
                    logger.error("查询角色头像异常",e);
                }
                if (role!=null){
                    roleIcon = role.getRoleIcon();
                }
                userId = user.getId()+"";
                userName = user.getUserName();
                userIcon = user.getIcon();
                DomainChannelMap.removeDomainUserContext(domain,user.getId());
                DomainChannelMap.addChannelContext(domain,roomId,user.getToken(),user.getId(),ip,ctx.channel());
            }else {
                //游客
                DomainChannelMap.removeIpChannelContext(domain,ip);
                userName = randomName();
                DomainChannelMap.addChannelContext(domain, roomId, null, null, ip, ctx.channel());
            }
            JSONObject response = new JSONObject();
            response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            response.put("userId", userId);
            response.put("userName", userName);
            response.put("userIcon", userIcon);
            response.put("roleIcon", roleIcon);
            response.put("command", ServerCommandEnum.S_JOIN_ROOM.getKey());
            messageProcess.sendRoomMsg(domain, roomId, response.toJSONString());
            JSONObject historyResponse = new JSONObject();
            List<String> history = HistoryMessageHandler.getHistory(domain,roomId);
            historyResponse.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            historyResponse.put("command", ServerCommandEnum.S_HISTORY_CHAT.getKey());
            historyResponse.put("content", history);
            ctx.channel().writeAndFlush(new TextWebSocketFrame(historyResponse.toJSONString()));
        }
    }

    private void handleWebSocket(ChannelHandlerContext ctx, WebSocketFrame frame) {
        // 判断是否关闭链路命令
        if (frame instanceof CloseWebSocketFrame) {
            AttributeKey<NettyChannelInfo> infoKey = AttributeKey.valueOf("channelInfo");
            Attribute<NettyChannelInfo> attribute = ctx.channel().attr(infoKey);

            if (attribute.get()!=null){
                NettyChannelInfo nettyChannelInfo = attribute.get();
                ChannelContext channelContext = DomainChannelMap.getAndRemoveSingleChannelContext(nettyChannelInfo.getDomain(),nettyChannelInfo.getRoomId(),
                        nettyChannelInfo.getZoneKey(),nettyChannelInfo.getContextKey());

//                if (channelContext!=null){
//                    JSONObject response = new JSONObject();
//                    response.put("messageId",UUID.randomUUID().toString().replace("-",""));
//                    response.put("command",ServerCommandEnum.S_OUT_CHAT.getKey());
//                    response.put("userId",channelContext.getUserId());
//                    DomainChannelMap.sendRoomMsg(channelContext.getDomain(),channelContext.getRoomId(),response.toJSONString());
//                }
            }
            handshaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }
        // 判断是否Ping消息
        if (frame instanceof PingWebSocketFrame) {
            logger.info("ping message:{}", frame.content().retain());
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }
        // 判断是否Pong消息
        if (frame instanceof PongWebSocketFrame) {
            logger.info("pong message:{}", frame.content().retain());
            ctx.writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        // 本程序目前只支持文本消息
        if (frame instanceof TextWebSocketFrame) {
            try {
                TextWebSocketFrame textWebSocketFrame = (TextWebSocketFrame)frame;
                String message = textWebSocketFrame.text();
                messageProcess.execute(ctx.channel(),message);
            } catch (Exception e) {
                logger.error("消息发送异常",e);
            }
        }
    }

    public String getRemoteIP(Channel channel, FullHttpRequest httpRequest) {
        String ip = "";
        try{
            String ipForwarded = httpRequest.headers().get("x-forwarded-for");
            if (StringUtils.isBlank(ipForwarded) || "unknown".equalsIgnoreCase(ipForwarded)) {
                InetSocketAddress insocket = (InetSocketAddress)channel.remoteAddress();
                ip = insocket.getAddress().getHostAddress();
            } else {
                ip = ipForwarded;
            }
        }catch(Exception e){
            logger.error("getRemoteIP(): get remote ip fail!", e);
        }
        if("0:0:0:0:0:0:0:1".equals(ip)){
            ip = "127.0.0.1";
        }
        return ip;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        try {
            AttributeKey<NettyChannelInfo> infoKey = AttributeKey.valueOf("channelInfo");
            Attribute<NettyChannelInfo> attribute = ctx.channel().attr(infoKey);
            if (attribute.get()!=null) {
                NettyChannelInfo nettyChannelInfo = attribute.get();
                ChannelContext channelContext = DomainChannelMap.getAndRemoveSingleChannelContext(nettyChannelInfo.getDomain(),
                        nettyChannelInfo.getRoomId(), nettyChannelInfo.getZoneKey(),nettyChannelInfo.getContextKey());
                channelContext.getChannel().close();
            }
        } catch (Exception e) {
            logger.error("处理channel异常发生异常",e);
        }
        logger.error("connection error and close the channel", cause);
    }

    private Map<String,String> getQuery(String uri){
        uri = uri.replace("/","");
        Map<String,String> queryMap = new HashMap<>();
        if (StringUtils.isEmpty(uri)){
            return null;
        }

        String queryStr = uri.substring(uri.indexOf("?")+1);
        if (StringUtils.isEmpty(queryStr)){
            return null;
        }
        String[] array = queryStr.split("&");
        if (array!=null && array.length>0){
            for (int i = 0;i<array.length;i++){
                String[] resultArray = array[i].split("=");
                if (resultArray!=null && resultArray.length==2){
                    queryMap.put(resultArray[0],resultArray[1]);
                }
            }
        }
        return queryMap;
    }

    private static String getWebSocketLocation(FullHttpRequest req) {
        String location = req.headers().get(HOST);
        return "ws://" + location;
    }

    private String randomName() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("游客");
        try {
            String[] charArray = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
            Random random = new Random();
            for (int i = 0; i < 7; i++) {
                int index = random.nextInt(26);
                String indexvalue = charArray[index];
                stringBuilder.append(indexvalue);
            }
        } catch (Exception e) {
        }
        return stringBuilder.toString();
    }
}
