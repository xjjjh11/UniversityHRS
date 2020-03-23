package com.xjjjh.service.impl;

import com.xjjjh.mapper.StudentMapper;
import com.xjjjh.pojo.Student;
import com.xjjjh.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    @Resource(name = "redisTemplate")
    private HashOperations<String ,String, Student> hashOperations; //redisTemplate.opsForHash

    /**
     * 学生认证，插入学生信息
     * @param student
     * @return
     */
    @Override
    public int insStuInfo(Student student) {
        //获取认证姓名
        String name = student.getName();
        //班级
        String sClass = student.getSClass();
        //学院
        String academy = student.getAcademy();

        //用正则判断学生学号是否为12
        String number = student.getNumber();
        String regx = "\\d{12}";
        Matcher matcher = Pattern.compile(regx).matcher(number);

        //注册信息不为空
        if (name!=null || sClass!=null || academy!=null){
                //学号长度为12
                if (matcher.matches()) {
                    int a = studentMapper.insStuInfo(student);
                    //此处Ajax
                    log.info("资料上传成功，请耐心等待审核");
                    //每次注册，把学生id放入学生类中
                    int id = studentMapper.selStudentByNum(number).getId();
                    student.setId(id);
                    log.info("insStuInfo_StudentServiceImpl:"+student);
                    return a;
                    //学号长度不为12
                }else {
                    //此处应用ajax
                    log.info("insStuInfo_StudentServiceImpl:请输入12位长度的学号");
                }
            //学生信息没有填完整
        }else {
            //Ajax
            log.info("insStuInfo_StudentServiceImpl: 请输入完整信息");
        }
        return -1;
    }

    /**
     * 显示已认证的学生信息
     *
     * 需求：用户输入一个key
     *  先判断Redis中是否存在该key
     *      若存在，则直接从redis中查询，并返回
     *      若不存在，则从MySQL数据库中查询，再存入Redis中，并返回
     * @param number：学号
     * @return
     */
    @Override
    public Student selStudentByNum(String number) {
        //判断redis中是否存在该key
        if (hashOperations.hasKey(Student.getKeyName(),number)){
            log.info("在Redis中查询");
            return hashOperations.get(Student.getKeyName(),number);
        //Redis中不存在该key，从MySQL数据库查询
        }else {
            log.info("查询MySQL数据库");
            Student student = studentMapper.selStudentByNum(number);
            log.info("并把查询的结果存入Redis中");
            hashOperations.put(Student.getKeyName(),number,student);
            return student;
        }

    }

    /**
     * 修改学生信息
     *      修改信息成功后，删除Redis中所存的学生信息
     *      注意：
     *          要判断该学生是否已经存在，防止填错的情况
     * @param student 修改的学生信息
     * @return
     */
    @Override
    public int updStuInfoById(Student student,String preNumber) {
        //获取id
        Integer id = student.getId();
        //修改后的学号
        String afterNumber = student.getNumber();
        String regx = "\\d{12}";
        Matcher matcher = Pattern.compile(regx).matcher(afterNumber);
        //用正则判断学生学号是否为12
        if (matcher.matches()) {
            //查询修改的学生信息是否已注册
            List<Student> students = selAllStu(id, afterNumber);
            //没有重复的学生信息
            if (students == null) {
                log.info("修改成功！");
                //删除redis中所存的学生信息
                hashOperations.delete(Student.getKeyName(),preNumber);
                //修改MySQL中的学生信息
                return studentMapper.updStuInfoById(student);
            } else log.info("该学生已注册");
        }else log.info("请输入12位长度的学号");
        //如果没有修改成功，直接返回-1
        return -1;
    }

    /**
     * 查询是否有已注册的学生
     *      只要姓名、学号即可
     * @return
     */
    @Override
    public List<Student> selAllStu(Integer id,String number) {
        //获取数据库中全部学生类
        List<Student> students = studentMapper.selAllStu();
        int i = 0;
        //遍历全部学生集合
        while (i < students.size()) {
            Student stu = students.get(i);
            //不匹配自己那条数据
            if (id!=i+1){
                //数据库中查到有相同的学号,就返回查到的集合
                if (stu.getNumber().equals(number)) {
                    return students;
                }
            }
            i++;
        }
        //如果返回null，则说明没有找到相同的学生信息
        return null;
    }
}
