package com.whalefall.learncases.design.functionalregistry.v3;

import cn.hutool.core.bean.BeanUtil;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessContext;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.InputDto3;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.RequestDto;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.ResponseDto;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType3;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static cn.hutool.core.text.CharSequenceUtil.format;

/**
 * 业务3控制器 - 功能注册执行引擎V3版本
 * <p>
 * 提供基于业务类型（businessType）的通用业务执行接口，支持动态业务逻辑调用。
 * 该控制器是功能注册执行引擎（Functional Registry Execution Engine）的V3实现，
 * 通过将业务逻辑与执行模板解耦，实现高可扩展、类型安全的业务执行框架。
 * </p>
 *
 * @author Halcyon
 * @since 2025/10/22
 */
@RestController
@RequestMapping("/run")
@Slf4j
public class Business3Controller<I, O> {

    @Resource
    private RegisterEnginV3<BusinessContext<I, O>, BusinessType3<I, O>> registerEnginV3;

    @SuppressWarnings("all")
    /**
     * 执行业务3接口
     * <p>
     * POST /run/v33/{businessType} - 根据业务类型执行对应的业务逻辑
     * </p>
     * <p>
     * 请求示例：
     * <pre>
     * POST http://localhost:8080/run/v33/business3
     * Content-Type: application/json
     *
     * {
     *   "otherParams": {
     *     "field1": "value1",
     *     "key2": 123,
     *     "key3": true,
     *     "key4": {
     *       "nestedKey": "nestedValue"
     *     },
     *     "key5": ["item1", "item2"]
     *   }
     * }
     * </pre>
     * </p>
     * <p>
     * 参数说明：
     * <ul>
     *   <li>{@code businessType}：业务类型标识符，用于匹配对应的业务实现类</li>
     *   <li>{@code requestDto}：请求数据传输对象，包含业务参数</li>
     * </ul>
     * </p>
     * <p>
     * 返回值：ResponseDto - 包含处理结果的响应对象
     * </p>
     *
     * @param businessType 业务类型标识符
     * @param requestDto   请求数据传输对象
     * @return 处理结果的响应对象
     * @throws NoSuchMethodException      当反射创建实例失败时抛出
     * @throws InvocationTargetException  当反射调用方法失败时抛出
     * @throws InstantiationException     当反射创建实例失败时抛出
     * @throws IllegalAccessException     当访问私有成员失败时抛出
     */
    @PostMapping("/v33/{businessType}")
    public ResponseDto executeV1(@PathVariable String businessType,
                                 @RequestBody RequestDto requestDto)
            throws NoSuchMethodException, InvocationTargetException,
            InstantiationException, IllegalAccessException {
        BusinessContext<I, O> businessContext = new BusinessContext<>();
        // 假如我用注解的方式，在Business类上指定Input的Class类型和Output的Class类型
        @SuppressWarnings("unchecked")
        Class<I> inputClass = (Class<I>) InputDto3.class;
        I inputDto3 = inputClass.getDeclaredConstructor().newInstance();

        BeanUtil.copyProperties(requestDto.getOtherParams(), inputDto3);
        // Converted requestDto to inputDto3: InputDto3(field1=value1, key2=123, key3=true)
        log.info("Converted requestDto to inputDto3: {}", inputDto3);
        businessContext.setInput(inputDto3);
        registerEnginV3.run(businessType, businessContext);
        O output = businessContext.getOutput();
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(output);
        // 这里可以是一个xml或者别的定义，来打包返回结果
        // 假设先写死Output的类型是OutputDto3
        Object field2 = stringObjectMap.get("field2");
        Object field3 = stringObjectMap.get("field3");
        ResponseDto responseDto = new ResponseDto();
        responseDto.setResponseField(format("field2={}, field3={}", field2, field3));
        return responseDto;
    }


}