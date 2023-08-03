package com.akinnova.bookstoredemo.service.studentprojectservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectCreateDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectUpdateDto;

import com.akinnova.bookstoredemo.entity.StudentProject;
import com.akinnova.bookstoredemo.repository.StudentProjectRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class StudentProjectServiceImpl implements IStudentProjectService {

    private final StudentProjectRepository studentProjectRepository;

    //Class constructor
    public StudentProjectServiceImpl(StudentProjectRepository studentProjectRepository) {
        this.studentProjectRepository = studentProjectRepository;
    }

    @Override
    public ResponsePojo<StudentProject> createProject(StudentProjectCreateDto studentProjectCreateDto) {
        //Check if book with the same title for an institution, faculty and department exists
        Optional<StudentProject> studentProjectOptional = studentProjectRepository.findByProjectTitle(studentProjectCreateDto.getProjectTitle())
                .filter(x-> (x.getSchoolName() == studentProjectCreateDto.getSchoolName()) && (x.getFaculty() == studentProjectCreateDto.getFaculty())
                        && (x.getDepartment() == studentProjectCreateDto.getDepartment()));

        //If Handout already exists for the institution, faculty, department and level, notify user.
        if(!studentProjectOptional.isEmpty()){
            ResponsePojo<StudentProject> responsePojo = new ResponsePojo<>();
            responsePojo.setStatusCode(ResponseUtils.BAD_REQUEST);
            responsePojo.setSuccess(false);
            responsePojo.setMessage(String.format("Student project with title: %s, already exists for the Institution" +
                    ", faculty, department and level. Upload a new book.", studentProjectCreateDto.getProjectTitle()));

            return responsePojo;
        }

        StudentProject studentProject = StudentProject.builder()
                .imageAddress(studentProjectCreateDto.getImageAddress())
                .schoolName(studentProjectCreateDto.getSchoolName())
                .faculty(studentProjectCreateDto.getFaculty())
                .department(studentProjectCreateDto.getDepartment())
                .projectTitle(studentProjectCreateDto.getProjectTitle())
                .summary(studentProjectCreateDto.getSummary())
                .price(studentProjectCreateDto.getPrice())
                .serialNumber(ResponseUtils.generateBookSerialNumber(5, studentProjectCreateDto.getProjectTitle()))
                .createdBy(studentProjectCreateDto.getCreatedBy())
                .email(studentProjectCreateDto.getEmail())
                .createdAt(LocalDateTime.now())
                .build();

        //Save to student repository
        StudentProject studentProjectToReturn = studentProjectRepository.save(studentProject);


        ResponsePojo<StudentProject> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Successfully created");
        responsePojo.setData(studentProjectToReturn);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> findProjectBySchool(String schoolName) {
        List<StudentProject> studentProjectList = studentProjectRepository.findBySchoolName(schoolName).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(studentProjectList.isEmpty())
            return new ResponseEntity<>(String.format("Student projects with this school name: %s, are not available yet",
                    schoolName), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentProjectList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findProjectByFaculty(String faculty) {
        List<StudentProject> studentProjectList = studentProjectRepository.findByFaculty(faculty).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(studentProjectList.isEmpty())
            return new ResponseEntity<>(String.format("Student projects with faculty: %s, are not available yet",
                    faculty), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentProjectList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findProjectByDepartment(String department) {
        List<StudentProject> studentProjectList = studentProjectRepository.findByDepartment(department).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(studentProjectList.isEmpty())
            return new ResponseEntity<>(String.format("Student projects with this department: %s, are not available yet",
                    department), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentProjectList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findProjectByProjectTitle(String projectTitle) {

        StudentProject studentProject = studentProjectRepository.findByProjectTitle(projectTitle).get();

        if(ObjectUtils.isEmpty(studentProject) || (!studentProject.getActiveStatus()))
            return new ResponseEntity<>(String.format("Student project with this project title: %s, is not available",
                    projectTitle), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentProject, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findProjectByAuthor(String author) {
        List<StudentProject> studentProjectList = studentProjectRepository.findByCreatedBy(author).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(studentProjectList.isEmpty())
            return new ResponseEntity<>(String.format("Student projects with author name: %s, are not available yet",
                    author), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(studentProjectList, HttpStatus.FOUND);
    }

    @Override
    public ResponsePojo<StudentProject> updateProject(StudentProjectUpdateDto studentProjectUpdateDto) {
        Optional<StudentProject> studentProjectOptional = studentProjectRepository.findByProjectTitle(studentProjectUpdateDto.getProjectTitle())
                .filter(x -> x.getActiveStatus().equals(true));

        studentProjectOptional.orElseThrow(()-> new ApiException(String.format("Student project with title: %s, does not exist",
                studentProjectUpdateDto.getProjectTitle())));
//

        StudentProject studentProjectToUpdate = studentProjectOptional.get();
        studentProjectToUpdate.setImageAddress(studentProjectUpdateDto.getImageAddress());
        studentProjectToUpdate.setSchoolName(studentProjectUpdateDto.getSchoolName());
        studentProjectToUpdate.setFaculty(studentProjectUpdateDto.getFaculty());
        studentProjectToUpdate.setDepartment(studentProjectUpdateDto.getDepartment());
        studentProjectToUpdate.setProjectTitle(studentProjectUpdateDto.getProjectTitle());
        studentProjectToUpdate.setSummary(studentProjectUpdateDto.getSummary());
        studentProjectToUpdate.setPrice(studentProjectUpdateDto.getPrice());
        studentProjectToUpdate.setModifiedBy(studentProjectUpdateDto.getModifiedBy());
        studentProjectToUpdate.setEmail(studentProjectUpdateDto.getEmail());
        studentProjectToUpdate.setModifiedAt(LocalDateTime.now());

        //Save changes to repository
       StudentProject studentProject = studentProjectRepository.save(studentProjectToUpdate);

       ResponsePojo<StudentProject> responsePojo = new ResponsePojo<>();
       responsePojo.setMessage("Project was updated successfully");
       responsePojo.setData(studentProject);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> deleteProject(String serialNumber) {
        StudentProject studentProject = studentProjectRepository.findBySerialNumber(serialNumber).get();

        if(ObjectUtils.isEmpty(studentProject) || (!studentProject.getActiveStatus()))
            return new ResponseEntity<>(String.format("Student project with serial number: %s does not exist", serialNumber),
                    HttpStatus.NOT_FOUND);

        //Change the active Status of the book
        studentProject.setActiveStatus(false);
        //Save change to repository
        studentProjectRepository.save(studentProject);

        return new ResponseEntity<>("Student project was successfully deleted.", HttpStatus.ACCEPTED);
    }
}
