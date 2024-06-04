package cn.com.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;

/**
 * 启动程序
 *
 * @author Lion Li
 */

@SpringBootApplication
public class AiChatApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AiChatApplication.class);
        application.setApplicationStartup(new BufferingApplicationStartup(2048));
        application.run(args);
        System.out.println("(♥◠‿◠)ﾉﾞ  AI-CHAT启动成功   ლ(´ڡ`ლ)ﾞ");
    }

}
