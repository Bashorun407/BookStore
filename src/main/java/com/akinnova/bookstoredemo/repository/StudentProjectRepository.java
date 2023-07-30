package com.akinnova.bookstoredemo.repository;


import com.akinnova.bookstoredemo.entity.StudentProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentProjectRepository extends JpaRepository<StudentProject, Long> {
    Optional<List<StudentProject>> findBySchoolName(String schoolName);
    Optional<List<StudentProject>> findByFaculty(String faculty);
    Optional<List<StudentProject>> findByDepartment(String department);
    Optional<StudentProject> findByProjectTitle(String projectTitle);
    Optional<List<StudentProject>> findByCreatedBy(String projectOwner);
    Optional<StudentProject> findBySerialNumber(String serialNumber);
}
