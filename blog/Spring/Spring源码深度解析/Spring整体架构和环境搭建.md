## Spring整体架构

![](/Users/q/Documents/Code/Github/MyGit/blog/Spring/Spring源码深度解析/NeatReader-1595674128956.png)

## 容器的基本实现

### 容器的基本用法

先来一个bean

```java
public class MyTestBean {
     private String testStr = "testStr";

     public String getTestStr() {
         return testStr;
     }

     public void setTestStr(String testStr) {
         this.testStr = testStr;
     }

}
```

只是一个POJO，下面是配置文件

~~~xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:util="http://www.springframework.org/schema/util"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/util https://www.springframework.org/schema/util/spring-util.xsd">

     <bean id="myTestBean" class="bean.MyTestBean"/>

</beans>

~~~

测试

~~~java
@SuppressWarnings("deprecation")
public class BeanFactoryTest {

     @Test
     public void testSimpleLoad(){
             BeanFactory    bf = new XmlBeanFactory(new ClassPathResource("beanFactoryTest.xml"));
             MyTestBean bean=(MyTestBean) bf.getBean("myTestBean");
             assertEquals("testStr",bean.getTestStr());
     }
}
~~~

> 目前为止(2020/7/25 下午7:22:20)，XmlBeanFactory已经不再推荐使用。
>
> 可以用ClassPathXmlApplicationContext

~~~java
	public void testSimpleLoad(){
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("charpter02.xml");
		System.out.println(applicationContext.getBean("myTestBean"));
	}
~~~

梳理一下，Spring完成这些操作至少需要三个类，一个App用于串联所有逻辑，一个ConfigReader用于读取XML文件，还有一个ReflectionUtil用于将从XML读取出来的类进行反射实例化。

![](./NeatReader-1595676615320.png)

### Spring结构组成

跟着书本原本的代码进行解析。

### 配置文件封装

Spring配置文件读取通过ClassPathResource进行封装。

Spring对其内部使用到的资源实现了自己的抽象结构：Resource接口封装底层资源。

![image-20200725212316123](/Users/q/Documents/Code/Github/MyGit/blog/Spring/Spring源码深度解析/image-20200725212316123.png)

InputStreamSource封装任何能返回InputStream的类

~~~java
public interface InputStreamSource {

	InputStream getInputStream() throws IOException;

}
~~~

比如File、Classpath下的资源和Byte Array等。

Resource接口抽象了所有Spring内部使用到的底层资源：File、URL、Classpath等。

Resource接口提供了不同资源到URL、URI、File类型的转换，以及获取lastModified属性、文件名（不带路径信息的文件名，getFilename()）的方法。为了便于操作，Resource还提供了基于当前资源创建一个相对资源的方法：createRelative()。在错误处理中需要详细地打印出错的资源文件，因而Resource还提供了getDescription()方法用来在错误处理中打印信息。

对不同来源的资源文件都有相应的Resource实现：文件（FileSystemResource）、Classpath资源（ClassPathResource）、URL资源（UrlResource）、InputStream资源（InputStreamResource）、Byte数组（ByteArrayResource）等。

在日常的开发工作中，资源文件的加载也是经常用到的，可以直接使用Spring提供的类，比如在希望加载文件时可以使用以下代码：

```java
Resource resource=new ClassPathResource("beanFactoryTest.xml");
InputStream inputStream=resource.getInputStream();
```

有了Resource接口便可以对所有资源文件进行统一处理。至于实现，其实是非常简单的，以getInputStream为例，ClassPathResource中的实现方式便是通过class或者classLoader提供的底层方法进行调用，而对于FileSystemResource的实现其实更简单，直接使用FileInputStream对文件进行实例化。

~~~java
		InputStream is;
		if (this.clazz != null) {
			is = this.clazz.getResourceAsStream(this.path);
		}
		else if (this.classLoader != null) {
			is = this.classLoader.getResourceAsStream(this.path);
		}
~~~

接下来把视线转回XmlBeanFactory的构造，可以发现构造方法最终调用：

~~~java
public XmlBeanFactory(Resource resource, BeanFactory parentBeanFactory) throws BeansException {
		super(parentBeanFactory);
		this.reader.loadBeanDefinitions(resource);
	}
~~~

上面函数中的代码this.reader.loadBeanDefinitions(resource) 才是资源加载的真正实现。

但是在这之前，还有一个调用父类构造函数初始化的过程，跟踪下去，得到：

```java
public AbstractAutowireCapableBeanFactory() {
   super();
   ignoreDependencyInterface(BeanNameAware.class);
   ignoreDependencyInterface(BeanFactoryAware.class);
   ignoreDependencyInterface(BeanClassLoaderAware.class);
   if (IN_NATIVE_IMAGE) {
      this.instantiationStrategy = new SimpleInstantiationStrategy();
   }
   else {
      this.instantiationStrategy = new CglibSubclassingInstantiationStrategy();
   }
}
```

P10