package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface AuthService {
    ResponseResult auth(String passengerPhone, String code);
}
