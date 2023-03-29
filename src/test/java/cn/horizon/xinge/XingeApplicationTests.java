package cn.horizon.xinge;

import cn.horizon.xinge.service.UserService;
import cn.horizon.xinge.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class XingeApplicationTests {

    @Autowired
    private UserServiceImpl userService;

    @Test
    void contextLoads() throws Exception {

        System.out.println(userService.getUserInfo("username", "horizon"));

    }

}
