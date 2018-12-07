package chat.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import chat.core.common.domain.ResultDo;
import chat.core.common.redis.RedisManager;
import chat.core.common.utility.DateUtils;
import chat.core.common.utility.Md5Manager;
import chat.core.common.utility.RSAUtils;
import chat.core.common.utility.RandomUtils;
import chat.core.db.manager.RoomManager;
import chat.core.db.manager.UserManager;
import chat.core.db.model.DomainConfig;
import chat.core.db.model.Room;
import chat.core.db.model.User;
import chat.web.auth.SysAuthUser;
import chat.web.http.HttpRequestClient;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @auther a-de
 * @date 2018/11/17 19:21
 */
@Controller
@RequestMapping(value = "/api")
public class ApiController extends BaseController {
    private Logger logger = LoggerFactory.getLogger(ApiController.class);
    @Autowired
    private UserManager userManager;

    @Autowired
    private RoomManager roomManager;

    @Autowired
    private HttpRequestClient httpRequestClient;

    @ResponseBody
    @RequestMapping(value = "/sign", method = RequestMethod.POST)
    public ResultDo sign(String sign) {
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();
        if (StringUtils.isEmpty(sign)){
            resultDo.setErrorDesc("签名数据为空");
            return resultDo;
        }
        String decryStr = RSAUtils.decryptStrByPrivateKey(sign, domainConfig.getPrivateKey());
        if (StringUtils.isEmpty(decryStr)) {
            resultDo.setErrorDesc("签名数据为空");
            return resultDo;
        }
        JSONObject jsonObject = JSONObject.parseObject(decryStr);
        String username = jsonObject.get("username") + "";
        String signtimestr = jsonObject.get("signtime") + "";
        Date signtime = DateUtils.getDateFromString(signtimestr, "yyyyMMddHHmmss");
        logger.info("解密username:"+username);
        logger.info("解密signtimestr:"+signtimestr);
        if (signtime.before(DateUtils.addMinute(new Date(), -30)) || signtime.after(DateUtils.addMinute(new Date(), 30))) {
            resultDo.setErrorDesc("请求过期");
            return resultDo;
        }
        User user = userManager.queryByUserName(username, domainConfig.getId());
        if (user == null) {
            user = new User();
            user.setDomainId(domainConfig.getId());
            user.setUserName(username);
            int icon = RandomUtils.getRandom(25);
            user.setIcon("userIcon" + icon);
            Room room = roomManager.queryDefaultRoom(domainConfig.getId());
            if (room == null) {
                resultDo.setErrorDesc("服务数据有误");
                return resultDo;
            }
            user.setRoomId(room.getId());
            user.setStatus(1);
            user.setRemark("api接口注册");
            user.setToken(UUID.randomUUID().toString().replace("-", ""));
            user.setSalt(UUID.randomUUID().toString().replace("-", ""));
            user.setDelFlag(1);
            if (!userManager.insert(user)) {
                resultDo.setErrorDesc("注册失败");
                return resultDo;
            }
        }
        Map map = new HashMap();
        map.put("userId", user.getId());
        map.put("token", user.getToken());
        map.put("userName", user.getUserName());
        map.put("userIcon", user.getIcon());
        map.put("roomId", user.getRoomId());
        resultDo.setResult(map);
        return resultDo;
    }

//    @ResponseBody
//    @RequestMapping(value = "/sign")
//    public ResultDo sign(String msg){
//        ResultDo resultDo = new ResultDo();
//        String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1HEKPmO9zZIIiLzhCBbk/2eROWkrWl5m5ufEOtdYmj9afph3NkFSzADofxCzID63c1fm4pEDI5ArUAYmzfB+MreWGfe6gt/philGDITNIU/KmPSd+DGrfNWHAi/laFTHZrPOB2dk6aGLLv2kbpbrj3B9kGY8ILJz83nOpavH4kwIDAQAB";
//        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALUcQo+Y73NkgiIvOEIFuT/Z5E5aStaXmbm58Q611iaP1p+mHc2QVLMAOh/ELMgPrdzV+bikQMjkCtQBibN8H4yt5YZ97qC3+mGKUYMhM0hT8qY9J34Mat81YcCL+VoVMdms84HZ2TpoYsu/aRuluuPcH2QZjwgsnPzec6lq8fiTAgMBAAECgYAExFqoJvFeiy21sqJ7cmUe3iBZQ50mqtTcA53psjWRzEVs7FVqfLsN/xqIjYRUO71qy6MIDiK6Zu42CFhUhhbadL5Pfwfdf2k+ACSL2OeJim1z3Nf+rwVbmB9EleyJ25lzGzhbmKY5HieIKvPRtOzZIafR7v7ciyvZ4JoEL1r7oQJBAN2KOh0HIErakVcRPI38nRu2za+7wtrmVBuWBB5hf4Cw5Nn/Wl97kHJLK0bfNq9FkYPDRuMlK8Yiqyfuqin8fxECQQDRSB5SdI6h2NBwnCPt+xePpVHBiEuXBWqtVOgH6rJXdpySVA01iEp696v5gVZh+FsGvmtzJFO2tO6ou8pduYVjAkBheEltMA9zH4rGhdzgk34EDX+di7yFVsU2heA2Bat1UV6+0uxOCi1ZO91EhzymDLOjBy2PDnGd8O7g2M6hPmghAkEAnJ+jicQPZtLTlCNWxGjxRiDMYdF1+M0l/BiaGiqsU8bCAMUEqbaZPxQSUYVZN7LdtNtXoPFj8U3Retgn6CLKzwJBAJDPWGvd/LjZ7pLFHJEplR2qY83lJZ8YZLgWJRuImAxqGj3zZcGd8rhpxSF2cT6ePYR6uJK9wq/L/IRaGIPWO6I=";
//        String encryptStr = RSAUtils.encryptStrByPublicKey(msg,publicKey);
//        String decryStr = RSAUtils.decryptStrByPrivateKey(encryptStr,privateKey);
//        Map map = new HashMap();
//        map.put("encryptStr",encryptStr);
//        map.put("decryStr",decryStr);
//        resultDo.setResult(map);
//        return resultDo;
//    }

