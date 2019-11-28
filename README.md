# config-center 配置中心后台

## 主要功能
- 分布式配置文件管理
- 热更替
- 版本管理
- 差异对比

## 主要技术
前后端分离 Restful Web API

### Java 后台
- 框架，Sprinboot 1.5.x
- MySQL集群，分布式主从读写分离
- Zookeeper， 配置服务发现管理
- redis， 文件缓存，共享session
- 持久层，Spring JPA， Mybiats

### 前端
  - 框架，VueJS
  - Element，UI
  - Vuex,状态管理

### 中间件
  - MySQL --> MyCat
  - Nginx, 反向代理，负载均衡
  - Haproxy + keepAlive, 高性能高可用
  

 
  
