package chat.web.controller;

import chat.core.common.domain.AppConfig;
import chat.core.db.model.AbstractBaseObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

/**
 * @auther a-de
 * @date 2018/11/6 16:51
 */
@Controller
public class VerifyCodeController extends AbstractBaseObject {
    @ResponseBody
    @RequestMapping(value = "/sysLoginVerifyCode",method = RequestMethod.POST)
    public void loginVerifyCode(HttpServletResponse response, HttpSession session){
        int width = 70;
        int height = 37;
        Random random = new Random();
        //设置response头信息
        //禁止缓存
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        //生成缓冲区image类
        BufferedImage image = new BufferedImage(width, height, 1);
        //产生image类的Graphics用于绘制操作
        Graphics g = image.getGraphics();
        //Graphics类的样式
        g.setColor(this.getRandColor(200, 250));
        g.setFont(new Font("Times New Roman",0,28));
        g.fillRect(0, 0, width, height);
        //绘制干扰线
        for(int i=0;i<40;i++){
            g.setColor(this.getRandColor(130, 200));
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int x1 = random.nextInt(12);
            int y1 = random.nextInt(12);
            g.drawLine(x, y, x + x1, y + y1);
        }

        //绘制字符
        String strCode = "";
        String[] codes = MakeCode(4);
        for(int i=0;i<codes.length;i++){
            String rand = codes[i];
            strCode = strCode + rand;
            g.setColor(new Color(20+random.nextInt(110),20+random.nextInt(110),20+random.nextInt(110)));
            g.drawString(rand, 13*i+6, 28);
        }
        //将字符保存到session中用于前端的验证
        session.setAttribute(AppConfig.LoginVerifyCode, strCode);

        g.dispose();

        try {
            ImageIO.write(image, "JPEG", response.getOutputStream());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String[] MakeCode(int codeLen){
        String allChar = "1,2,3,4,5,6,7,8,9,A,B,C,D,E,a,b,c,d,e,f,g,h,i,g,k,l,m,n,p,q,r,F,G,H,I,G,K,L,M,N,P,Q,R,S,T,U,V,W,X,Y,Z,s,t,u,v,w,x,y,z";
        String[] allCharArray = allChar.split(",");
        String[] randomCode = new String[codeLen];
        int temp = -1;
        Random rand = new Random();
        for (int i = 0; i < codeLen; i++)
        {
            if (temp != -1)
            {
                rand = new Random(i * temp * (System.currentTimeMillis()));
            }
            int t = rand.nextInt(35);
            if (temp == t)
            {
                return MakeCode(codeLen);
            }
            temp = t;
            randomCode[i] = allCharArray[t];
        }
        return randomCode;
    }

    //创建颜色
    private Color getRandColor(int fc, int bc){
        Random random = new Random();
        if(fc>255)
            fc = 255;
        if(bc>255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r,g,b);
    }
}
