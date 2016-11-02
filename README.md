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


### API和WEB服务端环境配置
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



### 菜单配置
````
<?xml version="1.0" encoding="UTF-8"?>
<menubar>

    <!-- ================================================================================ -->
    <!-- 								菜单配置										 
   		 用法:
   		 1. 需要菜单的Activity继承MenubarFragmentActivity，不许要setContentView();
   		 2. 调用initMenubar( R文件class , R.raw.menubar);
   		 配置说明:
   	   item-background(可选) 菜单tab背景图片，目前支持drawable和color,暂不支持#ffffff 样式
   	   			可以时xml的drawable，暂不支持@color，不设置无背景样式
   	   <items> 页面列表，子项只能有<item>
   	   <item>
   	       		属性：id: 功能暂留  
   	      <label> 菜单上的文本 ， 暂只支持纯文本。
   	      <label-color> 菜单上的文本颜色 ，只支持 @color/
   	      <icon> 图标
   	      <page>  target：打开方式  (必须)
   	              有4种 1:fragment ， 嵌入式页面 ， 普通fragment，指定fragment包路径
   	                   2:fragment_web , 嵌入式页面 ， 标准web的fragment
   	                   3:activity , 跳转activity ， 普通activity （不需要提供值）
   	                   4:activity_web , 跳转activity ， 跳转到web的activity （不需要提供值）
     -->
    <!-- ================================================================================ -->

    <!-- 条目背景，支持颜色和drawable 暂不支持 -->
    <!--
    <item-background>@drawable/xmlbg_menubar_tab</item-background>
     -->
    <!--<item-background>@color/menu_tab_bg</item-background>-->

    <items>

        <item id="1">
            <label color="@color/color_menu_label">菜单1</label>
            <icon drawable="@drawable/xmlbtn_menu_tab" />
            <page target="fragment" title="首页">
                <action>com.honestwalker.android.modules.commons.fragments.HomeFragement</action>
            </page>
        </item>

        <item id="2">
            <label color="@color/color_menu_label">菜单2</label>
            <icon drawable="@drawable/xmlbtn_menu_tab" />
            <page target="fragment" title="菜单2标题">
                <action>com.honestwalker.android.modules.demo.fragments.ListDemoFragement</action>
            </page>
        </item>

        <item id="3">
            <label color="@color/color_menu_label">Web菜单</label>
            <icon drawable="@drawable/xmlbtn_menu_tab" />
            <page target="fragment_web" title="Web 菜单页">
                <action>http://www.baidu.com</action>
            </page>
        </item>

    </items>

</menubar>
````


### 初始化策略配置与注入
- 支持指定进程启动初始化。
- 支持非UI线程初始化。

初始化业务会有一个Action对象，继承 BaseInitAction，并实现init()方法。
init()方法就是注入的初始化逻辑。
初始化注入配置如下
````Gradle
<strategies>
    
    <!-- ======================================================================= -->
    <!--                                                             			 -->
	<!--参数说明: 																 -->
	<!--    																     -->
	<!-- <strategy> 可选属性 process , 指定进程才会执行该组初始化操作-->    
	<!--    	固定参数 main 只有在主进程才会执行     				         -->
	<!--    	不加process属性，住进成也会执行，但子进程也会执行          -->
	<!-- <action>													       -->
	<!--  name: 初始化策略实现对象                             			 -->    
	<!--  async: (可选)执行初始化过程中，是否异步执行，默认为false	 -->    
    <!--                                                             			  -->
    <!--其他说明:                                                   			  -->
    <!--    初始化过程中，将按照actions中的顺序依次执行        			  -->
    <!--                                                             			  -->
    <!-- ======================================================================== -->

    <strategy process="main">
        <actions>
            <action name="com.honestwalker.android.init.ImageCacheInitAction" />
            <action name="com.honestwalker.android.modules.commons.init.PrestartProcessAction" />
        </actions>
    </strategy>

    <strategy>
        <actions>
            <action name="com.honestwalker.android.init.GlobalInitAction" />
            <action name="com.honestwalker.android.modules.commons.init.APIInitAction" />
        </actions>
    </strategy>
    
</strategies>
````











