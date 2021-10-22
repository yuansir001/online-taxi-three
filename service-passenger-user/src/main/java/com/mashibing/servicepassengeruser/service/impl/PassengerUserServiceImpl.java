package com.mashibing.servicepassengeruser.service.impl;

import com.mashibing.internalcommon.constant.RedisKeyPrefixConstant;
import com.mashibing.internalcommon.dto.ResponseResult;
import com.mashibing.internalcommon.util.JwtUtil;
import com.mashibing.servicepassengeruser.dao.ServicePassengerUserInfoCustomDao;
import com.mashibing.servicepassengeruser.dao.ServicePassengerUserInfoDao;
import com.mashibing.servicepassengeruser.entity.ServicePassengerUserInfo;
import com.mashibing.servicepassengeruser.service.PassengerUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
public class PassengerUserServiceImpl implements PassengerUserService {

    @Autowired
    private ServicePassengerUserInfoCustomDao servicePassengerUserInfoCustomDao;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Override
    public ResponseResult login(String passengerPhone) {
        // 如果数据库没有此用户，插库。可以用分布式锁，也可以用唯一所引
        // 为什么次使用手机号？
        // 查出用户id

        ServicePassengerUserInfo passengerUserInfo = new ServicePassengerUserInfo();
        passengerUserInfo = servicePassengerUserInfoCustomDao.selectByPhoneNumber(passengerPhone);
        if (passengerUserInfo.getId() == null){
            passengerUserInfo.setCreateTime(new Date());
            passengerUserInfo.setPassengerGender((byte)1);
            passengerUserInfo.setPassengerName("");
            passengerUserInfo.setRegisterDate(new Date());
            passengerUserInfo.setUserState((byte)1);
            passengerUserInfo.setPassengerPhone(passengerPhone);

            servicePassengerUserInfoCustomDao.insertSelective(passengerUserInfo);
        }
        Long passengerUserInfoId = passengerUserInfo.getId();

        String token = JwtUtil.createToken(passengerUserInfoId + "", new Date());
        // 存入redis，设置过期时间。
        BoundValueOperations<String, String> stringStringBoundValueOperations = redisTemplate.boundValueOps(RedisKeyPrefixConstant.PASSENGER_LOGIN_TOKEN_APP_KEY_PRE + passengerUserInfoId);
        stringStringBoundValueOperations.set(token, 30, TimeUnit.DAYS);
        return ResponseResult.success(token);
    }

    @Override
    public ResponseResult logout(String passengerId) {
        setExpireToken(passengerId);
        return ResponseResult.success("");
    }

    private int setExpireToken(String passengerId) {
        String key = RedisKeyPrefixConstant.PASSENGER_LOGIN_TOKEN_APP_KEY_PRE + passengerId;
        redisTemplate.delete(key);
        return 1;
    }
}
