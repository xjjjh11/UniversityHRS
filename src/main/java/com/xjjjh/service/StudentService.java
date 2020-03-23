package com.xjjjh.service;

import com.xjjjh.pojo.Student;

import java.util.List;

public interface StudentService {

    /**
     * 学生认证，插入学生信息
     * @param student
     * @return
     */
    int insStuInfo(Student student);

    /**
     * 显示已认证的学生信息
     * @param number 学号
     * @return
     */
    Student selStudentByNum(String number);

    /**
     * 修改学生信息
     * @param student 学生信息
     * @return
     */
    int updStuInfoById(Student student,String preNumber);

    /**
     * 查询全部用户
     * @return
     */
    List<Student> selAllStu(Integer id,String number);

}
