package chat.server.start;

import chat.core.db.manager.DomainConfigManager;
import chat.core.db.manager.RoomManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Room;
import chat.server.channel.DomainChannelMap;
import chat.server.channel.RoomChannelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @auther a-de
 * @date 2018/11/8 22:24
 */
public class ChannelHeartBeatServer {
    private static Logger logger = LoggerFactory.getLogger(ChannelHeartBeatServer.class);

    @Autowired
    private DomainConfigManager domainConfigManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public void init() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                    Map<String, RoomChannelMap> domainMap = DomainChannelMap.instance();
                    if (domainMap != null && domainMap.size()>0) {
                        for (String key : domainMap.keySet()) {
                            logger.info("【" + key + "】开始清理channel");
                            threadPoolTaskExecutor.execute(new ChannelProcessThread(key, domainMap.get(key)));
                        }
                    }
                    }
                };
                Timer timer = new Timer();
                timer.schedule(timerTask, 0, 30 * 1000);
            }
        }).start();
    }

    class ChannelProcessThread implements Runnable{
        private String domain;
        private RoomChannelMap roomChannelMap;
        ChannelProcessThread(String _domain,RoomChannelMap _roomChannelMap){
            domain = _domain;
            roomChannelMap = _roomChannelMap;
        }
        @Override
        public void run() {
            try {
                DomainConfig domainConfig = domainConfigManager.queryByDomainName(domain);
                if (domainConfig == null || domainConfig.getStatus() != 1 || new Date().after(domainConfig.getEndTime()) ||
                        new Date().before(domainConfig.getStartTime())) {
                    DomainChannelMap.removeDomainContext(domainConfig.getDomainName());
                    return;
                }
                //清理房间
                try {
                    Set<String> roomkeys = roomChannelMap.getRoomKeys();
                    if (roomkeys!=null&&roomkeys.size()>0){
                        Room room = null;
                        for (String key:roomkeys) {
                            try {
                                room = roomManager.queryById(Long.parseLong(key));
                                if (room == null){
                                    roomChannelMap.removeRoomAllContext(key);
                                }
                            } catch (Exception e) {
                                logger.error("清理【"+key+"】房间channel异常",e);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("清理房间异常",e);
                }
                //清理无用channel
                DomainChannelMap.cleanChannelContext(domainConfig.getDomainName());
            } catch (Exception e) {
                logger.error("心跳清理channel异常",e);
            }
        }
    }
}
