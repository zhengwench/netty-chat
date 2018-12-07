package chat.core.common.utility;

import java.io.IOException;
import java.util.Properties;

/**
 * @auther a-de
 * @date 2018/11/6 16:58
 */
public class CommonPropertiesUtility {
    private static String configFilePath = "common.properties";
    private static Properties props = new Properties();
    static {
        try {
            props.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(configFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String property){
        return props.getProperty(property);
    }
}
