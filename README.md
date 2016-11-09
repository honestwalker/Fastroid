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

- 项目根目录build.gradle 配置
````Gradle
allprojects {
    repositories {
        jcenter()
        maven { url "https://jitpack.io" }
    }
}
````




-主项目build.gradle中配置Fastroid的依赖。
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


