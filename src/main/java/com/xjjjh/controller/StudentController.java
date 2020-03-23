package com.xjjjh.controller;

import com.xjjjh.service.StudentService;
import com.xjjjh.pojo.Student;
import com.xjjjh.mapper.StudentMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Controller
@Slf4j
public class StudentController {

    @Autowired
    StudentService studentService;

    @Autowired
    StudentMapper studentMapper;

    /**
     * 学生认证
     * @param student 学生信息
     * @param model
     * @param file1 正面身份证照片
     * @param file2 反面身份证照片
     * @param request
     * @param session
     * @return
     */
    @GetMapping("/register")
    @ResponseBody
    public String confirm(Student student, Model model, MultipartFile file1
            ,MultipartFile file2, HttpServletRequest request, HttpSession session) {
        //把图片命名存放在数据库中
        if (file1!=null&&file2!=null) {
            //身份证正面；为确保每张图片名的唯一性，使用UUID随机命名(把图片的格式名保存，以最后一个.做分隔符)
            String idFrontName = UUID.randomUUID().toString() + file1.getOriginalFilename().substring(file1.getOriginalFilename().lastIndexOf("."));
            //身份证背面
            String idReverseName = UUID.randomUUID().toString() + file2.getOriginalFilename().substring(file2.getOriginalFilename().lastIndexOf("."));
            //身份证图片在电脑中存放的路径;getRealPath("/images/student")：在电脑中images的实际路径
            String idFrontPath = request.getServletContext().getRealPath("/images/student") + "/" + idFrontName;
            String idReversePath = request.getServletContext().getRealPath("/images/student") + "/" + idReverseName;

            //把图片赋值一份存放到定义的路径中
            try {
                FileUtils.copyInputStreamToFile(file1.getInputStream(), new File(idFrontPath));
                FileUtils.copyInputStreamToFile(file2.getInputStream(), new File(idReversePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把身份证存放到学生类中
            student.setSIdFront(idFrontName);
            student.setSIdReverse(idReverseName);
        }
        log.info("student:"+student);
        session.setAttribute("studentInfo",student);
        //学生认证之前先检查该学生是否重复注册；因为是注册，所以id写-1，直接查询全部的学生
        List<Student> students = studentService.selAllStu(-1, student.getNumber());
        //未注册
        if (students==null) {
            int i = studentService.insStuInfo(student);
            if (i > 0) {
                //把学生信息放入session中
                session.setAttribute("studentInfo", student);
                model.addAttribute("successFlag", "成功认证");
                //跳转到显示已认证的学生信息
                return "my";
            } else {
                model.addAttribute("falseFlag", "认证失败");
                //重新认证
                return "register";
            }
            //该学生已经注册了
        }else {
            //ajax
            System.out.println("confirm_StudentController: 该学生已注册");
            return "register";
        }
    }

    /**
     * 显示已认证的学生信息
     * @param session
     * @return
     */
    @GetMapping("/my")
    @ResponseBody
    public String show(HttpSession session){
        Student studentInfo = (Student)session.getAttribute("studentInfo");
        //获取学生学号
        String number = studentInfo.getNumber();
        Student s = studentService.selStudentByNum(number);
        log.info("学生信息："+s);
        return "showStudentSuccess";
    }

    /**
     * 修改学生信息       问题：可修改的有哪些
     * @param student 要修改的学生信息
     * @param session
     * @return
     */
    @GetMapping("modify")
    @ResponseBody
    public String updStuInfo(Student student,HttpSession session){
        Student studentInfo = (Student) session.getAttribute("studentInfo");
        //把学生id放入学生信息中
        Integer id = studentInfo.getId();
        //获取修改学生信息前学生的学号
        String preNumber = studentInfo.getNumber();
        if (id!=null) {
            student.setId(id);
            int i = studentService.updStuInfoById(student,preNumber);
            if (i > 0) {
                //修改完信息后返回显示信息页面
                return "my";
            } else
                //修改信息不成功则重新返回修改页面
                return "modify";
        }else
            return "modify";
    }
}
