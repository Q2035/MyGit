

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

> Ignore the given dependency interface for autowiring.
>
> This will typically be used by application contexts to register
> dependencies that are resolved in other ways, like BeanFactory through
> BeanFactoryAware or ApplicationContext through ApplicationContextAware.
> By default, only the BeanFactoryAware interface is ignored.
> For further types to ignore, invoke this method for each type.

上面这段话是ignoreDependencyInterface方法上的注释。

### 加载Bean

之前提到XmlBeanFactory构造函数中调用了XmlBeanDefinitionReader类型的reader属性提供的方法loadBeanDefinitions(resource)

```java
this.reader.loadBeanDefinitions(resource);
```

先看看这个方法的时序图：

![](./NeatReader-1595733484996.png)

从上面的时序图中我们尝试梳理整个的处理过程如下。

1．封装资源文件。当进入XmlBeanDefinitionReader后首先对参数Resource使用EncodedResource类进行封装。

2．获取输入流。从Resource中获取对应的InputStream并构造InputSource。

3．通过构造的InputSource实例和Resource实例继续调用函数doLoadBeanDefinitions。

查看loadBeanDefinitions函数具体的实现过程：

```Java
@Override
public int loadBeanDefinitions(Resource resource) throws BeanDefinitionStoreException {
   return loadBeanDefinitions(new EncodedResource(resource));
}
```

EncodedResource可以大致推断为对资源文件的编码进行处理，其主要逻辑体现在getReader()方法中：

```
public Reader getReader() throws IOException {
   if (this.charset != null) {
      return new InputStreamReader(this.resource.getInputStream(), this.charset);
   }
   else if (this.encoding != null) {
      return new InputStreamReader(this.resource.getInputStream(), this.encoding);
   }
   else {
      return new InputStreamReader(this.resource.getInputStream());
   }
}
```

上面代码构造了一个有编码（encoding）的InputStreamReader。当构造好encodedResource对象后，再次转入了可复用方法loadBeanDefinitions(new EncodedResource(resource))

```java
	public int loadBeanDefinitions(EncodedResource encodedResource) throws BeanDefinitionStoreException {
		Assert.notNull(encodedResource, "EncodedResource must not be null");
		if (logger.isTraceEnabled()) {
			logger.trace("Loading XML bean definitions from " + encodedResource);
		}

//		通过属性记录已经加载的资源
		Set<EncodedResource> currentResources = this.resourcesCurrentlyBeingLoaded.get();

		if (!currentResources.add(encodedResource)) {
			throw new BeanDefinitionStoreException(
					"Detected cyclic loading of " + encodedResource + " - check your import definitions!");
		}

//		从encodeResource获取已经封装的Resource对象并再次从Resource中获取其中的inputStream
		try (InputStream inputStream = encodedResource.getResource().getInputStream()) {
//			InputSource这个类并不来自于Spring，它的全路径是org.xml.sax.InputSource
			InputSource inputSource = new InputSource(inputStream);
			if (encodedResource.getEncoding() != null) {
				inputSource.setEncoding(encodedResource.getEncoding());
			}
//			真正进入逻辑核心部分
			return doLoadBeanDefinitions(inputSource, encodedResource.getResource());
		}
		catch (IOException ex) {
			throw new BeanDefinitionStoreException(
					"IOException parsing XML document from " + encodedResource.getResource(), ex);
		}
		finally {
			currentResources.remove(encodedResource);
			if (currentResources.isEmpty()) {
				this.resourcesCurrentlyBeingLoaded.remove();
			}
		}
	}
```

我们再次整理数据准备阶段的逻辑，首先对传入的resource参数做封装，目的是考虑到Resource可能存在编码要求的情况，其次，通过SAX读取XML文件的方式来准备InputSource对象，最后将准备的数据通过参数传入真正的核心处理部分doLoadBeanDefinitions(inputSource, encodedResource.getResource())。

