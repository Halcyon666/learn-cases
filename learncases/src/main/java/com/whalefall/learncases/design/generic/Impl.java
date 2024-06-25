package com.whalefall.learncases.design.generic;

import com.whalefall.learncases.override.Father;
import com.whalefall.learncases.override.Son;

/**
 * @author WhaleFall
 * @date 2022-07-26 20:09
 */
public class Impl implements I<Father, Son> {

    @Override
    public Son getInstance(Father father) {
        father.work();
        return new Son();
    }
}
