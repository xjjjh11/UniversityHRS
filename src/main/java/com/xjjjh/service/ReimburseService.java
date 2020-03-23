package com.xjjjh.service;

import com.xjjjh.pojo.Reimburse;

import java.util.List;

public interface ReimburseService {

    /**
     * 提交报销申请
     * @param reimburse 报销内容
     * @return
     */
    int insReimburse(Reimburse reimburse);

    /**
     * 根据学生信息，查询与之相关的报销信息
     * @param sid：学生id
     * @return
     */
    List<Reimburse> selReimBySid(Integer sid);
}
