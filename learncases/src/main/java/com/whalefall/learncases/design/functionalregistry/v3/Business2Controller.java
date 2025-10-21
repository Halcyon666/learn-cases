package com.whalefall.learncases.design.functionalregistry.v3;

import com.whalefall.learncases.design.functionalregistry.v3.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType1;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/run")
public class Business2Controller {

    @Resource
    private RegisterEnginV3<TxData, BusinessType1> registerEnginV3;
    /**
     * support template 1 and template 2
     * @param jobName job name
     * @param params parameters
     * @return result
     */
    @PostMapping("/v3/business-type1/{jobName}")
    public TxData executeV1(@PathVariable String jobName, @RequestBody TxData params) {
        return registerEnginV3.run(jobName, params);
    }


}
