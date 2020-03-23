package com.xjjjh.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
public class Reimburse {

    Integer id;             //报销id
    String hospital;        //医院
    String medicalHistory;  //病历
    double money;           //花费金额
    String moneyInvoice;    //发票
    Integer prove = -1;     //转诊证明（有/无：1/0）
    String proveInvoice;    //转诊证明图片
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date time;              //时间
    Integer sid;            //学生id

}
