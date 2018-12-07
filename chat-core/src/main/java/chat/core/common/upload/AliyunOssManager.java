package chat.core.common.upload;

import chat.core.common.domain.ManagerException;
import chat.core.common.utility.CommonPropertiesUtility;
import chat.core.common.utility.DateUtils;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.auth.DefaultCredentialProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.Date;
import java.util.Random;

/**
 * @auther a-de
 * @date 2018/11/6 16:57
 */
public class AliyunOssManager {
    private static Logger logger = LoggerFactory.getLogger(AliyunOssManager.class);

    public static String uploadTempImg(InputStream stream, String suffix) {
        try {
            String accessKeyId = CommonPropertiesUtility.getProperty("accessKeyId");
            String accessKeySecret = CommonPropertiesUtility.getProperty("accessKeySecret");
            String endPoint = CommonPropertiesUtility.getProperty("endPoint");
            String bucketName = CommonPropertiesUtility.getProperty("bucketName");
            String domainStr = CommonPropertiesUtility.getProperty("domainStr");
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endPoint, new DefaultCredentialProvider(accessKeyId, accessKeySecret), null);
            String key = "t_" + uploadKey() + suffix;
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, key, stream);
            ossClient.shutdown();
            return domainStr + "/" + key;
        } catch (Exception e) {
            logger.error("阿里云上传图片异常",e);
            throw new ManagerException(e);
        }
    }

    public static String uploadForeverImg(InputStream stream, String suffix) {
        try {
            String accessKeyId = CommonPropertiesUtility.getProperty("accessKeyId");
            String accessKeySecret = CommonPropertiesUtility.getProperty("accessKeySecret");
            String endPoint = CommonPropertiesUtility.getProperty("endPoint");
            String bucketName = CommonPropertiesUtility.getProperty("bucketName");
            String domainStr = CommonPropertiesUtility.getProperty("domainStr");
            // 创建OSSClient实例。
            OSSClient ossClient = new OSSClient(endPoint, new DefaultCredentialProvider(accessKeyId, accessKeySecret), null);
            String key = "f_" + uploadKey() + suffix;
            // 上传内容到指定的存储空间（bucketName）并保存为指定的文件名称（objectName）。
            ossClient.putObject(bucketName, key, stream);
            ossClient.shutdown();
            return domainStr + "/" + key;
        } catch (Exception e) {
            logger.error("阿里云上传图片异常",e);
            throw new ManagerException(e);
        }
    }

    private static String uploadKey() {
        String firstName = DateUtils.getStrFromDate(new Date(), "yyyyMMddHHmmss");
        Random random = new Random();
        int a = random.nextInt(10);
        int b = random.nextInt(10);
        int c = random.nextInt(10);
        int d = random.nextInt(10);
        return firstName + a + b + c + d;
    }
}
