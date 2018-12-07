package chat.web.controller;

import chat.core.common.domain.ResultDo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @auther a-de
 * @date 2018/11/6 16:53
 */
@Controller
public class ExceptionController  {
    @ResponseBody
    @RequestMapping(value = "/ajaxLogin")
    public ResultDo nologin(){
        ResultDo resultDo = new ResultDo();
        resultDo.setErrorDesc("未登录");
        return resultDo;
    }

    @RequestMapping(value = "/error")
    public String error(){
        return "/sys/500";
    }

    @ResponseBody
    @RequestMapping(value = "/ajaxError")
    public ResultDo ajaxError(){
        ResultDo resultDo = new ResultDo();
        resultDo.setErrorDesc("程序运行异常");
        return resultDo;
    }
}
