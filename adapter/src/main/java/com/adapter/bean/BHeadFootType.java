package com.adapter.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({
        BHeadFootType.BOTH,
        BHeadFootType.HEADER,
        BHeadFootType.FOOTER
})
@Retention(RetentionPolicy.SOURCE)
public @interface BHeadFootType {
    int BOTH = 0;
    int HEADER = 1;
    int FOOTER = 2;
}