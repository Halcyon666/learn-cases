package com.whalefall.com.whalefall.learncases.design.case2;

import com.whalefall.learncases.design.case2.ConcreteImpl;
import com.whalefall.learncases.design.case2.MainService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author Halcyon
 * @date 2024/6/5 19:37
 * @since 1.0.0
 */
class TestConcreteImpl {
    @Test
    void test() {
        MainService mainService = new MainService();
        ConcreteImpl concrete = new ConcreteImpl();
        concrete.setAge(18);
        concrete = mainService.doSomething(concrete);

        Assertions.assertThat(concrete).isNotNull();
        Assertions.assertThat(concrete.getAge()).isEqualTo(18);
        Assertions.assertThat(concrete.getName()).isEqualTo("Halcyon");
        Assertions.assertThat(concrete.getAddress()).isEqualTo("Beijing");
        Assertions.assertThat(concrete.getPostcode()).isEqualTo("518116");
        Assertions.assertThat(concrete.getPhone()).isEqualTo("12321321231");
    }
}
