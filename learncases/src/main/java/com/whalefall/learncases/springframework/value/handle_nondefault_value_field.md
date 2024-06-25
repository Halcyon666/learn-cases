# Spring Boot `@Value` 启动默认值填充

## 支持类型

* String
* int Integer
* boolean Boolean
* `long` `Long`
* double `Double` 
* float Float
* byte Byte
* List
* Map
* Array
* Queue
* Set

## 支持注解位置

```java
// 1. 属性上面
@Value("${org.xxx2:}")
private String org2;

// 2. 方法上面
@Value("${app.version}")
public void setAppVersion(String appVersion) {
}

// 3. 方法参数上面
public void setAppVersion(@Value("${app.version1}") String appVersion1, String hh, @Value("${app.flagA}") Boolean flagA) {
}

// 4.构造方法参数上面
public MethodParameterInjectionService(@Value("${app.name}") String appName) {
    this.appName = appName;
}
```

> 不支持`SPEL` 表达式
