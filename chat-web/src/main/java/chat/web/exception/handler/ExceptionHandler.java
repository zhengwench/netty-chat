package chat.web.exception.handler;

import chat.core.common.domain.ManagerException;
import chat.web.exception.SysAuthException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther a-de
 * @date 2018/11/6 14:00
 */
public class ExceptionHandler implements HandlerExceptionResolver {
    private static Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);
    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        String xRequest = httpServletRequest.getHeader("X-Requested-With");
        ModelAndView modelAndView = null;
        if (xRequest != null) {
            modelAndView = new ModelAndView(new MappingJackson2JsonView());
        }
        if (e instanceof SysAuthException) {
            if (xRequest != null) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("errorDesc", e.getMessage());
                return modelAndView;
            }
            return new ModelAndView("redirect:/sys/login");
        }
        if (e instanceof ManagerException) {
            if (xRequest != null) {
                modelAndView.addObject("success", false);
                modelAndView.addObject("errorDesc", e.getMessage());
                return modelAndView;
            }
        }
        logger.error("全局捕获异常", e);
        if (xRequest != null) {
            modelAndView.addObject("success", false);
            modelAndView.addObject("errorDesc", e.getMessage());
            return modelAndView;
        }
        modelAndView = new ModelAndView();
        modelAndView.setViewName("/sys/404");
        return modelAndView;
    }
}
