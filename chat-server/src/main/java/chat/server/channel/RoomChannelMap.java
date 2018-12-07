package chat.server.channel;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @auther a-de
 * @date 2018/11/5 15:11
 */
public class RoomChannelMap {
    private static final Logger logger = LoggerFactory.getLogger(RoomChannelMap.class);
    private Map<String, Map<String, ChannelContextMap>> roomMap;

    private int figure;

    public RoomChannelMap(int _figure) {
        roomMap = new ConcurrentHashMap<>();
        figure = _figure;
    }

    public Set<String> getRoomKeys(){
        return roomMap.keySet();
    }

    /**
     * 增加房间channel上下文
     *
     * @param roomId
     * @param token
     * @param userId
     * @param ip
     * @param channel
     */
    public void addRoomContextMap(String domain, String roomId, String token, Long userId, String ip, Channel channel) {
        ChannelContextMap contextMap = null;
        Map<String, ChannelContextMap> channelMap = null;

        if (roomMap.containsKey(roomId)) {
            channelMap = roomMap.get(roomId);
        }
        if (channelMap == null) {
            synchronized (this) {
                if (roomMap.containsKey(roomId)) {
                    channelMap = roomMap.get(roomId);
                }
                if (channelMap == null) {
                    channelMap = new HashMap<>();
                    int zoneCount = 1;
                    for (int i = 0; i < figure; i++) {
                        zoneCount = zoneCount * 10;
                    }
                    for (int i = 0; i < zoneCount; i++) {
                        channelMap.put("zone_" + i, new ChannelContextMap());
                    }
                }
                roomMap.put(roomId, channelMap);
            }
        }
        if (channelMap != null) {
            String zoneKey = randomZone(ip, userId == null ? null : userId + "");
            contextMap = channelMap.get(zoneKey);
            if (contextMap != null) {
                contextMap.addChannelContext(domain, roomId, zoneKey, ip, userId, token, channel);
            }
        }
    }

    /**
     * 获得房间Channel上下文map
     *
     * @param roomId
     * @return
     */
    public Map<String, ChannelContextMap> getRoomContextMap(String roomId) {
        if (roomMap.containsKey(roomId)) {
            return roomMap.get(roomId);
        }
        return null;
    }

    public static void main(String[] args){
        String a = 10001+"";
        String hashcodestr = a.hashCode()+"";

        System.out.print("长度："+hashcodestr.length());
        String zone = hashcodestr.substring(hashcodestr.length() - 2);
        System.out.print("zone:"+zone);
        System.out.print("hashcode:"+a.hashCode());
    }

    public boolean checkSender(String roomId, String userId,String token) {
        Map<String, ChannelContextMap> channelMap = null;
        ChannelContextMap contextMap = null;
        if (roomMap.containsKey(roomId)) {
            channelMap = roomMap.get(roomId);
        }
        String zoneKey = randomZone(null, userId);
        if (channelMap != null) {
            if (channelMap.containsKey(zoneKey)) {
                contextMap = channelMap.get(zoneKey);
            }
        }
        if (contextMap != null) {
            return contextMap.checkToken(token);
        }
        return false;
    }

