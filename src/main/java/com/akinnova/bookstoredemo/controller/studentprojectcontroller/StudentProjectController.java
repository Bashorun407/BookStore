package com.akinnova.bookstoredemo.controller.studentprojectcontroller;

import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectCreateDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectUpdateDto;
import com.akinnova.bookstoredemo.entity.StudentProject;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.studentprojectservice.StudentProjectServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/studentProject/auth")
public class StudentProjectController {
    @Autowired
    private StudentProjectServiceImpl studentProjectService;

    @PostMapping("/create")
    public ResponsePojo<StudentProject> createProject(@RequestBody StudentProjectCreateDto studentProjectCreateDto) {
        return studentProjectService.createProject(studentProjectCreateDto);
    }

    @GetMapping("/school/{schoolName}")
    public ResponseEntity<?> findProjectBySchool(@PathVariable String schoolName) {
        return studentProjectService.findProjectBySchool(schoolName);
    }

    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<?> findProjectByFaculty(@PathVariable String faculty) {
        return studentProjectService.findProjectByFaculty(faculty);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> findProjectByDepartment(@PathVariable String department) {
        return studentProjectService.findProjectByDepartment(department);
    }

    @GetMapping("/title/{projectTitle}")
    public ResponseEntity<?> findProjectByProjectTitle(@PathVariable String projectTitle) {
        return studentProjectService.findProjectByProjectTitle(projectTitle);
    }

    @GetMapping("/author/{author}")
    public ResponseEntity<?> findProjectByAuthor(@PathVariable String author) {
        return studentProjectService.findProjectByAuthor(author);
    }

    @PutMapping("/update")
    public ResponsePojo<StudentProject> updateProject(@RequestBody StudentProjectUpdateDto studentProjectUpdateDto) {
        return studentProjectService.updateProject(studentProjectUpdateDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteProject(@PathVariable String serialNumber) {
        return studentProjectService.deleteProject(serialNumber);
    }
}
