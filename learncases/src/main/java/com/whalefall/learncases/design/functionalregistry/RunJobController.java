package com.whalefall.learncases.design.functionalregistry;

import com.whalefall.learncases.design.functionalregistry.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.template.TemplateImpl1;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/run")
public class RunJobController {

    private final RegisterEnginV1<TxData> registerEnginV1;
    private final RegisterEnginV2<TxData> registerEnginV2;

    public RunJobController(RegisterEnginV1<TxData> registerEnginV1, RegisterEnginV2<TxData> registerEnginV2) {
        this.registerEnginV1 = registerEnginV1;
        this.registerEnginV2 = registerEnginV2;
    }


    /**
     * support template 1 and template 2
     * @param jobName job name
     * @param params parameters
     * @return result
     */
    @PostMapping("v1/{jobName}")
    public TxData executeV1(@PathVariable String jobName, @RequestBody TxData params) {
        return registerEnginV1.run(jobName, params);
    }

    /**
     * support template 1 only
     * @param jobName job name
     * @param params parameters
     * @return result
     */
    @PostMapping("v2/{jobName}")
    public TxData executeV21(@PathVariable String jobName, @RequestBody TxData params) {
        return registerEnginV2.run(jobName, params, TemplateImpl1.class);
    }

}
