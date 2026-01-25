package com.whalefall.learncases.design.functionalregistry.v1v2;

import com.whalefall.learncases.design.functionalregistry.v1v2.pojo.TxData;
import com.whalefall.learncases.design.functionalregistry.v1v2.template.TemplateImpl1;
import org.springframework.web.bind.annotation.*;

/**
 * 作业执行控制器
 * <p>
 * 提供两种版本的作业执行接口，支持不同的模板执行方式
 *
 * @author WhaleFall
 * @date 2022-07-24 6:34
 */
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
     * 执行作业接口（版本1）
     * <p>
     * POST /run/v1/{jobName}
     * <p>
     * 功能说明：使用RegisterEnginV1执行作业，支持模板1和模板2
     * jobName参数用于标识具体的作业类型，会自动添加"NEW"前缀
     *
     * @param jobName 作业名称
     * @param params 作业执行参数
     * @return TxData 执行结果数据
     */
    @PostMapping("/v1/{jobName}")
    public TxData executeV1(@PathVariable String jobName, @RequestBody TxData params) {
        return registerEnginV1.run("NEW" + jobName, params);
    }

    /**
     * 执行作业接口（版本2）
     * <p>
     * POST /run/v2/{jobName}
     * <p>
     * 功能说明：使用RegisterEnginV2执行作业，仅支持模板1
     * jobName参数用于标识具体的作业类型，会自动添加"NEW"前缀
     *
     * @param jobName 作业名称
     * @param params 作业执行参数
     * @return TxData 执行结果数据
     */
    @PostMapping("/v2/{jobName}")
    public TxData executeV21(@PathVariable String jobName, @RequestBody TxData params) {
        return registerEnginV2.run("NEW" + jobName, params, TemplateImpl1.class);
    }
}
