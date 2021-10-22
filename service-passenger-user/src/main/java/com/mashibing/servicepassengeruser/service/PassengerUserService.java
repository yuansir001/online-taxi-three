package com.mashibing.servicepassengeruser.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface PassengerUserService {
    ResponseResult login(String passengerPhone);

    ResponseResult logout(String token);
}