```java
protected int doLoadBeanDefinitions(InputSource inputSource, Resource resource)
      throws BeanDefinitionStoreException {

   try {
      Document doc = doLoadDocument(inputSource, resource);
      int count = registerBeanDefinitions(doc, resource);
      if (logger.isDebugEnabled()) {
         logger.debug("Loaded " + count + " bean definitions from " + resource);
      }
      return count;
   }
   catch (BeanDefinitionStoreException ex) {
      throw ex;
   }
   catch (SAXParseException ex) {
      throw new XmlBeanDefinitionStoreException(resource.getDescription(),
            "Line " + ex.getLineNumber() + " in XML document from " + resource + " is invalid", ex);
   }
   catch (SAXException ex) {
      throw new XmlBeanDefinitionStoreException(resource.getDescription(),
            "XML document from " + resource + " is invalid", ex);
   }
   catch (ParserConfigurationException ex) {
      throw new BeanDefinitionStoreException(resource.getDescription(),
            "Parser configuration exception parsing XML from " + resource, ex);
   }
   catch (IOException ex) {
      throw new BeanDefinitionStoreException(resource.getDescription(),
            "IOException parsing XML document from " + resource, ex);
   }
   catch (Throwable ex) {
      throw new BeanDefinitionStoreException(resource.getDescription(),
            "Unexpected exception parsing XML document from " + resource, ex);
   }
}
```

上述代码其实只做了三件事，这三件事的每一件都必不可少。

- 获取对XML文件的验证模式。
- 加载XML文件，并得到对应的Document。
- 根据返回的Document注册Bean信息

### 获取XML的验证模式

XML文件的验证模式保证了XML文件的正确性，而比较常用的验证模式有两种：DTD和XSD。

DTD（Document Type Definition）即文档类型定义，是一种XML约束模式语言，是XML文件的验证机制，属于XML文件组成的一部分。DTD是一种保证XML文档格式正确的有效方法，可以通过比较XML文档和DTD文件来看文档是否符合规范，元素和标签使用是否正确。 一个DTD文档包含：元素的定义规则，元素间关系的定义规则，元素可使用的属性，可使用的实体或符号规则。

XML Schema语言就是XSD（XML Schemas Definition）。XML Schema描述了XML文档的结构。可以用一个指定的XML Schema来验证某个XML文档，以检查该XML文档是否符合其要求。文档设计者可以通过XML Schema指定XML文档所允许的结构和内容，并可据此检查XML文档是否是有效的。XML Schema本身是XML文档，它符合XML语法结构。可以用通用的XML解析器解析它。

#### 验证模式的读取

通过之前的分析我们锁定了Spring通过getValidationModeForResource方法来获取对应资源的的验证模式。(之前追到doLoadDocument，进入该方法，发现有一个参数调用了getValidationModeForResource)

~~~java
	protected int getValidationModeForResource(Resource resource) {
		int validationModeToUse = getValidationMode();
//		如果手动指定了验证模式则使用指定的验证模式
		if (validationModeToUse != VALIDATION_AUTO) {
			return validationModeToUse;
		}
//		没有的话使用自动检测
		int detectedMode = detectValidationMode(resource);
		if (detectedMode != VALIDATION_AUTO) {
			return detectedMode;
		}
		// Hmm, we didn't get a clear indication... Let's assume XSD,
		// since apparently no DTD declaration has been found up until
		// detection stopped (before finding the document's root tag).
		return VALIDATION_XSD;
	}
~~~

自动检测验证模式的功能是在函数detectValidationMode方法中实现的，在detectValidationMode函数中又将自动检测验证模式的工作委托给了专门处理类XmlValidationMode- Detector，调用了XmlValidationModeDetector的validationModeDetector方法，具体代码如下：

```java
protected int detectValidationMode(Resource resource) {
   if (resource.isOpen()) {
      throw new BeanDefinitionStoreException(
            "Passed-in Resource [" + resource + "] contains an open stream: " +
            "cannot determine validation mode automatically. Either pass in a Resource " +
            "that is able to create fresh streams, or explicitly specify the validationMode " +
            "on your XmlBeanDefinitionReader instance.");
   }

   InputStream inputStream;
   try {
      inputStream = resource.getInputStream();
   }
   catch (IOException ex) {
      throw new BeanDefinitionStoreException(
            "Unable to determine validation mode for [" + resource + "]: cannot open InputStream. " +
            "Did you attempt to load directly from a SAX InputSource without specifying the " +
            "validationMode on your XmlBeanDefinitionReader instance?", ex);
   }

   try {
      return this.validationModeDetector.detectValidationMode(inputStream);
   }
   catch (IOException ex) {
      throw new BeanDefinitionStoreException("Unable to determine validation mode for [" +
            resource + "]: an error occurred whilst reading from the InputStream.", ex);
   }
}
```

