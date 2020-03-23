package com.xjjjh.pojo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
public class Student implements Serializable {

    Integer id;                 //学生id
    String name;                //姓名
    String number;              //学号
    String academy;             //学院
    String sClass;              //班级
    String sIdFront;            //身份证正面照
    String sIdReverse;          //身份证背面照
    double sReimbursableAmount; //报销金额

    public static String getKeyName(){
        return "student";
    }

}
