package com.miaoshaproject.utils;

import com.google.code.kaptcha.text.TextProducer;
import com.google.code.kaptcha.util.Configurable;

public class ChineseTest extends Configurable implements TextProducer {

    /**
     * 中文实例
     * @return
     */
    @Override
    public String getText() {
        return null;
    }
}
