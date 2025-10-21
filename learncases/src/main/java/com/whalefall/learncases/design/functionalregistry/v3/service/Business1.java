package com.whalefall.learncases.design.functionalregistry.v3.service;

import com.whalefall.learncases.design.functionalregistry.v3.anno.UseTemplate;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto1;
import com.whalefall.learncases.design.functionalregistry.v3.template.TemplateImpl1;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service
@Slf4j
@UseTemplate(TemplateImpl1.class)
public class Business1 implements BusinessType1 {


    @Override
    public BusinessDto1 doBusiness1(BusinessDto1 t) {
        return t;
    }
}
