package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface ServicePassengerUserService {
    ResponseResult login(String passengerPhone);
}
