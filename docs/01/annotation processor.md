## the module of annotation-processor

JAVA APT base annotation processor, in this project, just print some message in the processors

## Write a processor 

[example file](https://github.com/Halcyon666/learn-cases/blob/main/annotation-processor/src/main/java/com/whalefall/apt/MyAnnotationProcessor.java)

If you want to know more about program a processor, you can refer the book of Core-Java-Vol.-II-Advanced-Features-12th-Edition-Cay-S.-Horstmann.

## how to use the processor jar in other project

### maven

```xml

<!--...-->
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>1.8</source>
        <target>1.8</target>
        <annotationProcessorPaths>
            <path>
                <groupId>com.whalefall</groupId>
                <artifactId>annotation-processor</artifactId>
                <version>0.0.1-SNAPSHOT</version>
            </path>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.26</version>
            </path>
        </annotationProcessorPaths>
        <showWarnings>true</showWarnings>
    </configuration>
</plugin>
        <!--...-->
```

Here are include lombok, thus I use the plugin.

### gradle

```groovy
dependencies {
    //    ...
    annotationProcessor 'com.whalefall:annotation-processor:0.0.1-SNAPSHOT'
    testAnnotationProcessor 'com.whalefall:annotation-processor:0.0.1-SNAPSHOT'
    // if you are using processor in test code
    
    // if you use this in other module of the aggregation project 
    annotationProcessor project(':annotation-processor')
}
```
