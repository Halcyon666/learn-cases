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

@RestController
@RequestMapping("/run")
@Slf4j
public class Business3Controller<I, O> {

    @Resource
    private RegisterEnginV3<BusinessContext<I, O>, BusinessType3<I, O>> registerEnginV3;

    @SuppressWarnings("all")
    /**
     * ###
     * POST http://localhost:8080/run/v33/business3
     * Content-Type: application/json
     * <p>
     * {
     * "otherParams": {
     * "field1": "value1",
     * "key2": 123,
     * "key3": true,
     * "key4": {
     * "nestedKey": "nestedValue"
     * },
     * "key5": ["item1", "item2"]
     * }
     * }
     * businessType = 3
     *
     * @param businessType job name
     * @param requestDto   parameters
     * @return result
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
