package com.mashibing.servicesms.service;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicesms.request.SmsSendRequest;

public interface SmsService {

    ResponseResult sendSms(SmsSendRequest smsSendRequest);
}
