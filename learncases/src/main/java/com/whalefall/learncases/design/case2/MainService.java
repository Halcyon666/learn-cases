package com.whalefall.learncases.design.case2;

/**
 * @author Halcyon
 * @date 2024/6/5 19:38
 * @since 1.0.0
 */
public class MainService {
    public <T extends AbstractClazz> T doSomething(T abstractClazz) {
        abstractClazz.setName("Halcyon");
        abstractClazz.setAddress("Beijing");
        abstractClazz.setPostcode("518116");
        abstractClazz.setPhone("12321321231");
        if (abstractClazz instanceof ConcreteImpl concrete) {
            concrete.setAge(25);
        }
        return abstractClazz;
    }


    public AbstractClazz doSomethingUseAbstract(AbstractClazz abstractClazz) {
        abstractClazz.setName("Halcyon");
        abstractClazz.setAddress("Beijing");
        abstractClazz.setPostcode("518116");
        abstractClazz.setPhone("12321321231");
        if (abstractClazz instanceof ConcreteImpl concrete) {
            concrete.setAge(25);
        }
        return abstractClazz;
    }
}
