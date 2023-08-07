package com.akinnova.bookstoredemo.service.handoutservice;

import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutResponseDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutSearchDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutUpdateDto;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import org.springframework.http.ResponseEntity;

public interface IHandOutService {
    ResponsePojo<HandOutResponseDto> createHandout(HandOutCreateDto handOutCreateDto);
    ResponseEntity<?> findHandOutBySchool(String schoolName, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByFaculty(String faculty, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByDepartment(String department, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByLevel(HandOutSearchDto searchDto, int pageNum, int pageSize);
    ResponseEntity<?> findHandOutByCourseCode(HandOutSearchDto searchDto);
    ResponseEntity<?> findHandOutByCourseTitle(HandOutSearchDto searchDto);
    ResponsePojo<HandOutResponseDto> updateHandOut(HandOutUpdateDto handOutUpdateDto);
    ResponseEntity<?> deleteHandOut(HandOutSearchDto searchDto);
}
