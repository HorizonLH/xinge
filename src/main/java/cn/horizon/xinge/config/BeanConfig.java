package cn.horizon.xinge.config;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.nio.NioEventLoopGroup;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @author horizon
 * @create 2023/3/28 20:44
 **/
@Configuration
public class BeanConfig {

    @Bean
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(1);
    }

    @Bean
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup();
    }

    /**
     * 服务器启动引导类
     * @return ServerBootstrap
     */
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public ServerBootstrap serverBootstrap() {
        return new ServerBootstrap();
    }

}
