package com.mashibing.apipassenger.service;

import com.mashibing.internalcommon.dto.ResponseResult;

public interface ServiceSmsRestTemplateService {

    public ResponseResult sendSms(String phoneNumber, String code);
}
