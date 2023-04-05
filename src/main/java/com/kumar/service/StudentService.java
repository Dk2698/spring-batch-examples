package com.kumar.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

//@Service
public class StudentService {
    List<StudentResponse> list = new ArrayList<>();
    public List<StudentResponse> restCallToGetStudents(){
        RestTemplate restTemplate = new RestTemplate();
        StudentResponse[] studentResponseArray = restTemplate.getForObject("http://localhost/get/students", StudentResponse[].class);


        for(StudentResponse st :studentResponseArray){
            list.add(st);
        }

        return  list;

    }

    public StudentResponse getStudent(){
        if(list == null){
            restCallToGetStudents();
        }

        if(list != null && !list.isEmpty()){
            return  list.remove(0);
        }
        return  null;
    }
}

class StudentResponse{

}

