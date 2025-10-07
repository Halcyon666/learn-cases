/*
 * Copyright (C) 2012-2016 Markus Junginger, greenrobot (http://greenrobot.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whalefall.use.apt;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.meta.SimpleSubscriberInfo;
import org.greenrobot.eventbus.meta.SubscriberInfoIndex;
import org.greenrobot.eventbus.meta.SubscriberMethodInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// do not change Junit4 -> junit5 in this file
public class EventBusIndexTest {
    private String value;

    /**
     * Ensures the index is actually used and no reflection fall-back kicks in.
     */
    @Test
    public void testManualIndexWithoutAnnotation() {
        SubscriberInfoIndex index = subscriberClass -> {
            assertEquals(EventBusIndexTest.class, subscriberClass);
            SubscriberMethodInfo[] methodInfos = {
                    new SubscriberMethodInfo("someMethodWithoutAnnotation", String.class)
            };
            return new SimpleSubscriberInfo(EventBusIndexTest.class, false, methodInfos);
        };

        EventBus eventBus = EventBus.builder().addIndex(index).build();
        eventBus.register(this);
        eventBus.post("Yepp");
        eventBus.unregister(this);
        assertEquals("Yepp", value);
    }

    @SuppressWarnings("unused")
    public void someMethodWithoutAnnotation(String value) {
        this.value = value;
    }

    @Test
    public void testReflect() {
        EventBus eventBus = EventBus.getDefault();
        eventBus.register(this);
        eventBus.post("Yepp");
        eventBus.unregister(this);
        Assertions.assertNotNull(eventBus);
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(String event) {
        assertEquals("Yepp1", event);
    }
}