    /**
     * 获取并移除用户channel
     *
     * @param userId
     * @return
     */
    public List<ChannelContext> getAndRemoveUserAllChannelContext(Long userId) {
        List<ChannelContext> list = new ArrayList<>();
        Map<String, ChannelContextMap> channelMap = null;
        String zoneKey = randomZone(null, userId == null ? null : userId + "");
        ChannelContextMap contextMap = null;
        if (roomMap != null && roomMap.size() > 0) {
            for (String key : roomMap.keySet()) {
                try {
                    channelMap = roomMap.get(key);
                    if (channelMap != null && channelMap.size() > 0) {
                        try {
                            contextMap = channelMap.get(zoneKey);
                            if (contextMap != null) {
                                ChannelContext remove = contextMap.getAndRemoveUserIdChannelContext(userId);
                                if (remove != null) {
                                    list.add(remove);
                                }
                            }
                        } catch (Exception e) {
                            logger.error("getAndRemoveUserAllChannelContext异常", e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("getAndRemoveUserAllChannelContext异常", e);
                }
            }
        }
        return list;
    }

    public ChannelContext getAndRemoveRoomUserChannelContext(String roomId, Long userId) {
        Map<String, ChannelContextMap> channelMap = null;
        String zoneKey = randomZone(null, userId == null ? null : userId + "");
        ChannelContextMap contextMap = null;
        if (roomMap != null && roomMap.size() > 0) {
            if (roomMap.containsKey(roomId)) {
                channelMap = roomMap.get(roomId);
                if (channelMap != null && channelMap.size() > 0) {
                    contextMap = channelMap.get(zoneKey);
                    if (contextMap != null) {
                        ChannelContext remove = contextMap.getAndRemoveUserIdChannelContext(userId);
                        if (remove != null) {
                            return remove;
                        }
                    }
                }
            }
        }
        return null;
    }

    /**
     * 移除房间channel上下文map
     *
     * @param roomId
     * @return
     */
    public Map<String, ChannelContextMap> removeRoomContextMap(String roomId) {
        Map<String, ChannelContextMap> channelMap = null;
        if (roomMap.containsKey(roomId)) {
            synchronized (this) {
                if (roomMap.containsKey(roomId)) {
                    channelMap = roomMap.get(roomId);
                    roomMap.remove(roomId);
                }
            }
        }
        return channelMap;
    }

    /**
     * 移除房间下用户channel
     *
     * @param roomId
     * @param userId
     */
    public void removeRoomUserIdContext(String roomId, Long userId) {
        ChannelContextMap contextMap = null;
        Map<String, ChannelContextMap> channelMap = null;
        if (roomMap.containsKey(roomId)) {
            synchronized (this) {
                if (roomMap.containsKey(roomId)) {
                    channelMap = roomMap.get(roomId);
                }
            }
        }
        if (channelMap != null && channelMap.size() > 0) {
            try {
                for (String key : channelMap.keySet()) {
                    contextMap = channelMap.get(key);
                    if (contextMap != null) {
                        contextMap.removeUserIdChannelContext(userId);
                    }
                }
            } catch (Exception e) {
                logger.error("removeRoomUserIdContext异常",e);
            }
        }
    }

    /**
     * 移除房间下用户channel
     *
     * @param roomId
     * @param ip
     */
    public void removeRoomIpContext(String roomId, String ip) {
        ChannelContextMap contextMap = null;
        Map<String, ChannelContextMap> channelMap = null;
        if (roomMap.containsKey(roomId)) {
            synchronized (this) {
                if (roomMap.containsKey(roomId)) {
                    channelMap = roomMap.get(roomId);
                }
            }
        }
        if (channelMap != null) {
            try {
                for (String key : channelMap.keySet()) {
                    contextMap = channelMap.get(key);
                    if (contextMap != null) {
                        contextMap.removeIpChannelContext(ip);
                    }
                }
            } catch (Exception e) {
                logger.error("removeRoomIpContext异常",e);
            }
        }
    }

    /**
     * 清除ip channel
     * @param ip
     */
    public void removeIpChannelContext(String ip){
        Map<String, ChannelContextMap> channelMap = null;
        ChannelContextMap contextMap = null;
        String zoneKey = randomZone(ip,null);
        if (roomMap!=null && roomMap.size()>0){
            for (String key : roomMap.keySet()){
                try {
                    channelMap = roomMap.get(key);
                    if (channelMap != null) {
                        if (channelMap.containsKey(zoneKey)) {
                            contextMap = channelMap.get(zoneKey);
                        }
                        if (contextMap!=null){
                            contextMap.removeIpChannelContext(ip);
                        }
                    }
                } catch (Exception e) {
                    logger.error("removeIpChannelContext异常",e);
                }
            }
        }
    }

    /**
     * 移除房间下所有channel上下文
     *
     * @param roomId
     */
    public void removeRoomAllContext(String roomId) {
        ChannelContextMap contextMap = null;
        Map<String, ChannelContextMap> channelMap = null;
        if (roomMap.containsKey(roomId)) {
            channelMap = roomMap.get(roomId);
        }
        if (channelMap != null) {
            for (String key : channelMap.keySet()) {
                try {
                    contextMap = channelMap.get(key);
                    if (contextMap != null) {
                        contextMap.removeAllChannelContext();
                    }
                } catch (Exception e) {
                    logger.error("removeRoomAllContext异常",e);
                }
            }
        }
    }

    /**
     * 移除所有channel上下文
     */
    public void removeAllContext() {
        if (roomMap != null && roomMap.size() > 0) {
            Map<String, ChannelContextMap> contextMap = null;
            for (String key : roomMap.keySet()) {
                contextMap = roomMap.get(key);
                if (contextMap != null) {
                    Iterator<Map.Entry<String, ChannelContextMap>> it = contextMap.entrySet().iterator();
                    while (it.hasNext()) {
                        try {
                            Map.Entry<String, ChannelContextMap> entry = it.next();
                            if (entry.getValue() != null) {
                                entry.getValue().removeAllChannelContext();
                            }
                            it.remove();
                        } catch (Exception e) {
                            logger.error("removeAllContext异常",e);
                        }
                    }
                }
            }
        }
    }

    /**
     * 移除所有channel上下文
     */
    public void removeUserAllContext(Long userId) {
        String zone = randomZone(null, userId == null ? null : userId + "");
        if (roomMap != null && roomMap.size() > 0) {
            Map<String, ChannelContextMap> contextMap = null;
            for (String key : roomMap.keySet()) {
                try {
                    contextMap = roomMap.get(key);
                    if (contextMap != null) {
                        if (contextMap.containsKey(zone)) {
                            ChannelContextMap channelContextMap = contextMap.get(zone);
                            if (channelContextMap != null) {
                                channelContextMap.removeUserIdChannelContext(userId);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("removeUserAllContext异常", e);
                }
            }
        }
    }

    /**
     * 清理channel上下文
     */
    public void cleanChannelContext() {
        if (roomMap != null && roomMap.size() > 0) {
            Map<String, ChannelContextMap> contextMap = null;
            for (String key : roomMap.keySet()) {
                try {
                    contextMap = roomMap.get(key);
                    if (contextMap != null) {
                        Iterator<Map.Entry<String, ChannelContextMap>> it = contextMap.entrySet().iterator();
                        while (it.hasNext()) {
                            try {
                                Map.Entry<String, ChannelContextMap> entry = it.next();
                                if (entry.getValue() != null) {
                                    entry.getValue().cleanChannelContext();
                                }
                            } catch (Exception e) {
                                logger.error("cleanChannelContext异常",e);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("cleanChannelContext异常",e);
                }
            }
        }
    }

    public Map<String, Integer> monitorOnline() {
        Map<String, Integer> map = new HashMap();
        Map<String, ChannelContextMap> channelMap = null;
        ChannelContextMap contextMap = null;
        if (roomMap != null && roomMap.size() > 0) {
            for (String key : roomMap.keySet()) {
                try {
                    Integer onlineCount = 0;
                    channelMap = roomMap.get(key);
                    if (channelMap != null && channelMap.size() > 0) {
                        for (String channelkey : channelMap.keySet()) {
                            try {
                                contextMap = channelMap.get(channelkey);
                                if (contextMap != null) {
                                    onlineCount += contextMap.monitorOnline();
                                }
                            } catch (Exception e) {
                                logger.error("monitorOnline异常",e);
                            }
                        }
                    }
                    map.put(key, onlineCount);
                } catch (Exception e) {
                    logger.error("monitorOnline异常",e);
                }
            }
        }
        return map;
    }

    public Integer monitorOnlineCount() {
        Map<String, ChannelContextMap> channelMap = null;
        ChannelContextMap contextMap = null;
        Integer onlineCount = 0;
        if (roomMap != null && roomMap.size() > 0) {
            for (String key : roomMap.keySet()) {
                try {
                    channelMap = roomMap.get(key);
                    if (channelMap != null && channelMap.size() > 0) {
                        for (String channelkey : channelMap.keySet()) {
                            try {
                                contextMap = channelMap.get(channelkey);
                                if (contextMap != null) {
                                    onlineCount += contextMap.monitorOnline();
                                }
                            } catch (Exception e) {
                                logger.error("monitorOnlineCount异常",e);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("monitorOnlineCount异常",e);
                }
            }
        }
        return onlineCount;
    }

    public ChannelContext getAndRemoveSingleChannelContext(String roomId, String zoneKey, String contextKey) {
        Map<String, ChannelContextMap> channelMap = null;
        ChannelContextMap contextMap = null;
        if (roomMap.containsKey(roomId)) {
            channelMap = roomMap.get(roomId);
        }
        if (channelMap != null && channelMap.size() > 0) {
            if (channelMap.containsKey(zoneKey)) {
                contextMap = channelMap.get(zoneKey);
            }
        }
        if (contextMap != null) {
            return contextMap.getAndRemoveSingleChannelContext(contextKey);
        }
        return null;
    }

    /**
     * 发送所有消息
     */
    public Map<String, Map<String, ChannelContextMap>> getSendAllContext() {
        return roomMap;
    }

    /**
     * 获取需要发送消息的context
     *
     * @param roomId
     * @return
     */
    public Map<String, ChannelContextMap> getSendRoomContext(String roomId) {
        Map<String, ChannelContextMap> channelMap = null;
        if (roomMap.containsKey(roomId)) {
            channelMap = roomMap.get(roomId);
        }
        return channelMap;
    }

    /**
     * 获得分区
     *
     * @return
     */
    private String randomZone(String ip, String userId) {
        String key = null;
        if (!StringUtils.isEmpty(userId)) {
            key = userId;
        } else {
            key = ip;
        }

        if (StringUtils.isEmpty(key)){
            return "";
        }

        String zone = null;
        int hashCode = key.hashCode();
        String hashCodeStr = hashCode + "";
        if (!StringUtils.isEmpty(hashCodeStr)) {
            if (hashCodeStr.length() >= figure) {
                zone = hashCodeStr.substring(hashCodeStr.length() - figure);
            } else {
                zone = hashCodeStr.substring(hashCodeStr.length() - 1);
            }
        }
        int zoneint = Integer.parseInt(zone);
        return "zone_" + zoneint;
    }
}
