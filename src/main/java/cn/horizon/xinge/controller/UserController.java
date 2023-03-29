package cn.horizon.xinge.controller;

import cn.horizon.xinge.annotation.NoTokenApi;
import cn.horizon.xinge.common.api.Result;
import cn.horizon.xinge.domain.vo.UserVo;
import cn.horizon.xinge.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author horizon
 * @create 2023/3/24 20:59
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @NoTokenApi
    @PostMapping("/login")
    public Result<Object> login(@RequestBody @Validated UserVo user) {
        return userService.login(user);
    }

    @NoTokenApi
    @PostMapping("/register")
    public Result<Object> register(@RequestBody @Validated UserVo userVo){
        return userService.register(userVo);
    }

    @GetMapping("/info/search")
    public Result<Object> userInfo(@RequestParam("search") String filterStr) {
        return userService.userInfo(filterStr);
    }

    @GetMapping("/info")
    public Result<Object> user(HttpServletRequest request) {
        return userService.userInfo(request);
    }



}
