package com.xjjjh;

import com.xjjjh.pojo.Student;
import com.xjjjh.service.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootApplicationTests {

    @Autowired
    StudentService studentService;

    @Test
    void contextLoads() {
        Student student = studentService.selStudentByNum("202011801129");
        System.out.println(student);
    }

    @Test
    void t1(){
        Student s = new Student();
        s.setAcademy("药学院");
        s.setSReimbursableAmount(88.92);
        s.setSIdReverse("124sgwq");
        s.setSIdFront("awrqw1257568");
        s.setName("若若");
        s.setNumber("202011801129");
        s.setSClass("护理系");
        s.setId(2);
    }
}
