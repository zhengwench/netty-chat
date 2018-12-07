package chat.web.controller;

import chat.core.common.domain.ResultDo;
import chat.core.common.upload.AliyunOssManager;
import chat.web.auth.SysAuth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @auther a-de
 * @date 2018/11/6 16:52
 */
@Controller
public class UploadController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(UploadController.class);
    @Autowired
    private MultipartResolver multipartResolver;

    @SysAuth(rule = "")
    @ResponseBody
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ResultDo upload(HttpServletRequest req, HttpServletResponse resp){
        ResultDo resultDo = new ResultDo();
        MultipartHttpServletRequest multipartRequest = null;
        try {
            if(multipartResolver.isMultipart(req)){
                multipartRequest = (MultipartHttpServletRequest) req;
                Map<String,MultipartFile> fileMap = multipartRequest.getFileMap();
                for(Map.Entry<String,MultipartFile> en:fileMap.entrySet()){
                    MultipartFile multipartFile = en.getValue();
                    //后缀
                    String fileName = multipartFile.getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf("."), fileName.length());
                    String result = AliyunOssManager.uploadForeverImg(multipartFile.getInputStream(),suffix);
                    resultDo.setResult(result);
                    return resultDo;
                }
            }else{
                logger.error("无效的上传请求。");
                resultDo.setErrorDesc("无效的上传请求。");
            }
        } catch (IOException e) {
            logger.error("上传文件出现异常。",e);
            resultDo.setErrorDesc("上传文件出现异常。");
        }finally{
            if(multipartRequest != null){
                multipartResolver.cleanupMultipart(multipartRequest);
            }
        }
        return resultDo;
    }
}
