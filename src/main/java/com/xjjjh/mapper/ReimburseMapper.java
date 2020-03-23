package com.xjjjh.mapper;

import com.xjjjh.pojo.Reimburse;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ReimburseMapper {

    //申请报销，把学生id也存入，关联二者
    @Insert("insert into reimburse values (default,#{hospital},#{medicalHistory}," +
            " #{money},#{moneyInvoice},#{prove},#{proveInvoice},#{time},#{sid})")
    int insReimburse(Reimburse reimburse);
    //根据学生id，查询与之相关的报销信息
    @Select("select reimburse.*" +
            " from student left join reimburse" +
            " on student.id=reimburse.sid" +
            " where student.id=#{sid}")
    List<Reimburse> selReimBySid(@Param("sid") Integer sid);
}
