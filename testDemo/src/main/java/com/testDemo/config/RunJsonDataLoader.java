package com.testDemo.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.testDemo.common.StudentRepository;
import com.testDemo.model.Student;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Configuration
public class RunJsonDataLoader implements CommandLineRunner {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(RunJsonDataLoader.class);
    private final StudentRepository studentRepository;
    final ObjectMapper objectMapper;

    public RunJsonDataLoader(StudentRepository studentRepository, ObjectMapper objectMapper) {
        this.studentRepository = studentRepository;
        this.objectMapper = objectMapper;
    }



    @Override
    public void run(String... args) throws Exception {
        if(studentRepository.count() == 0){
            try(InputStream inputStream = getClass().getResourceAsStream("/data/student.json")){
                List<Student> students = objectMapper.readValue(inputStream, new TypeReference<List<Student>>() {});
                studentRepository.saveAll(students);
                logger.info("Student loaded from JSON file{}", students);
            }catch (IOException e){
                throw new RuntimeException("Unable to load data from JSON file", e);
            }
        }else
            logger.info("Data already loaded");
    }
}
