package com.adapter.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;

@IntDef({
        BStatusType.ALL,
        BStatusType.ERROR,
        BStatusType.EMPTY,
        BStatusType.PROGRESSING
})
@Retention(RetentionPolicy.SOURCE)
public @interface BStatusType {
    int ALL = -2;
    int ERROR = -1;
    int EMPTY = 0;
    int PROGRESSING = 1;
}