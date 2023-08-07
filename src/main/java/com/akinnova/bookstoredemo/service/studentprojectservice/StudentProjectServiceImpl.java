package com.akinnova.bookstoredemo.service.studentprojectservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectCreateDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectResponseDto;
import com.akinnova.bookstoredemo.dto.studentprojectdto.StudentProjectUpdateDto;

import com.akinnova.bookstoredemo.entity.StudentProject;
import com.akinnova.bookstoredemo.repository.StudentProjectRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class StudentProjectServiceImpl implements IStudentProjectService {

    private final StudentProjectRepository studentProjectRepository;

    //Class constructor
    public StudentProjectServiceImpl(StudentProjectRepository studentProjectRepository) {
        this.studentProjectRepository = studentProjectRepository;
    }

    @Override
    public ResponsePojo<StudentProjectResponseDto> createProject(StudentProjectCreateDto studentProjectCreateDto) {

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
                .activeStatus(true)
                .createdAt(LocalDateTime.now())
                .build();

        //Save to student repository
        StudentProject studentProjectToReturn = studentProjectRepository.save(studentProject);
        StudentProjectResponseDto responseDto = StudentProjectResponseDto.builder()
                .imageAddress(studentProjectToReturn.getImageAddress())
                .projectTitle(studentProjectToReturn.getProjectTitle())
                .summary(studentProjectToReturn.getSummary())
                .price(studentProjectToReturn.getPrice())
                .build();

        ResponsePojo<StudentProjectResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Successfully created");
        responsePojo.setData(responseDto);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> findProjectBySchool(String schoolName, int pageNum, int pageSize) {
        List<StudentProject> studentProjectList = studentProjectRepository.findBySchoolName(schoolName)
                .orElseThrow(()->
                        new ApiException(String.format("Student projects with this school name: %s, are not available yet",
                        schoolName)));

        List<StudentProjectResponseDto> responseDtoList = new ArrayList<>();

        studentProjectList.stream().filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .skip(pageNum - 1).limit(pageSize).map(
                        studentProject -> StudentProjectResponseDto.builder()
                                .imageAddress(studentProject.getImageAddress())
                                .projectTitle(studentProject.getProjectTitle())
                                .summary(studentProject.getSummary())
                                .price(studentProject.getPrice())
                                .build()
                ).forEach(responseDtoList::add);


        return ResponseEntity.ok()
                .header("Project-Page-Number", String.valueOf(pageNum))
                .header("Project-Page-Size", String.valueOf(pageSize))
                .header("Project-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findProjectByFaculty(String faculty, int pageNum, int pageSize) {
        List<StudentProject> studentProjectList = studentProjectRepository.findByFaculty(faculty)
                .orElseThrow(()-> new ApiException(String.format("Student projects with faculty: %s, are not available yet",
                        faculty)));

        List<StudentProjectResponseDto> responseDtoList = new ArrayList<>();

        studentProjectList.stream().filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .skip(pageNum - 1).limit(pageSize).map(
                        studentProject -> StudentProjectResponseDto.builder()
                                .imageAddress(studentProject.getImageAddress())
                                .projectTitle(studentProject.getProjectTitle())
                                .summary(studentProject.getSummary())
                                .price(studentProject.getPrice())
                                .build()
                ).forEach(responseDtoList::add);


        return ResponseEntity.ok()
                .header("Project-Page-Number", String.valueOf(pageNum))
                .header("Project-Page-Size", String.valueOf(pageSize))
                .header("Project-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findProjectByDepartment(String department, int pageNum, int pageSize) {
        List<StudentProject> studentProjectList = studentProjectRepository.findByDepartment(department)
                .orElseThrow(()-> new ApiException(String.format("Student projects with this department: %s, are not available yet",
                        department)));

        List<StudentProjectResponseDto> responseDtoList = new ArrayList<>();

        studentProjectList.stream().filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .skip(pageNum - 1).limit(pageSize).map(
                        studentProject -> StudentProjectResponseDto.builder()
                                .imageAddress(studentProject.getImageAddress())
                                .projectTitle(studentProject.getProjectTitle())
                                .summary(studentProject.getSummary())
                                .price(studentProject.getPrice())
                                .build()
                ).forEach(responseDtoList::add);


        return ResponseEntity.ok()
                .header("Project-Page-Number", String.valueOf(pageNum))
                .header("Project-Page-Size", String.valueOf(pageSize))
                .header("Project-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findProjectByProjectTitle(String projectTitle) {

        StudentProject studentProject = studentProjectRepository.findByProjectTitle(projectTitle)
                .orElseThrow(()-> new ApiException(String.format("Student project with this project title: %s, is not available",
                        projectTitle)));

        StudentProjectResponseDto responseDto = StudentProjectResponseDto.builder()
                .imageAddress(studentProject.getImageAddress())
                .projectTitle(studentProject.getProjectTitle())
                .summary(studentProject.getSummary())
                .price(studentProject.getPrice())
                .build();
        return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<?> findProjectByAuthor(String author, int pageNum, int pageSize) {
        List<StudentProject> studentProjectList = studentProjectRepository.findByCreatedBy(author)
                .orElseThrow(()-> new ApiException(String.format("Student projects with author name: %s, are not available yet",
                        author)));

        List<StudentProjectResponseDto> responseDtoList = new ArrayList<>();

        studentProjectList.stream().filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .skip(pageNum - 1).limit(pageSize).map(
                        studentProject -> StudentProjectResponseDto.builder()
                                .imageAddress(studentProject.getImageAddress())
                                .projectTitle(studentProject.getProjectTitle())
                                .summary(studentProject.getSummary())
                                .price(studentProject.getPrice())
                                .build()
                ).forEach(responseDtoList::add);


        return ResponseEntity.ok()
                .header("Project-Page-Number", String.valueOf(pageNum))
                .header("Project-Page-Size", String.valueOf(pageSize))
                .header("Project-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponsePojo<StudentProjectResponseDto> updateProject(StudentProjectUpdateDto studentProjectUpdateDto) {
        StudentProject studentProject = studentProjectRepository.findByProjectTitle(studentProjectUpdateDto.getProjectTitle())
                .filter(x -> x.getActiveStatus().equals(Boolean.TRUE))
                .orElseThrow(()-> new ApiException(String.format("Student project with title: %s, does not exist",
                        studentProjectUpdateDto.getProjectTitle())));

        studentProject.setImageAddress(studentProjectUpdateDto.getImageAddress());
        studentProject.setSchoolName(studentProjectUpdateDto.getSchoolName());
        studentProject.setFaculty(studentProjectUpdateDto.getFaculty());
        studentProject.setDepartment(studentProjectUpdateDto.getDepartment());
        studentProject.setProjectTitle(studentProjectUpdateDto.getProjectTitle());
        studentProject.setSummary(studentProjectUpdateDto.getSummary());
        studentProject.setPrice(studentProjectUpdateDto.getPrice());
        studentProject.setModifiedBy(studentProjectUpdateDto.getModifiedBy());
        studentProject.setEmail(studentProjectUpdateDto.getEmail());
        studentProject.setModifiedAt(LocalDateTime.now());

        //Save changes to repository
       StudentProject studentToReturn = studentProjectRepository.save(studentProject);
       //Preparing response dto
       StudentProjectResponseDto responseDto = StudentProjectResponseDto.builder()
               .imageAddress(studentToReturn.getImageAddress())
               .projectTitle(studentToReturn.getProjectTitle())
               .summary(studentToReturn.getSummary())
               .price(studentToReturn.getPrice())
               .build();

       ResponsePojo<StudentProjectResponseDto> responsePojo = new ResponsePojo<>();
       responsePojo.setMessage("Project was updated successfully");
       responsePojo.setData(responseDto);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> deleteProject(String serialNumber) {
        StudentProject studentProject = studentProjectRepository.findBySerialNumber(serialNumber)
                .orElseThrow(()->
                        new ApiException(String.format("Student project with serial number: %s does not exist",
                                serialNumber)));

        //Change the active Status of the book
        studentProject.setActiveStatus(false);
        //Save change to repository
        studentProjectRepository.save(studentProject);

        return new ResponseEntity<>("Student project was successfully deleted.", HttpStatus.ACCEPTED);
    }
}