XmlValidationModeDetector.java

~~~java
	public int detectValidationMode(InputStream inputStream) throws IOException {
		// Peek into the file to look for DOCTYPE.
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			boolean isDtdValidated = false;
			String content;
			while ((content = reader.readLine()) != null) {
				content = consumeCommentTokens(content);
//				如果读取到的是空行或者是注释则略过
				if (this.inComment || !StringUtils.hasText(content)) {
					continue;
				}
				if (hasDoctype(content)) {
					isDtdValidated = true;
					break;
				}
//				读取到<开始符号，验证模式一定会在开始符号之前
				if (hasOpeningTag(content)) {
					// End of meaningful data...
					break;
				}
			}
			return (isDtdValidated ? VALIDATION_DTD : VALIDATION_XSD);
		}
		catch (CharConversionException ex) {
			// Choked on some character encoding...
			// Leave the decision up to the caller.
			return VALIDATION_AUTO;
		}
	}
~~~

只要我们理解了XSD与DTD的使用方法，理解上面的代码应该不会太难，Spring用来检测验证模式的办法就是判断是否包含DOCTYPE，如果包含就是DTD，否则就是XSD。

### 获取Document

经过了验证模式准备的步骤就可以进行Document加载了，同样XmlBeanFactoryReader类对于文档读取并没有亲力亲为，而是委托给了DocumentLoader去执行，这里的DocumentLoader是个接口，而真正调用的是DefaultDocumentLoader，解析代码如下

```java
@Override
public Document loadDocument(InputSource inputSource, EntityResolver entityResolver,
      ErrorHandler errorHandler, int validationMode, boolean namespaceAware) throws Exception {

   DocumentBuilderFactory factory = createDocumentBuilderFactory(validationMode, namespaceAware);
   if (logger.isTraceEnabled()) {
      logger.trace("Using JAXP provider [" + factory.getClass().getName() + "]");
   }
   DocumentBuilder builder = createDocumentBuilder(factory, entityResolver, errorHandler);
   return builder.parse(inputSource);
}
```

首先创建DocumentBuilderFactory，再通过DocumentBuilderFactory创建DocumentBuilder，进而解析inputSource来返回Document对象。

这里有必要提及一下EntityResolver，对于参数entityResolver，传入的是通过getEntityResolver()函数获取的返回值，如下代码：

```java
protected EntityResolver getEntityResolver() {
   if (this.entityResolver == null) {
      // Determine default EntityResolver to use.
      ResourceLoader resourceLoader = getResourceLoader();
      if (resourceLoader != null) {
         this.entityResolver = new ResourceEntityResolver(resourceLoader);
      }
      else {
         this.entityResolver = new DelegatingEntityResolver(getBeanClassLoader());
      }
   }
   return this.entityResolver;
}
```

#### EntityResolver

如果SAX应用程序需要实现自定义处理外部实体，则必须实现此接口并使用setEntityResolver方法向SAX 驱动器注册一个实例。也就是说，对于解析一个XML，SAX首先读取该XML文档上的声明，根据声明去寻找相应的DTD定义，以便对文档进行一个验证。默认的寻找规则，即通过网络（实现上就是声明的DTD的URI地址）来下载相应的DTD声明，并进行认证。下载的过程是一个漫长的过程，而且当网络中断或不可用时，这里会报错，就是因为相应的DTD声明没有被找到的原因。

EntityResolver的作用是项目本身就可以提供一个如何寻找DTD声明的方法，即由程序来实现寻找DTD声明的过程，比如我们将DTD文件放到项目中某处，在实现时直接将此文档读取并返回给SAX即可。这样就避免了通过网络来寻找相应的声明。

EntityResolver的接口方法声明：

```java
public abstract InputSource resolveEntity (String publicId,
                                           String systemId)
    throws SAXException, IOException;
```

1. 如果解析验证模式为XSD的配置文件：

   ```xml
   <?xml version="1.0" encoding="UTF-8"?>
   <beans xmlns="http://www.Springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.Springframework.org/schema/beans
   
   
         http://www.springframework.org/schema/beans/Spring-beans.xsd
   
   ">
        ... ...
   </beans>
   ```

   读取到以下两个参数：

   - publicId： null
   - systemId：http://www.springframework.org/schema/beans/Spring-beans.xsd

