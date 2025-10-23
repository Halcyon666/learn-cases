package com.whalefall.learncases.design.functionalregistry.v1v2;

import com.whalefall.learncases.design.functionalregistry.v1v2.pojo.TxData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Halcyon
 * @since 2025/10/20 22:28
 */
@Service("business1v1")
@Slf4j
public class Business1Duplication {

    public TxData doJob(TxData txData) {
        log.info("{} executed", this.getClass().getSimpleName());
        return txData;
    }
}
