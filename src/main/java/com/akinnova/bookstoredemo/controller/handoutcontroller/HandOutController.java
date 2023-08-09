package com.akinnova.bookstoredemo.controller.handoutcontroller;

import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutResponseDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutSearchDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutUpdateDto;
import com.akinnova.bookstoredemo.entity.HandOut;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.service.handoutservice.HandoutServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/handout/auth")
public class HandOutController {
    @Autowired
    private HandoutServiceImpl handoutService;

    @PostMapping("/create")
    public ResponsePojo<HandOutResponseDto> createHandout(@RequestBody HandOutCreateDto handOutCreateDto) {
        return handoutService.createHandout(handOutCreateDto);
    }

    @GetMapping("/school/{schoolName}")
    public ResponseEntity<?> findHandOutBySchool(@PathVariable String schoolName, @RequestParam(defaultValue = "1") int pageNum,
                                                 @RequestParam(defaultValue = "10") int pageSize) {
        return handoutService.findHandOutBySchool(schoolName, pageNum, pageSize);
    }

    @GetMapping("/faculty/{faculty}")
    public ResponseEntity<?> findHandOutByFaculty(@PathVariable String faculty, @RequestParam(defaultValue = "1") int pageNum,
                                                  @RequestParam(defaultValue = "10") int pageSize) {
        return handoutService.findHandOutByFaculty(faculty, pageNum, pageSize);
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<?> findHandOutByDepartment(@PathVariable String department, @RequestParam(defaultValue = "1") int pageNum,
                                                     @RequestParam(defaultValue = "10") int pageSize) {
        return handoutService.findHandOutByDepartment(department, pageNum, pageSize);
    }

    @GetMapping("/level")
    public ResponseEntity<?> findHandOutByLevel(@RequestBody HandOutSearchDto searchDto, @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return handoutService.findHandOutByLevel(searchDto, pageNum, pageSize);
    }

    @GetMapping("/courseCode")
    public ResponseEntity<?> findHandOutByCourseCode(@RequestBody HandOutSearchDto searchDto) {
        return handoutService.findHandOutByCourseCode(searchDto);
    }

    @GetMapping("/courseTitle")
    public ResponseEntity<?> findHandOutByCourseTitle(@RequestBody HandOutSearchDto searchDto) {
        return handoutService.findHandOutByCourseTitle(searchDto);
    }

    @PutMapping("/update")
    public ResponsePojo<HandOutResponseDto> updateHandOut(@RequestBody HandOutUpdateDto handOutUpdateDto) {
        return handoutService.updateHandOut(handOutUpdateDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHandOut(HandOutSearchDto searchDto) {
        return handoutService.deleteHandOut(searchDto);
    }
}
