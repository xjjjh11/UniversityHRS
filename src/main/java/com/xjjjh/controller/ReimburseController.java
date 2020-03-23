package com.xjjjh.controller;

import com.xjjjh.pojo.Reimburse;
import com.xjjjh.pojo.Student;
import com.xjjjh.service.ReimburseService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
public class ReimburseController {

    @Autowired
    ReimburseService reimburseService;

    @GetMapping("/applyReim")
    @ResponseBody
    public String apply(Reimburse reimburse, MultipartFile file1
            , MultipartFile file2, MultipartFile file3, HttpServletRequest request){
        //防止文件报空指针异常
        if (file1!=null&&file2!=null&&file3!=null) {
            //病例；把图片以图片名的形式存入数据库中
            String medicalHistoryName = UUID.randomUUID().toString() + file1.getOriginalFilename().substring(file1.getOriginalFilename().lastIndexOf("."));
            //发票
            String moneyInvoiceName = UUID.randomUUID().toString() + file2.getOriginalFilename().substring(file2.getOriginalFilename().lastIndexOf("."));
            //转诊证明
            String proveInvoiceName = UUID.randomUUID().toString() + file3.getOriginalFilename().substring(file3.getOriginalFilename().lastIndexOf("."));
            //存放以上图片的路径
            String medicalHistoryPath = request.getServletContext().getRealPath("/images/reimbursement") + "/" + medicalHistoryName;
            String moneyInvoicePath = request.getServletContext().getRealPath("/images/reimbursement") + "/" + moneyInvoiceName;
            String proveInvoicePath = request.getServletContext().getRealPath("/images/reimbursement") + "/" + proveInvoiceName;
            try {
                FileUtils.copyInputStreamToFile(file1.getInputStream(), new File(medicalHistoryPath));
                FileUtils.copyInputStreamToFile(file2.getInputStream(), new File(moneyInvoicePath));
                FileUtils.copyInputStreamToFile(file3.getInputStream(), new File(proveInvoicePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //把图片放入报销类中
            reimburse.setMedicalHistory(medicalHistoryName);
            reimburse.setMoneyInvoice(moneyInvoiceName);
            reimburse.setProveInvoice(proveInvoiceName);
        }else {
            log.info("请上传图片");
        }
        //获取学生认证信息
        HttpSession session = request.getSession();
        Student studentInfo = (Student)session.getAttribute("studentInfo");
        //设置sid，用学生id关联报销申请
        reimburse.setSid(studentInfo.getId());
        int i = reimburseService.insReimburse(reimburse);
        //成功申请报销
        if (i>0){
            log.info("成功提交报销信息");
            return "success";
        }else
            return "error";
    }

    //显示报销内容
    @GetMapping("/showInfo")
    @ResponseBody
    public String showInfo(HttpSession session){
        //获取学生认证的信息
        Student studentInfo = (Student)session.getAttribute("studentInfo");
        log.info("studentInfo:"+studentInfo);
        //用学生id与找到与之对应的报销内容
        Integer sid = studentInfo.getId();
        List<Reimburse> reimburses = reimburseService.selReimBySid(sid);
        log.info("reimburses:"+reimburses);
        return "success";
    }
}
