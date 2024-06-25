package com.whalefall.learncases.spel;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
@Slf4j
public class AspectMyAnnotation {


    /**
     * 用于SpEL表达式解析.
     */
    private final SpelExpressionParser parser = new SpelExpressionParser();
    /**
     * 用于获取方法参数定义名字.
     */
    private final DefaultParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(com.whalefall.learncases.spel.MyAnnotation)")
    public void pointcut() {
    }

    @Around(value = "pointcut()")
    public Object aspectA(ProceedingJoinPoint p) throws Throwable {
        MethodSignature sign = (MethodSignature) p.getSignature();
        Method method = sign.getMethod();

        MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
        if (myAnnotation == null) {
            return p.proceed();
        }

        TypeEnum value = myAnnotation.value();
        if (TypeEnum.TYPE1.equals(value)) {
            log.info("{} Type1", this.getClass().getSimpleName());

        } else {
            log.info("{} Type2", this.getClass().getSimpleName());
        }
        String spel = myAnnotation.spel();

        return String.format("Season:%s- UpperWorld:%s", value.getSeason(), generateKeyBySpEL(spel, p));
    }

    public String generateKeyBySpEL(String spelString, ProceedingJoinPoint joinPoint) {
        // 通过joinPoint获取被注解方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        // 使用spring的DefaultParameterNameDiscoverer获取方法形参名数组
        String[] paramNames = nameDiscoverer.getParameterNames(method);
        // 解析过后的Spring表达式对象
        Expression expression = parser.parseExpression(spelString);
        // spring的表达式上下文对象
        EvaluationContext context = new StandardEvaluationContext();
        // 通过joinPoint获取被注解方法的形参
        Object[] args = joinPoint.getArgs();
        // 给上下文赋值
        for (int i = 0; i < args.length; i++) {
            assert paramNames != null;
            context.setVariable(paramNames[i], args[i]);
        }

        /*如:
            @annotation(key="#student.name")
             method(Student student)
          */
        return String.valueOf(expression.getValue(context));
    }


}
