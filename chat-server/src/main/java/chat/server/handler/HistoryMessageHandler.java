package chat.server.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import chat.core.common.redis.RedisManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther a-de
 * @date 2018/11/14 12:50
 */
public class HistoryMessageHandler {
    private static Logger logger = LoggerFactory.getLogger(HistoryMessageHandler.class);

    public static void insert(String domain, String roomId, String msg) {
        try {
            String key = domain + "_" + roomId + "_HISORYMESSAGE";
            RedisManager.lpush(key.hashCode() + "", msg);
            RedisManager.ltrim(key.hashCode() + "");
        } catch (Exception e) {
            logger.error("查询历史消息异常",e);
        }
    }

    public static List<String> getHistory(String domain, String roomId) {
        List<String> list = new ArrayList<>();
        try {
            String key = domain + "_" + roomId + "_HISORYMESSAGE";
            list = RedisManager.lrange(key.hashCode() + "");
            return list;
        } catch (Exception e) {
            logger.error("获取历史消息异常",e);
        }
        return list;
    }

    public static void remove(String domain,String roomId,String messageId){
        List<String> list = getHistory(domain,roomId);
        String msg = null;
        if (list!=null && list.size()>0){
            JSONObject jsonObject = null;
            String mid = null;
            for (String r : list){
                jsonObject = JSON.parseObject(r);
                if (jsonObject!=null){
                    mid = jsonObject.containsKey("messageId")?jsonObject.getString("messageId"):null;
                    if (messageId.equals(mid)){
                        msg = r;
                        break;
                    }
                }
            }
        }
        if (msg!=null){
            String key = domain + "_" + roomId + "_HISORYMESSAGE";
            RedisManager.lrem(key.hashCode() + "",1,msg);
        }
    }
}
