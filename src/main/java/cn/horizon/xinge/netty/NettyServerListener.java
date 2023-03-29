package cn.horizon.xinge.netty;


import cn.horizon.xinge.netty.handler.WebSocketChildChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.concurrent.Future;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @author horizon
 * @create 2022-06-20 20:40
 **/
@Component
@Order(1)
@ConfigurationProperties(prefix = "netty.websocket")
public class NettyServerListener implements CommandLineRunner {

    private final static Logger LOGGER = LoggerFactory.getLogger(NettyServerListener.class);

    private Integer port;

    /**
     * Netty服务启动器
     */
    @Qualifier("serverBootstrap")
    @Autowired
    ServerBootstrap serverBootstrap;

    /**
     * 接受客户端请求的boss循环组
     */
    @Qualifier("bossGroup")
    @Autowired
    private EventLoopGroup boss;

    /**
     * 接受发消息的工人循环组
     */
    @Qualifier("workerGroup")
    @Autowired
    private EventLoopGroup worker;

    /**
     * 启动或关闭服务
     */
    private ChannelFuture future;

    /**
     *
     */
    @Resource(name = "webSocketChildChannelHandler")
    private ChannelHandler channelHandler;

    /**
     * WebSocket Handler
     */
    @Autowired
    WebSocketChildChannelHandler websocketHandler;


    /**
     * 启动Netty服务
     */
    public void start() {
        long begin = System.currentTimeMillis();
        serverBootstrap.group(boss, worker) //boss辅助客户端的tcp连接请求  worker负责与客户端之前的读写操作
                .channel(NioServerSocketChannel.class) //配置客户端的Channel类型
                .option(ChannelOption.SO_BACKLOG, 1024) //TCP握手字符串长度
                //开启心跳包活机制，就是客户端、服务端建立连接处于ESTABLISHED状态，超过2小时没有交流，机制会被启动
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //配置固定长度接收缓存区分配器
                .childOption(ChannelOption.RCVBUF_ALLOCATOR, new FixedRecvByteBufAllocator(592048))
                //TCP_NODELAY算法，尽可能发送大块数据，减少充斥的小块数据
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childHandler(channelHandler) //绑定I/O事件的处理类,WebSocketChildChannelHandler中定义
                .handler(new LoggingHandler(LogLevel.INFO));

        serverBootstrap.childHandler(websocketHandler);
        long end = System.currentTimeMillis();
        LOGGER.info("Netty Websocket服务器启动完成，耗时{}ms，已绑定端口{}，阻塞式等候客户端连接", end - begin, port);
        try {
            future = serverBootstrap.bind(port).sync();
//            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            boss.shutdownGracefully();
            worker.shutdownGracefully();
            e.printStackTrace();
        }
    }

    /**
     * 关闭Netty服务器
     */
    public void close() {
        future.channel().close();
        Future<?> bossShutdown = boss.shutdownGracefully();
        Future<?> workerShutdown = worker.shutdownGracefully();

        try {
            bossShutdown.await();
            workerShutdown.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getPort() {
        return port;
    }

    public ChannelHandler getChannelHandler() {
        return channelHandler;
    }

    public void setChannelHandler(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }


    @Override
    public void run(String... args) {
        start();
    }
}
