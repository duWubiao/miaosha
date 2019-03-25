package com.miaoshaproject.error;

import com.miaoshaproject.utils.CommonReturnType;

public interface CommonError {

    int getErrorCode();

    String getErrorMsg();

    CommonError setErrorMsg(String errorMsg);
}