2. 如果解析验证模式为DTD的配置文件：

   ~~~xml
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE beans PUBLIC "-//Spring//DTD BEAN 2.0//EN" "http://www.Springframework. org/dtd/Spring-beans-2.0.dtd">
   <beans>
   ... ...
   </beans>
   ~~~

   读取到以下两个参数。

   - publicId： -//Spring//DTD BEAN 2.0//EN
   - systemId： http://www.springframework.org/dtd/Spring-beans-2.0.dtd

验证文件默认的加载方式是通过URL进行网络下载获取，这样会造成延迟，用户体验也不好，一般的做法都是将验证文件放置在自己的工程里，那么怎么做才能将这个URL转换为自己工程里对应的地址文件呢？我们以加载DTD文件为例来看看Spring中是如何实现的。根据之前Spring中通过getEntityResolver()方法对EntityResolver的获取，我们知道，Spring中使用DelegatingEntityResolver类为EntityResolver的实现类，resolveEntity实现方法如下：

```
public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId)
      throws SAXException, IOException {

   if (systemId != null) {
      if (systemId.endsWith(DTD_SUFFIX)) {
         return this.dtdResolver.resolveEntity(publicId, systemId);
      }
      else if (systemId.endsWith(XSD_SUFFIX)) {
         return this.schemaResolver.resolveEntity(publicId, systemId);
      }
   }

   // Fall back to the parser's default behavior.
   return null;
}
```

可以看到，对不同的验证模式，Spring使用了不同的解析器解析。这里简单描述一下原理，比如加载DTD类型的BeansDtdResolver的resolveEntity是直接截取systemId最后的xx.dtd然后去当前路径下寻找，而加载XSD类型的PluggableSchemaResolver类的resolveEntity是默认到META-INF/Spring.schemas文件中找到systemid所对应的XSD文件并加载。

```java
public InputSource resolveEntity(@Nullable String publicId, @Nullable String systemId) throws IOException {
   if (logger.isTraceEnabled()) {
      logger.trace("Trying to resolve XML entity with public ID [" + publicId +
            "] and system ID [" + systemId + "]");
   }

   if (systemId != null && systemId.endsWith(DTD_EXTENSION)) {
      int lastPathSeparator = systemId.lastIndexOf('/');
      int dtdNameStart = systemId.indexOf(DTD_NAME, lastPathSeparator);
      if (dtdNameStart != -1) {
         String dtdFile = DTD_NAME + DTD_EXTENSION;
         if (logger.isTraceEnabled()) {
            logger.trace("Trying to locate [" + dtdFile + "] in Spring jar on classpath");
         }
         try {
            Resource resource = new ClassPathResource(dtdFile, getClass());
            InputSource source = new InputSource(resource.getInputStream());
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            if (logger.isTraceEnabled()) {
               logger.trace("Found beans DTD [" + systemId + "] in classpath: " + dtdFile);
            }
            return source;
         }
         catch (FileNotFoundException ex) {
            if (logger.isDebugEnabled()) {
               logger.debug("Could not resolve beans DTD [" + systemId + "]: not found in classpath", ex);
            }
         }
      }
   }

   // Fall back to the parser's default behavior.
   return null;
}
```

### 解析及注册BeanDefinitions

当把文件转换为Document后，接下来的提取及注册bean就是我们的重头戏。继续上面的分析，当程序已经拥有XML文档文件的Document实例对象时，就会被引入下面这个方法。

```java
	public int registerBeanDefinitions(Document doc, Resource resource) throws BeanDefinitionStoreException {
//		使用DefaultBeanDefinitionDocumentReader实例化
		BeanDefinitionDocumentReader documentReader = createBeanDefinitionDocumentReader();
//		记录统计前BeanDefinition的加载个数
		int countBefore = getRegistry().getBeanDefinitionCount();
//		加载及注册Bean
		documentReader.registerBeanDefinitions(doc, createReaderContext(resource));
//		记录本次加载的BeanDefinition个数
		return getRegistry().getBeanDefinitionCount() - countBefore;
	}
```

