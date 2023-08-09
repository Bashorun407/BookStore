package com.akinnova.bookstoredemo.service.studentprojectservice;

import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectCreateDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectResponseDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectUpdateDto;
import com.akinnova.bookstoredemo.entity.StudentProject;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IStudentProjectService {
    ResponsePojo<StudentProjectResponseDto> createProject(StudentProjectCreateDto studentProjectCreateDto);
    ResponseEntity<?> findProjectBySchool(String schoolName, int pageNum, int pageSize);
    ResponseEntity<?> findProjectByFaculty(String faculty, int pageNum, int pageSize);
    ResponseEntity<?> findProjectByDepartment(String department, int pageNum, int pageSize);
    ResponseEntity<?> findProjectByProjectTitle(String projectTitle);
    ResponseEntity<?> findProjectByAuthor(String author, int pageNum, int pageSize);
    ResponsePojo<StudentProjectResponseDto> updateProject(StudentProjectUpdateDto studentProjectUpdateDto);
    ResponseEntity<?> deleteProject(String serialNumber);
}