    @ResponseBody
    @RequestMapping(value = "/sendMsg", method = RequestMethod.POST)
    public ResultDo sendMsg(String sign, String roomId, String msgType, String msg) {
        ResultDo resultDo = new ResultDo();
        DomainConfig domainConfig = getDomainConfig();

        String key = domainConfig.getDomainName() + "api/sendMsg";
        if (RedisManager.exists(key)) {
            resultDo.setErrorDesc("发送频繁");
            return resultDo;
        }
        RedisManager.setnx(key, "sendmsg");
        RedisManager.expire(key, 5);
        String url = domainConfig.getHttpUrl() + "/api/sendMsg";

        String decryStr = RSAUtils.decryptStrByPrivateKey(sign, domainConfig.getPrivateKey());
        if (StringUtils.isEmpty(decryStr)) {
            resultDo.setErrorDesc("签名数据为空");
            return resultDo;
        }
        JSONObject jsonObject = JSONObject.parseObject(decryStr);
        String username = jsonObject.get("username") + "";
        String password = jsonObject.get("password") + "";
        User user = userManager.queryByUserName(username, domainConfig.getId());
        if (user == null) {
            resultDo.setErrorDesc("账号或密码错误");
            return resultDo;
        }
        String p = Md5Manager.md5(password, user.getSalt());
        if (!user.getPassword().toLowerCase().equals(p.toLowerCase())) {
            resultDo.setErrorDesc("账号或密码错误");
            return resultDo;
        }

        String responseStr = bindMsg(user, msgType, msg);
        if (StringUtils.isEmpty(responseStr)) {
            resultDo.setErrorDesc("发送消息数据为空");
            return resultDo;
        }

        Map<String, Object> postMap = new HashMap<>();
        postMap.put("domain", domainConfig.getDomainName());
        SysAuthUser userInfo = getUserInfo();
        postMap.put("token", userInfo.getToken());
        postMap.put("roomId", roomId);
        postMap.put("msg", responseStr);
        httpRequestClient.doPost(url, postMap, null);
        String response = httpRequestClient.doPost(url, postMap, null);
        resultDo = JSON.parseObject(response, ResultDo.class);
        return resultDo;
    }

    private String bindMsg(User user, String msgType, String msg) {
        if ("S_MESSAGE".equals(msgType)) {
            JSONObject response = new JSONObject();
            response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            response.put("userId", user.getId());
            response.put("userName", user.getUserName());
            response.put("userIcon", user.getIcon());
            response.put("command", "S_MESSAGE");
            response.put("content", msg);
            return response.toJSONString();
        }
        if ("S_LOTTERY_GOODNEWS".equals(msgType)) {
            JSONObject response = new JSONObject();
            response.put("messageId", UUID.randomUUID().toString().replace("-", ""));
            response.put("userId", user.getId());
            response.put("userName", user.getUserName());
            response.put("userIcon", user.getIcon());
            response.put("command", "S_LOTTERY_GOODNEWS");
            response.put("content", msg);
            return response.toJSONString();
        }
        return null;
    }
}
