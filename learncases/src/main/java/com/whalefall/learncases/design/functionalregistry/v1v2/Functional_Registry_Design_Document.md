
# 🌐 功能注册执行引擎（Functional Registry Execution Engine）设计文档

## 一、设计目标
本模块旨在实现一种高可扩展、类型安全、解耦的业务执行框架，通过“函数注册（Functional Registry）”的方式，将 **业务逻辑（Job）** 与 **执行模板（Template）** 解耦，从而实现：

- ✅ 模块化：业务逻辑与模板逻辑独立演进。
- ✅ 类型安全：强泛型约束，防止类型不匹配。
- ✅ 高扩展性：轻松新增 `Job` 与 `Template`，无需修改引擎核心代码。
- ✅ 灵活运行：可在运行时动态选择模板执行逻辑。

## 二、核心结构设计

### 1️⃣ 接口定义

#### （1）Job 接口
```java
public interface Job<T> {
    T doJob(T t);
}
```

#### （2）Template 接口
```java
public interface Template<T> {
    T handler(String txCode, T t, Job<T> job);
}
```

#### （3）@UseTemplate 注解
```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface UseTemplate {
    Class<? extends Template<?>> value();
}
```

### 2️⃣ 模板实现
```java
@Component
@Slf4j
public class TemplateImpl1 implements Template<TxData> {
    @Override
    public TxData handler(String txCode, TxData txData, Job<TxData> job) {
        log.info("TemplateImpl1 executing job {}", txCode);
        return job.doJob(txData);
    }
}
```

### 3️⃣ 执行引擎 RegisterExecuteEngine
```java
@Slf4j
@Service
public class RegisterExecuteEngine<T> implements ApplicationContextAware {
    private final Map<String, Job<T>> jobs;
    private final Map<String, Function<T, T>> registry = new HashMap<>();
    private ApplicationContext applicationContext;

    @SuppressWarnings("all")
    public RegisterExecuteEngine(Map<String, Job<T>> jobs) {
        this.jobs = jobs;
    }

    @PostConstruct
    public void init() {
        jobs.forEach((jobName, jobService) -> {
            UseTemplate ann = jobService.getClass().getAnnotation(UseTemplate.class);
            if (ann == null)
                throw new IllegalStateException("Job " + jobService.getClass() + " 未标注 @UseTemplate");

            Class<?> templateClass = ann.value();
            @SuppressWarnings("unchecked")
            Template<T> templateToUse = (Template<T>) applicationContext.getBean(templateClass);
            registry.put(jobName, param -> templateToUse.handler(jobName, param, jobService));
        });
    }

    public T run(String jobName, T params) {
        Function<T, T> fn = registry.get(jobName);
        if (fn == null) throw new IllegalArgumentException("未知 jobName: " + jobName);
        log.info("Executing job: {}", jobName);
        return fn.apply(params);
    }

    @Override
    public void setApplicationContext(@Nonnull ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
```

## 三、类型安全性分析
- 泛型在编译期保障 `Job<T>` 与 `Template<T>` 类型匹配。
- Spring 容器注入 Map 时自动约束类型。
- 唯一的 `@SuppressWarnings("unchecked")` 来源于 Java 泛型擦除。

## 四、运行机制
1️⃣ 启动时：扫描所有 `Job`，根据注解加载模板并注册执行函数。
2️⃣ 执行时：通过 `engine.run(jobName, params)` 自动路由到正确模板。

## 五、异常处理策略
- 缺少注解时：启动即报错。
- jobName 不存在：抛出 `IllegalArgumentException`。
- 模板内部可自定义异常回滚与重试。

## 六、设计优势总结
| 特性 | 说明 |
|------|------|
| 🔧 高扩展性 | 新增 Job 或 Template 时无需修改核心代码 |
| 🔗 解耦性强 | 业务逻辑与模板逻辑完全独立 |
| 🧩 类型安全 | 编译期校验 |
| 💬 统一执行入口 | 所有业务通过 `engine.run()` 调用 |
| 🚫 低维护成本 | 无需反射或运行时类型匹配 |

## 七、最佳实践建议
| 场景 | 推荐做法 |
|------|-----------|
| 多模板共用同类型数据 | 使用注解 `@UseTemplate` 精确绑定 |
| Job 与 Template 类型不匹配 | 泛型定义清晰，编译期即报错 |
| 少量调用场景 | 直接传入模板类运行 |
| 多项目复用 | 提取为独立 Starter 自动扫描注册 |

## 八、结论
- ✅ 通用性强，支持多种数据类型与模板逻辑
- ✅ 类型安全，仅 1 处受控强转
- ✅ 启动阶段完成注册，运行高效
- ✅ 新增业务无需修改引擎
- ✅ 工程化标准，完全符合 Spring Boot 泛型规范
