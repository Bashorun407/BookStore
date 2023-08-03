package com.akinnova.bookstoredemo.service.handoutservice;

import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutUpdateDto;
import com.akinnova.bookstoredemo.entity.HandOut;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IHandOutService {
    ResponsePojo<HandOut> createHandout(HandOutCreateDto handOutCreateDto);
    ResponseEntity<?> findHandOutBySchool(String schoolName, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByFaculty(String faculty, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByDepartment(String department, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutBylevel(int level, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByCourseCode(String courseCode);
    ResponseEntity<?> findHandOutByCourseTitle(String courseTitle);
    ResponsePojo<HandOut> updateHandOut(HandOutUpdateDto handOutUpdateDto);
    ResponseEntity<?> deleteHandOut(String serialNumber);
}
