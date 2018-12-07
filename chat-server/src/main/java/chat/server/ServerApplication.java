package chat.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @auther a-de
 * @date 2018/11/4 22:07
 */
@SpringBootApplication
@ComponentScan(basePackages = "chat.core,chat.server")
@ImportResource({"classpath*:mybatis/applicationContext-*.xml"})
public class ServerApplication {
    public static void main(String[] args){
        SpringApplication.run(ServerApplication.class);
    }
}
