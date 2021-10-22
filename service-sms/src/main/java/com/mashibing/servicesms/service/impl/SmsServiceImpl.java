package com.mashibing.servicesms.service.impl;

import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.dto.servicesms.SmsTemplateDto;
import com.mashibing.internalcommon.dto.servicesms.request.SmsSendRequest;
import com.mashibing.servicesms.constant.SmsStatusEnun;
import com.mashibing.servicesms.dao.ServiceSmsRecordDao;
import com.mashibing.servicesms.dao.ServiceSmsTemplateCustomDao;
import com.mashibing.servicesms.entity.ServiceSmsRecord;
import com.mashibing.servicesms.entity.ServiceSmsTemplate;
import com.mashibing.servicesms.service.SmsService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class SmsServiceImpl implements SmsService {
    // 缓存用于替换内容的模版
    private Map<String, String> templateMaps = new HashMap<>();

    @Autowired
    private ServiceSmsTemplateCustomDao serviceSmsTemplateCustomDao;

    @Autowired
    private ServiceSmsRecordDao serviceSmsRecordDao;

    @Override
    public ResponseResult sendSms(SmsSendRequest smsSendRequest) {
        log.info(smsSendRequest.toString());

        for (String phoneNumber : smsSendRequest.getReceivers()) {
            List<SmsTemplateDto> templates = smsSendRequest.getData();
            ServiceSmsRecord sms = new ServiceSmsRecord();
            sms.setPhoneNumber(phoneNumber);
            for (SmsTemplateDto template : templates){
                // 从DB加载模版内容至缓存
                if (!templateMaps.containsKey(template.getId())) {
                    ServiceSmsTemplate smsTemplate = serviceSmsTemplateCustomDao.selectByTmplateId(template.getId());
                    templateMaps.put(template.getId(), smsTemplate.getTemplateContent());
                }
                // 替换占位符
                String content = templateMaps.get(template.getId());
                for (Map.Entry<String, Object> entry : template.getTemplateMap().entrySet()){
                    content = StringUtils.replace(content, "${" + entry.getKey() + "}", "" + entry.getValue());
                }
                // 发生错误，不影响其他手机号和模版的发送
                try{
                    int result = send(phoneNumber, template.getId(), template.getTemplateMap());
                    // 组装SMS对象
                    sms.setSendTime(new Date());
                    sms.setOperatorName("");
                    sms.setSendFlag(1);
                    sms.setSendNumber(0);
                    sms.setSmsContent(content);
                } catch (Exception e){
                    sms.setSendFlag(0);
                    sms.setSendNumber(1);
                    log.error("发送短信（" + template.getId() + "）失败，" + phoneNumber, e);
                } finally {
                    sms.setCreateTime(new Date());
                    serviceSmsRecordDao.insert(sms);
                }
            }
        }
        return ResponseResult.success("");
    }

    private int send(String phoneNumber, String templateId, Map<String, ?> map) {
        JSONObject param = new JSONObject();
        param.putAll(map);
        return sendMsg(phoneNumber, templateId, param.toString());
    }

    private int sendMsg(String phoneNumber, String templateId, String toString) {
        /**
         * 供应商发短信
         */
        return SmsStatusEnun.SEND_SUCCESS.getCode();
    }
}
