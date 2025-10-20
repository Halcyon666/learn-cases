package com.whalefall.learncases.design.functionalregistry;

import com.whalefall.learncases.design.functionalregistry.pojo.TxData;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/run")
public class RunJobController {

    private final RegisterExecuteEngine<TxData> engine;

    public RunJobController(RegisterExecuteEngine<TxData> engine) {
        this.engine = engine;
    }

    @PostMapping("/{jobName}")
    public TxData execute(@PathVariable String jobName, @RequestBody TxData params) {
        return engine.run(jobName, params);
    }

}
