rabbitmq学习
==
## 常用操作
 - 启动MQ：service rabbitmq-server start
 - 重启MQ：service rabbitmq-server restart
 - 关闭MQ：service rabbitmq-server stop
 - 查看状态：rabbitmqctl status
 - 用户管理：
  - 新增一个用户 rabbitmqctl add_user Username Password   
  - 设置admin rabbitmqctl set_user_tags Username administrator  
    用户角色大致可分为五类，超级管理员, 监控者, 策略制定者, 普通管理者以及其他。
	1. 超级管理员(administrator)：   
	可登陆管理控制台(启用management plugin的情况下)，可查看所有的信息，并且可以对用户，策略(policy)进行操作。  
	2. 监控者(monitoring)  
	可登陆管理控制台(启用management plugin的情况下)，同时可以查看rabbitmq节点的相关信息(进程数，内存使用情况，磁盘使用情况等)   
	3. 策略制定者(policymaker)  
	可登陆管理控制台(启用management plugin的情况下), 同时可以对policy进行管理。但无法查看节点的相关信息(上图红框标识的部分)。与administrator的对比，administrator能看到这些内容  
	4. 普通管理者(management)
	仅可登陆管理控制台(启用management plugin的情况下)，无法看到节点信息，也无法对策略进行管理。
	5. 其他无法登陆管理控制台，通常就是普通的生产者和消费者。
  - 删除一个用户 rabbitmqctl delete_user Username
  - 修改用户的密码 rabbitmqctl change_password Username Newpassword
  - 查看当前用户列表 rabbitmqctl list_users

----------

## 六种工作模式

 1. 简单队列

    ![简单队列](https://img-blog.csdnimg.cn/20181221114009759.png)

    ​    1. 消息产生者将消息放入队列

    ​    2. 消息的消费者(consumer) 监听(while) 消息队列,如果队列中有消息,就消费掉,消息被拿走后,自动从队列中删除(隐患 消息可能没有被消费者正确处理,已经从队列中消失了,造成消息的丢失)应用场景:聊天(中间有一个过度的服务器;p端,c端)

2. work工作模式（资源的竞争）

   ![work工作模式](https://img-blog.csdnimg.cn/20181221114036231.png)

   1. 消息产生者将消息放入队列消费者可以有多个,消费者1,消费者2,同时监听同一个队列,消息被消费?C1 C2共同争抢当前的消息队列内容,谁先拿到谁负责消费消息(隐患,高并发情况下,默认会产生某一个消息被多个消费者共同使用,可以设置一个开关(syncronize,与同步锁的性能不一样) 保证一条消息只能被一个消费者使用)
   2. 应用场景:红包;大项目中的资源调度(任务分配系统不需知道哪一个任务执行系统在空闲,直接将任务扔到消息队列中,空闲的系统自动争抢)

   > 模式1：自动确认
   > 只要消息从队列中获取，无论消费者获取到消息后是否成功消息，都认为是消息已经成功消费。
   > 模式2：手动确认
   > 消费者从队列中获取消息后，服务器会将该消息标记为不可用状态，等待消费者的反馈，如果消费者一直没有反馈，那么该消息将一直处于不可用状态。

3. publish/subscribe发布订阅

   ![发布订阅](https://img-blog.csdnimg.cn/20181221114050657.png)

   1. X代表交换机rabbitMQ内部组件,erlang 消息产生者是代码完成,代码的执行效率不高,消息产生者将消息放入交换机,交换机发布订阅把消息发送到所有消息队列中,对应消息队列的消费者拿到消息进行消费
   2. 相关场景:邮件群发,群聊天,广播(广告)

4. routing路由模式

   ![路由模式](https://img-blog.csdnimg.cn/20181221114420299.png)

   1. 消息生产者将消息发送给交换机按照路由判断,路由是字符串(info) 当前产生的消息携带路由字符(对象的方法),交换机根据路由的key,只能匹配上路由key对应的消息队列,对应的消费者才能消费消息;
   2. 根据业务功能定义路由字符串
   3. 从系统的代码逻辑中获取对应的功能字符串,将消息任务扔到对应的队列中业务场景:error 通知;EXCEPTION;错误通知的功能;传统意义的错误通知;客户通知;利用key路由,可以将程序中的错误封装成消息传入到消息队列中,开发者可以自定义消费者,实时接收错误;

5. topic 主题模式(路由模式的一种)

   ![主题模式](https://img-blog.csdnimg.cn/20181221114208408.png)

   1. 星号井号代表通配符
   2. 星号代表多个单词,井号代表一个单词
   3. 路由功能添加模糊匹配
   4. 消息产生者产生消息,把消息交给交换机
   5. 交换机根据key的规则模糊匹配到对应的队列,由队列的监听消费者接收消息消费

6. RPC模式

   暂无。





------------




