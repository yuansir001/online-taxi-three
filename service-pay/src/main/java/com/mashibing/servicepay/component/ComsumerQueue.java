package com.mashibing.servicepay.component;

import com.mashibing.servicepay.dao.TblPayEventDao;
import com.mashibing.servicepay.entity.TblPayEvent;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

@Component
public class ComsumerQueue {

    @Autowired
    private TblPayEventDao tblPayEventDao;

    /**
     * 监听mq
     * @param textMessage
     * @param session
     */
    @JmsListener(destination = "ActiveMQQueue", containerFactory = "jmsListenerContainerFactory")
    public void receive(TextMessage textMessage, Session session) throws JMSException {
        try {
            System.out.println("收到消息：" + textMessage.getText());
            String content = textMessage.getText();
            TblPayEvent tblPayEvent = (TblPayEvent)JSONObject.toBean(JSONObject.fromObject(content), TblPayEvent.class);
            tblPayEventDao.insert(tblPayEvent);
            // 业务完成确认消息 消费成功
            textMessage.acknowledge();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("异常了");
            session.recover();
        }
    }

    /**
     * 补偿 处理（人工，脚本）
     * @param text
     */
    @JmsListener(destination = "DLQ.ActiveMQQueue")
    public void receiveDLQ(String text){
        System.out.println("死信队列：" + text);
    }
}
