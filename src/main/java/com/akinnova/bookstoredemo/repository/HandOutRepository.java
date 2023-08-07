package com.akinnova.bookstoredemo.repository;

import com.akinnova.bookstoredemo.entity.HandOut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface HandOutRepository extends JpaRepository<HandOut, Long> {
    Boolean existsByCourseCode(String courseCode);
    Optional<List<HandOut>> findBySchoolName(String schoolName);
    Optional<List<HandOut>> findByFaculty(String faculty);
    Optional<List<HandOut>> findByDepartment(String department);
    Optional<List<HandOut>> findByLevel(int level);
    Optional<HandOut> findByCourseCode(String courseCode);
    Optional<HandOut> findByCourseTitle(String courseTitle);
    Optional<HandOut> findBySerialNumber(String serialNumber);

}
