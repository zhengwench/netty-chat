package chat.server.config;

import chat.server.start.ChannelHeartBeatServer;
import chat.server.start.ChatWebSocketServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @auther a-de
 * @date 2018/11/5 12:35
 */
@Configuration
public class SocketServerConfig {
    private Logger logger = LoggerFactory.getLogger(SocketServerConfig.class);

    @Bean(name = "chatWebSocketServer", initMethod = "init", destroyMethod = "shutdown")
    public ChatWebSocketServer chatWebSocketServer() {
        return new ChatWebSocketServer();
    }

    @Bean(name = "channelHeartBeatServer", initMethod = "init")
    public ChannelHeartBeatServer channelHeartBeatServer(){
        return new ChannelHeartBeatServer();
    }
}
