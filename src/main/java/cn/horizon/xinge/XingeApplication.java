package cn.horizon.xinge;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.horizon.xinge.repository")
public class XingeApplication {

    public static void main(String[] args) {
        SpringApplication.run(XingeApplication.class, args);
    }

}
