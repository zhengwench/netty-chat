package chat.server.channel;

import chat.core.common.utility.CommonPropertiesUtility;
import io.netty.channel.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther a-de
 * @date 2018/11/5 15:10
 */
public class DomainChannelMap {
    private static final Logger logger = LoggerFactory.getLogger(DomainChannelMap.class);
    private static final Object _lock = new Object();

    private static final Object _lockOperate = new Object();

    private static Map<String, RoomChannelMap> domainMap;

    public static Map<String, RoomChannelMap> instance()
    {
        if (domainMap == null)
        {
            synchronized (_lock){
                if (domainMap == null){
                    domainMap = new ConcurrentHashMap<>();
                }
            }
        }
        return domainMap;
    }

    /**
     * 增加channel上下文
     * @param domain
     * @param roomId
     * @param token
     * @param userId
     * @param ip
     * @param channel
     */
    public static void addChannelContext(String domain,String roomId, String token, Long userId, String ip, Channel channel) {
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)) {
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap == null) {
            synchronized (_lockOperate) {
                if (instance().containsKey(domain)) {
                    roomChannelMap = instance().get(domain);
                } else {
                    int figure = Integer.parseInt(CommonPropertiesUtility.getProperty("channelMap.figure"));
                    roomChannelMap = new RoomChannelMap(figure);
                    instance().put(domain, roomChannelMap);
                }
            }
        }
        if (roomChannelMap != null) {
            roomChannelMap.addRoomContextMap(domain, roomId, token, userId, ip, channel);
        }
    }

    /**
     * 获得ContextMap
     * @param domain
     * @return
     */
    public static RoomChannelMap getDomainContextMap(String domain){
        if (instance().containsKey(domain)){
            return instance().get(domain);
        }
        return null;
    }

    /**
     * 获得ContextMap
     * @param domain
     * @return
     */
    public static Map<String,ChannelContextMap> getDomainRoomContextMap(String domain,String roomId){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap !=null){
            return roomChannelMap.getRoomContextMap(roomId);
        }
        return null;
    }

    /**
     * 移除domain下所有channel上下文
     * @param domain
     */
    public static void removeDomainContext(String domain){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
            instance().remove(domain);
        }
        if (roomChannelMap != null){
            roomChannelMap.removeAllContext();
        }
    }

    /**
     * 移除domain下room所有channel上下文
     * @param domain
     */
    public static void removeDomainRoomContext(String domain,String roomId){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap != null){
            roomChannelMap.removeRoomAllContext(roomId);
        }
    }

    /**
     * 移除domain下所有用户context
     * @param domain
     * @param userId
     */
    public static void removeDomainUserContext(String domain,Long userId){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap != null){
            roomChannelMap.removeUserAllContext(userId);
        }
    }

    /**
     * 移除游客ip channel
     * @param domain
     * @param ip
     */
    public static void removeIpChannelContext(String domain,String ip){
        RoomChannelMap roomChannelMap = DomainChannelMap.getDomainContextMap(domain);
        if (roomChannelMap!=null){
            roomChannelMap.removeIpChannelContext(ip);
        }
    }

    /**
     * 清理Channel上下文
     * @param domain
     */
    public static void cleanChannelContext(String domain){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap!=null){
            roomChannelMap.cleanChannelContext();
        }
    }

    /**
     * 检查发送者信息
     * @param domain
     * @param roomId
     * @param token
     * @return
     */
    public static boolean checkSender(String domain,String roomId,String userId,String token){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap == null){
            return false;
        }
        return roomChannelMap.checkSender(roomId,userId,token);
    }

    /**
     * 获取并移除用户channel
     * @param domain
     * @param userId
     * @return
     */
    public static List<ChannelContext> getAndRemoveUserAllChannelContext(String domain, Long userId){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap == null){
            return null;
        }
        return roomChannelMap.getAndRemoveUserAllChannelContext(userId);
    }

    public static ChannelContext getAndRemoveRoomUserChannelContext(String domain,String roomId, Long userId){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap == null){
            return null;
        }
        return roomChannelMap.getAndRemoveRoomUserChannelContext(roomId,userId);
    }

    /**
     * 移除channel上下文
     * @param domain
     * @param roomId
     * @param zoneKey
     * @param contextKey
     */
    public static ChannelContext getAndRemoveSingleChannelContext(String domain,String roomId,String zoneKey,String contextKey){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap != null){
            return roomChannelMap.getAndRemoveSingleChannelContext(roomId,zoneKey,contextKey);
        }
        return null;
    }

    /**
     * 获得需要发送的所有domain下房间context
     * @param domain
     * @param roomId
     */
    public static Map<String,ChannelContextMap> sendRoomMsg(String domain,String roomId){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap != null){
            return roomChannelMap.getSendRoomContext(roomId);
        }
        return null;
    }

    /**
     * 获取domain下发送所有的context
     * @param domain
     * @param msg
     */
    public static Map<String, Map<String,ChannelContextMap>> sendAllMsg(String domain,String msg) {
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)) {
            roomChannelMap = instance().get(domain);
        }
        if (roomChannelMap != null) {
            return roomChannelMap.getSendAllContext();
        }
        return null;
    }

    /**
     * 监控在线人数
     * @param domain
     * @return
     */
    public static Map<String,Integer> monitorOnline(String domain){
        RoomChannelMap roomChannelMap = null;
        if (instance().containsKey(domain)){
            roomChannelMap = instance().get(domain);
        }

        if (roomChannelMap != null){
            return roomChannelMap.monitorOnline();
        }
        return null;
    }

    /**
     * 监控在线人数
     * @return
     */
    public static Integer monitorOnlineCount(){
        Integer count = 0;
        RoomChannelMap roomChannelMap = null;
        Map<String, RoomChannelMap> roomMap = instance();
        if (roomMap!=null && roomMap.size()>0){
            for (String key:roomMap.keySet()) {
                try {
                    roomChannelMap = roomMap.get(key);
                    if (roomChannelMap!=null){
                        count += roomChannelMap.monitorOnlineCount();
                    }
                } catch (Exception e) {
                    logger.error("monitorOnlineCount异常",e);
                }
            }
        }
        return count;
    }
}
