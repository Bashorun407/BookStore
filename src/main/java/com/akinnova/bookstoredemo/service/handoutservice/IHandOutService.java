package com.akinnova.bookstoredemo.service.handoutservice;

import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutUpdateDto;
import com.akinnova.bookstoredemo.entity.HandOut;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IHandOutService {
    ResponsePojo<HandOut> createHandout(HandOutCreateDto handOutCreateDto);
    ResponseEntity<?> findHandOutBySchool(String schoolName);
    ResponseEntity<?> findHandOutByFaculty(String faculty);
    ResponseEntity<?> findHandOutByDepartment(String department);
    ResponseEntity<?> findHandOutBylevel(int level);
    ResponseEntity<?> findHandOutByCourseCode(String courseCode);
    ResponseEntity<?> findHandOutByCourseTitle(String courseTitle);
    ResponsePojo<HandOut> updateHandOut(HandOutUpdateDto handOutUpdateDto);
    ResponseEntity<?> deleteHandOut(String serialNumber);
}
