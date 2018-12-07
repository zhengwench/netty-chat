package chat.console;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

/**
 * @auther a-de
 * @date 2018/11/11 14:39
 */
@SpringBootApplication
@ComponentScan(basePackages = "chat.core,chat.console")
@ImportResource({"classpath*:mybatis/applicationContext-*.xml"})
public class ConsoleApplication {
    public static void main(String[] args){
        SpringApplication.run(ConsoleApplication.class);
    }
}
