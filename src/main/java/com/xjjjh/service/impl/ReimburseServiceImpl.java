package com.xjjjh.service.impl;

import com.xjjjh.pojo.Reimburse;
import com.xjjjh.mapper.ReimburseMapper;
import com.xjjjh.service.ReimburseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReimburseServiceImpl implements ReimburseService {

    @Autowired
    ReimburseMapper reimburseMapper;

    /**
     * 申请报销
     * @param reimburse 报销内容
     * @return
     */
    @Override
    public int insReimburse(Reimburse reimburse) {
        String hospital = reimburse.getHospital();
        double money = reimburse.getMoney();
        int prove = reimburse.getProve();
        //确保填入所有信息
        if (hospital!=null&&money!=0&&prove!=-1) {
            return reimburseMapper.insReimburse(reimburse);
        }
        else {
            System.out.println("reimburse: " + reimburse);
            System.out.println("请完整填入信息");
            return -1;
        }
    }

    /**
     * 查询报销信息
     * @param sid：学生id
     * @return
     */
    @Override
    public List<Reimburse> selReimBySid(Integer sid) {
        List<Reimburse> reimburses = reimburseMapper.selReimBySid(sid);
        return reimburses;
    }
}
