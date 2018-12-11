package chat.core.common.utility;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

/**
 * @auther a-de
 * @date 2018/11/6 16:59
 */
public class Md5Manager {
    private static Logger logger = LoggerFactory.getLogger(Md5Manager.class);

    public static String md5(String text,String salt){
        //加密后的字符串
        try {
            String newstr = DigestUtils.md5Hex(text.getBytes("utf-8"));
            String r = newstr + salt;
            String lr = DigestUtils.md5Hex(r.getBytes("utf-8"));
            return lr.toLowerCase();
        } catch (Exception e) {
            logger.error("MD5异常",e);
        }
        return null;
    }

    public static String md5(String text){
        //加密后的字符串
        try {
            String r = DigestUtils.md5Hex(text.getBytes("utf-8"));
            return r;
        } catch (Exception e) {
            logger.error("MD5异常",e);
        }
        return null;
    }
}
