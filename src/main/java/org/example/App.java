package org.example;

import org.example.config.SwaggerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName App
 * @Description App
 * @Date 2020/4/14 10:47
 * @Author wangYong
 * @Version 1.0
 **/
@RestController
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
@Import({
        SwaggerConfig.class,  // 引入 Swagger2 接口文档依赖
})
public class App {


    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
