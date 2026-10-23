package com.gt.model;

import com.gt.enums.UserType;

public class Student extends User {


    
    public Student(int id, String name, String email) {
         super(id, name, email, UserType.STUDENT); 
    }

}
