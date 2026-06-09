package cn.xuele.tag;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 人群标签服务启动类
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/03 15:33
 */
@SpringBootApplication(scanBasePackages = "cn.xuele.tag")
@EnableDubbo
public class TagApplication {
    public static void main(String[] args) {
        SpringApplication.run(TagApplication.class);
    }
}
