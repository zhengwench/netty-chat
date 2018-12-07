package chat.server.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * @auther a-de
 * @date 2018/11/10 15:55
 */
@Configuration
public class BeanConfig {
    @Bean
    public ThreadPoolTaskExecutor threadPoolTaskExecutor(){
        ThreadPoolTaskExecutor threadPoolExecutor=new ThreadPoolTaskExecutor();
        threadPoolExecutor.setCorePoolSize(100);
        threadPoolExecutor.setMaxPoolSize(200);
        threadPoolExecutor.setQueueCapacity(3000);
        threadPoolExecutor.setKeepAliveSeconds(300);
        return threadPoolExecutor;
    }
}
