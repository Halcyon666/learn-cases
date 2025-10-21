package com.whalefall.learncases.design.functionalregistry.v3;

import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType2;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/run")
@Slf4j
public class Business1Controller {

    @Resource
    private RegisterEnginV3<Map<String, Object>, BusinessType2> registerEnginV3;
    /**
     * support template 1 and template 2
     * @param jobName job name
     * @param params parameters
     * @return result
     */
    @PostMapping("/v3/business-type2/{jobName}")
    public Map<String, Object> executeV1(@PathVariable String jobName, @RequestBody Map<String, Object> params) {

        return registerEnginV3.run(jobName, params);
    }

}
