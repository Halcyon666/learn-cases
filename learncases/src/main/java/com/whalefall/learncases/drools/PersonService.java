package com.whalefall.learncases.drools;

import lombok.AllArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @date 2024/5/23 23:34
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class PersonService {
    public static final String ERROR_MSG = "identity same but name not same";
    private final KieContainer kieContainer;

    public void kieTwoInsert() {
        KieSession kieSession = kieContainer.newKieSession("test-rules");
        Person p = Person.builder().identity("123").name("haha").build();
        Idcard i = Idcard.builder().identity("123").name("wohuo").build();
        kieSession.insert(p);
        kieSession.insert(i);
        kieSession.fireAllRules();
        kieSession.dispose();
    }

}
