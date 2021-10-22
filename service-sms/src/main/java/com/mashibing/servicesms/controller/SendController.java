package com.mashibing.servicesms.controller;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicesms.request.SmsSendRequest;
import com.mashibing.servicesms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/send")
@Slf4j
public class SendController {

    @Autowired
    private SmsService smsService;

    @PostMapping("/sms-template")
    public ResponseResult send(@RequestBody SmsSendRequest smsSendRequest){
        JSONObject jsonObject = JSONObject.fromObject(smsSendRequest);
        log.info("/send/alisms-template request:" + jsonObject.toString());
        return smsService.sendSms(smsSendRequest);
    }
}
