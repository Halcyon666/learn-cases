# Spring Boot `@Value` 启动默认值填充

## 支持类型

* String
* int Integer
* boolean Boolean
* `long` `Long`
* double `Double` 
* float Float
* byte Byte

## 属性上的

```java
    @Value("${org.xxx}")
    private String org;

    @Value("${org.number}")
    private int number;

    @Value("${org.flag}")
    private boolean flag;

    @Value("${org.b}")
    private byte b;

    @Value("${org.xxx1:}")
    private String org1;

    @Value("${org.xxx2:}")
    private String org2;
```

## 方法上面
```java
@Component
public class MethodParameterInjectionService {

    private final String appName;

    public MethodParameterInjectionService(@Value("${app.name}") String appName) {
        this.appName = appName;
    }

    @Value("${app.version}")
    public void setAppVersion(String appVersion) {
        System.out.println("App Version: " + appVersion);
    }


    public void setAppVersion( @Value("${app.version1}")String appVersion1, String hh) {
        System.out.println("App Version: " + appVersion1);
    }
}

```


> 目前还不支持`SPEL` 表达式
