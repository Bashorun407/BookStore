package com.akinnova.bookstoredemo.service.studentprojectservice;

import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectCreateDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectUpdateDto;
import com.akinnova.bookstoredemo.entity.StudentProject;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IStudentProjectService {
    ResponsePojo<StudentProject> createProject(StudentProjectCreateDto studentProjectCreateDto);
    ResponseEntity<?> findProjectBySchool(String schoolName);
    ResponseEntity<?> findProjectByFaculty(String faculty);
    ResponseEntity<?> findProjectByDepartment(String department);
    ResponseEntity<?> findProjectByProjectTitle(String projectTitle);
    ResponseEntity<?> findProjectByAuthor(String author);
    ResponsePojo<StudentProject> updateProject(StudentProjectUpdateDto studentProjectUpdateDto);
    ResponseEntity<?> deleteProject(String serialNumber);
}
