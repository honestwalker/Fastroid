# Fastroid

![Fastroid](http://www.kancart.com/images/kancart_logo.png)

Fastroid是有上海信行软件Android团队开发的一组Android开发套件。简化了一些Android开发常用业务的编码和系统API的调用，集合了一些常用的工具类和UI控件，简化日常编码，用过依赖注入把App一些常规的开发场景和应用耦合，如首页菜单页面可配置化与解耦，初始化业务逻辑解耦，Web与Native交互业务解耦等等。
同时也集成了一些常用的第三方框架，如xUtils、ShareSDK、ImageLoader等等。

## Features

- 常用UI套件，如菜单框架（通过配置快速实现菜单）、标题框架（相对调用系统标题，只需要一行代码）、Loading、对话框调用简化、自定义对话框简化
  Adapter写法简化等等。
- 基于Kancart API模型的API框架
- Web与Native交换套件，与交换业务依赖注入框架。
- 通用程序初始化功能模块，初始化业务耦合。
- 一些的工具类。
- 更多....

## 导入

build.gradle中配置Fastroid的依赖。
````Gradle
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
      compile 'com.github.honestwalker:Fastroid:0.1.0'
    }
}
````


## API和WEB服务端环境配置
在raw目录下新建文件 server.xml ， 并输入以下配置。
````
<server>

    <!-- =================================================================================== -->
    <!-- contexts 包含了各个服务端的配置                                                         -->
    <!-- context 服务端环境配置           													     -->
    <!--    id 环境id                    													 -->
    <!--    extends 复用环境配置，如果当前context又配置了同名字段则覆盖父类字段 					     -->
    <!--    method 请求方式 post get put delete等选项，默认post 								 -->
    <!--    action-key 如果服务端api需要一个参数来标记是什么api请求动作，这个参数就有actionKey配置      -->
    <!--    host 服务端api url , 如果服务端每个api地址不一样就有req对象的注解配置uri                  -->
    <!--    context 节点下 id 和 extends 属性是关键字 ， 它的子节点自由配置，并对应一个对象            -->
    <!--               传给 ServerLoader.getServerConfig 进行解析                              -->
    <!-- =================================================================================== -->

    <scheme>
        <context-id>dev</context-id>
    </scheme>

    <contexts>

        <context id="default_context">
            <method>post</method>
            <action_key>method</action_key>
            <app_key>11111111</app_key>
            <app_secret>11111111111111111111111111111111</app_secret>
        </context>
        
        <context id="test_context" extends="default_context">
        </context>
        
        <context id="dev" extends="test_context">
            <host>http://dev.xjshift.com/</host>
            <web_host>http://dev.xjshift.com/</web_host>
        </context>

        <context id="release" extends="test_context">
            <host>http://api.xxx.com</host>
            <web_host>http://m.xjshift.com/</web_host>
        </context>

    </contexts>

</server>

````
context可以有继承关系，但不要相互继承。



