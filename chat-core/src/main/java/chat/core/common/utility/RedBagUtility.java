package chat.core.common.utility;

import chat.core.common.redis.RedisManager;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @auther a-de
 * @date 2018/11/9 19:26
 */
public class RedBagUtility {
    /**
     * 生成红包
     * @param domain
     * @param redbagId
     * @param amount
     * @param count
     */
    public static void createRedBag(String domain, Long redbagId, BigDecimal amount, Integer count) {
        List<BigDecimal> redbagList = splitRedBag(amount, count);
        String key = domain + "_" + redbagId;
        int expirs = 24 * 60 * 60;
        for (BigDecimal r : redbagList) {
            RedisManager.lpush(key, r.toString(), expirs);
        }
    }

    /**
     * 领取红包
     * @param domain
     * @param redbagId
     * @return
     */
    public static BigDecimal receiveRedBag(String domain,Long redbagId) {
        String key = domain + "_" + redbagId;
        if (!RedisManager.exists(key)){
            return null;
        }
        if (RedisManager.llen(key) > 0) {
            String value = RedisManager.blpop(key);
            if (!StringUtils.isEmpty(value)){
                return new BigDecimal(value);
            }
        }
        return null;
    }

    public static void main(String[] args){
        List<BigDecimal> list = splitRedBag(new BigDecimal(1),100);
        for (BigDecimal b:list) {
            System.out.print("红包金额：" + b);
        }
    }

    /**
     * 生成红包
     *
     * @param amount
     * @param count
     * @return
     */
    private static List<BigDecimal> splitRedBag(BigDecimal amount, int count) {
        List<BigDecimal> redBagList = new ArrayList<>();
        BigDecimal percent;
        List<BigDecimal> percentList = new ArrayList<>();
        BigDecimal percentTotal = BigDecimal.ZERO;

        if (count>1){
            for (int i = 0; i < count; i++) {
                //每个红包百分比
                percent = new BigDecimal(RandomUtils.getRandom(10));
                percentList.add(percent);
                percentTotal = percentTotal.add(percent);
            }
            BigDecimal ave = amount.divide(percentTotal,2,BigDecimal.ROUND_DOWN);
            BigDecimal res;
            BigDecimal show;
            for (int i = 0; i < percentList.size() - 1; i++) {
                res = ave.multiply(percentList.get(i));
                show = res.setScale(2, BigDecimal.ROUND_DOWN);
                if (show.compareTo(res) > 0) {
                    show = show.subtract(new BigDecimal(0.01));
                }
                amount = amount.subtract(show);
                redBagList.add(show);
            }
            redBagList.add(amount);
        }else {
            redBagList.add(amount);
        }
        return redBagList;
    }
}
