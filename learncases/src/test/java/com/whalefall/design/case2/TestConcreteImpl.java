package com.whalefall.design.case2;

import com.whalefall.learncases.design.case2.AbstractClazz;
import com.whalefall.learncases.design.case2.ConcreteImpl;
import com.whalefall.learncases.design.case2.MainService;
import com.whalefall.learncases.design.case2.MainService2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/6/5 19:37
 * @since 1.0.0
 */
class TestConcreteImpl {
    /**
     * {@link MainService#doSomething(AbstractClazz)}
     * better than
     * {@link MainService#doSomethingUseAbstract(AbstractClazz)}
     */
    @Test
    void test() {
        MainService mainService = new MainService();
        ConcreteImpl concrete = new ConcreteImpl();
        concrete.setAge(18);
        concrete = mainService.doSomething(concrete);


        Assertions.assertThat(concrete).isNotNull();
        Assertions.assertThat(concrete.getAge()).isEqualTo(25);
        Assertions.assertThat(concrete.getName()).isEqualTo("Halcyon");
        Assertions.assertThat(concrete.getAddress()).isEqualTo("Beijing");
        Assertions.assertThat(concrete.getPostcode()).isEqualTo("518116");
        Assertions.assertThat(concrete.getPhone()).isEqualTo("12321321231");
    }

    @Test
    void test2() {
        MainService2<ConcreteImpl> mainService2 = new MainService2<>();
        ConcreteImpl concrete = new ConcreteImpl();
        concrete.setAge(18);
        concrete = mainService2.doSomething(concrete);

        Assertions.assertThat(concrete).isNotNull();
        Assertions.assertThat(concrete.getAge()).isEqualTo(25);
        Assertions.assertThat(concrete.getName()).isEqualTo("Halcyon");
        Assertions.assertThat(concrete.getAddress()).isEqualTo("Beijing");
        Assertions.assertThat(concrete.getPostcode()).isEqualTo("518116");
        Assertions.assertThat(concrete.getPhone()).isEqualTo("12321321231");
    }

    @Test
    void test1() {
        MainService mainService = new MainService();
        ConcreteImpl concrete = new ConcreteImpl();
        concrete.setAge(18);
       AbstractClazz abstractClazz = mainService.doSomethingUseAbstract(concrete);
        System.out.println("abstractClazz = " + abstractClazz);
        if (abstractClazz instanceof ConcreteImpl concrete1) {
            Assertions.assertThat(concrete1.getAge()).isEqualTo(25);
        }
        Assertions.assertThat(abstractClazz).isNotNull();
        Assertions.assertThat(abstractClazz.getName()).isEqualTo("Halcyon");
        Assertions.assertThat(abstractClazz.getAddress()).isEqualTo("Beijing");
        Assertions.assertThat(abstractClazz.getPostcode()).isEqualTo("518116");
        Assertions.assertThat(abstractClazz.getPhone()).isEqualTo("12321321231");
    }
}
