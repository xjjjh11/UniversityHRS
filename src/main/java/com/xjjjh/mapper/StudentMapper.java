package com.xjjjh.mapper;


import com.xjjjh.pojo.Reimburse;
import com.xjjjh.pojo.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StudentMapper {

    //学生认证
    @Insert("insert into student values (default,#{s.name},#{s.number},#{s.academy}," +
                " #{s.sClass},#{s.sIdFront},#{s.sIdReverse},#{s.sReimbursableAmount})")
    int insStuInfo(@Param("s") Student student);
    //显示已经认证过的学生的信息
    @Select("select * from student where number=#{number}")
    Student selStudentByNum(@Param("number") String number);
    //根据学生id，查询学生信息和与之相关的报销信息
    @Select("select student.name,student.academy,student.sClass,student.number,reimburse.*" +
            " from student left join reimburse on student.id=reimburse.sid" +
            " where student.id=#{id}")
    Reimburse selStuAndReimById(@Param("id") Integer id);
    //修改学生信息
    @Update("update student set name=#{s.name},number=#{s.number},academy=#{s.academy}," +
            " sClass=#{s.sClass},sIdFront=#{s.sIdFront},sIdReverse=#{s.sIdReverse}" +
            " where id=#{s.id}")
    int updStuInfoById(@Param("s") Student student);
    //查询全部已认证的学生的学号跟姓名
    @Select("select name,number from student")
    List<Student> selAllStu();
}