其中的参数doc是通过上一节loadDocument加载转换出来的。在这个方法中很好地应用了面向对象中单一职责的原则，将逻辑处理委托给单一的类进行处理，而这个逻辑处理类就是BeanDefinitionDocumentReader。BeanDefinitionDocumentReader是一个接口，而实例化的工作是在createBeanDefinitionDocumentReader()中完成的，而通过此方法，BeanDefinitionDocumentReader真正的类型其实已经是DefaultBeanDefinitionDocumentReader了，进入DefaultBeanDefinitionDocument- Reader后，发现这个方法的重要目的之一就是提取 root，以便于再次将root作为参数继续BeanDefinition的注册。

```java
public void registerBeanDefinitions(Document doc, XmlReaderContext readerContext) {
   this.readerContext = readerContext;
   doRegisterBeanDefinitions(doc.getDocumentElement());
}
```

如果说以前一直是XML加载解析的准备阶段，那么doRegisterBeanDefinitions算是真正地开始进行解析了，我们期待的核心部分真正开始了。

当我们跟进preProcessXml(root)或者postProcessXml(root)发现代码是空的，既然是空的写着还有什么用呢？就像面向对象设计方法学中常说的一句话，一个类要么是面向继承的设计的，要么就用final修饰。在DefaultBeanDefinitionDocumentReader中并没有用final修饰，所以它是面向继承而设计的。这两个方法正是为子类而设计的，如果读者有了解过设计模式，可以很快速地反映出这是模版方法模式，如果继承自DefaultBeanDefinitionDocumentReader的子类需要在Bean解析前后做一些处理的话，那么只需要重写这两个方法就可以了。

#### profile属性的使用

分析profile前我们先了解下profile的用法，官方示例代码片段如下：

```xml
<beans xmlns="http://www.Springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:jdbc="http://www. Springframework.org/schema/jdbc"
    xmlns:jee="http://www.springframework.org/schema/jee"
    xsi:schemaLocation="...">
     ... ...    
<beans profile="dev"

>
       ... ...
 </beans>
    <beans profile="production"

>
        ... ...
    </beans>
</beans>
```

集成到Web环境中时，在web.xml中加入以下代码：

```xml
  <context-param>
    <param-name>Spring.profiles.active</param-name>
    <param-value>dev

</param-value>
</context-param>
```

有了这个特性我们就可以同时在配置文件中部署两套配置来适用于生产环境和开发环境，这样可以方便的进行切换开发、部署环境，最常用的就是更换不同的数据库。

#### 解析注册BeanDefinition

处理了profile后就可以进行XML的读取了，跟踪代码进入parseBeanDefinitions(root, this.delegate)。

```java
	protected void parseBeanDefinitions(Element root, BeanDefinitionParserDelegate delegate) {
//		对beans的处理
		if (delegate.isDefaultNamespace(root)) {
			NodeList nl = root.getChildNodes();
			for (int i = 0; i < nl.getLength(); i++) {
				Node node = nl.item(i);
				if (node instanceof Element) {
					Element ele = (Element) node;
					if (delegate.isDefaultNamespace(ele)) {
//						对bean的处理
						parseDefaultElement(ele, delegate);
					}
					else {
//						对bean的处理
						delegate.parseCustomElement(ele);
					}
				}
			}
		}
		else {
			delegate.parseCustomElement(root);
		}
	}
```

上面的代码看起来逻辑还是蛮清晰的，因为在Spring的XML配置里面有两大类Bean声明，一个是默认的，如：

```xml
<bean id="test" class="test.TestBean"/>
```

另一类就是自定义的，如：

```xml
<tx:annotation-driven/>
```

而两种方式的读取及解析差别是非常大的，如果采用Spring默认的配置，Spring当然知道该怎么做，但是如果是自定义的，那么就需要用户实现一些接口及配置了。对于根节点或者子节点如果是默认命名空间的话则采用parseDefaultElement方法进行解析，否则使用delegate.parseCustomElement方法对自定义命名空间进行解析。而判断是否默认命名空间还是自定义命名空间的办法其实是使用node.getNamespaceURI()获取命名空间，并与Spring中固定的命名空间http://www.springframework.org/schema/beans进行比对。如果一致则认为是默认，否则就认为是自定义。



