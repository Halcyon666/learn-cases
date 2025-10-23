package com.whalefall.learncases.springframework.beanname;

import com.whalefall.learncases.design.functionalregistry.v1v2.service.Business;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;

@Slf4j
public class UniquePackageBeanNameGenerator extends AnnotationBeanNameGenerator {
    @NotNull
    @Override
    public String generateBeanName(@NotNull BeanDefinition definition, @NotNull BeanDefinitionRegistry registry) {

        String beanClassName = definition.getBeanClassName();
        String originalBeanName = super.generateBeanName(definition, registry);
        if (beanClassName != null) {
            try {
                Class<?> clazz = Class.forName(beanClassName);
                if (Business.class.isAssignableFrom(clazz)) {
                    return "NEW" + originalBeanName;
                }
            } catch (ClassNotFoundException e) {
                // ignore non-user beans
                log.warn("❌ Failed to register new bean for {}", beanClassName, e);
                return originalBeanName;
            }
        }

        // 降级到默认行为
        return originalBeanName;
    }

}
