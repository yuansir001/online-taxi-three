package com.mashibing.serviceorder.task;

import com.mashibing.serviceorder.dao.TblOrderEventDao;
import com.mashibing.serviceorder.entity.TblOrderEvent;
import net.sf.json.JSON;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.Queue;
import java.util.List;

@Component
public class ProduceTask {

    @Autowired
    private TblOrderEventDao tblOrderEventDao;

    @Autowired
    private Queue queue;

    @Autowired
    private JmsMessagingTemplate jmsMessagingTemplate;

    @Scheduled(cron="0/5 * * * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void task(){
        System.out.println("定时任务");

        List<TblOrderEvent> tblOrderEvents = tblOrderEventDao.selectByOrderType(1);
        for (TblOrderEvent tblOrderEvent : tblOrderEvents){
            // 更改这条数据的orderType为2
            tblOrderEvent.setOrderType((byte)2);
            tblOrderEventDao.updateByPrimaryKey(tblOrderEvent);
            jmsMessagingTemplate.convertAndSend(queue, JSONObject.fromObject(tblOrderEvent).toString());
        }
    }
}
