package com.whalefall.learncases.design.functionalregistry.v3;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessDto2;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType2;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/run")
@Slf4j
public class Business1Controller {

    @Resource
    private RegisterEnginV3<BusinessDto2, BusinessType2> registerEnginV3;
    /**
     * support template 1 and template 2
     * @param jobName job name
     * @param params parameters
     * @return result
     */
    @PostMapping("/v3/{jobName}")
    public BusinessDto2 executeV1(@PathVariable String jobName, @RequestBody BusinessDto2 params) {

        return registerEnginV3.run(jobName, params);
    }

}
