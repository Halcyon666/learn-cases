package com.whalefall.learncases.design.functionalregistry.v3.service;

import com.whalefall.learncases.design.functionalregistry.v3.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessContext;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.InputDto3;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.OutputDto3;
import com.whalefall.learncases.design.functionalregistry.v3.template.TemplateImpl3;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service
@Slf4j
@UseTemplate(TemplateImpl3.class)
public class Business3 implements BusinessType3<InputDto3, OutputDto3> {


    @Override
    public void doBusiness2(BusinessContext<InputDto3, OutputDto3> businessContext) {
        OutputDto3 output = new OutputDto3();
        output.setField2("result from Business3");
        output.setField3("xxx");
        businessContext.setOutput(output);
    }
}
