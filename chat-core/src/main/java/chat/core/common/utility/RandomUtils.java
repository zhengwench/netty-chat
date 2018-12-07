package chat.core.common.utility;

import java.util.Random;

/**
 * @auther a-de
 * @date 2018/11/27 11:02
 */
public class RandomUtils {
    public static int getRandom(int l){
        int result = 0;
        Random random = new Random();
        while (result==0){
            result = random.nextInt(l);
        }
        return result;
    }
}
