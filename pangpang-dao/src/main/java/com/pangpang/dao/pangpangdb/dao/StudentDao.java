package com.pangpang.dao.pangpangdb.dao;

import com.pangpang.dao.base.PangpangdbBase;
import com.pangpang.dao.pangpangdb.entity.Student;
import org.springframework.stereotype.Component;

/**
 * Created by root on 16-6-25.
 */
@Component
public class StudentDao extends PangpangdbBase<Student, Number> {
        public StudentDao(){
            System.out.println("init StudentDao");
        }
}
