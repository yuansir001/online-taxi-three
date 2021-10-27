package com.mashibing.servicepay.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * tbl_pay_event
 * @author 
 */
@Data
public class TblPayEvent implements Serializable {
    private Integer id;

    private Byte orderType;

    private Byte process;

    private String content;

    private Date createTime;

    private Date updateTime;

    private static final long serialVersionUID = 1L;
}