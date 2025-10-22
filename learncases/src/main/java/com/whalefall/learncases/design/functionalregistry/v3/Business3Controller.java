package com.whalefall.learncases.design.functionalregistry.v3;

import cn.hutool.core.bean.BeanUtil;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.BusinessContext;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.InputDto3;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.RequestDto;
import com.whalefall.learncases.design.functionalregistry.v3.pojo.ResponseDto;
import com.whalefall.learncases.design.functionalregistry.v3.service.BusinessType3;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static cn.hutool.core.text.CharSequenceUtil.format;

@RestController
@RequestMapping("/run")
public class Business3Controller<I, O> {

    @Resource
    private RegisterEnginV3<BusinessContext<I, O>, BusinessType3<I, O>> registerEnginV3;
    /**
     * businessType = 3
     * @param businessType job name
     * @param requestDto parameters
     * @return result
     */
    @PostMapping("/v33/{businessType}")
    @SuppressWarnings("unchecked")
    public ResponseDto executeV1(@PathVariable String businessType, @RequestBody RequestDto requestDto) {
        BusinessContext<I, O> businessContext = new BusinessContext<>();
        InputDto3 inputDto3 = new InputDto3();
        inputDto3.setField1(requestDto.getRequestField());
        businessContext.setInput((I) inputDto3);
        BusinessContext<I, O> ioBusinessContext = registerEnginV3.run(businessType, businessContext);
        O output = ioBusinessContext.getOutput();
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
