# 信格聊天后台接口

### websocket端接口
> 端口号：5678

#### 连接到服务器
```json
{
    "action": "CONNECT",
    "data": {
        "senderId" : "1000000000"
    }
}
```
#### 发送消息
```json
{
    "action": "CHAT",
    "data": {
        "senderId" : "1000000000",
        "receiverId": "1000000001",
        "msg": "你好"
    }
}
```
### HTTP端接口

#### 用户登陆
```bash
curl --request POST -sL \
     --url 'http://localhost:1234/user/login'
```

#### 用户注册

#### 获取用户信息

#### 获取当前登陆用户信息

### 开发中。。。