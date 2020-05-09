microservice-bookmark: 书签服务
microservice-comment : 点评服务
microservice-metadata：原数据服务
microservice-reminder：提醒服务
microservice-search  ：搜索服务
microservice-favorite：收藏服务
microservice-sync-data:同步数据工具服务
microservicem        ： 微服务管理平台，规划所有微服的数据可视化呈现，业务配置下沉管理（替换sping cloud config方式）
microservice-testclient：测试微服务
zmicroservice-hystrix-dashboard：监控服务
microservice-turbine： 集群监控服务
microservice-zuul：   ApiGetWay 路由服务，认证，安全检验，流量管理等
config-server:全局配置服务，spring  cloud 原生配置管理，也可使用microservicem，配置沉入db获取提供接口来管理获取配置
microservice-discovery-eureka：服务发现组件


目前具备： 动态路由，集群监控，负载均衡，流量管理，断路器与熔断，配置自动刷新，日志数据采集，高可用
