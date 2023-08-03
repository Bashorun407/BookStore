package com.akinnova.bookstoredemo.controller.handoutcontroller;

import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
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
    public ResponsePojo<HandOut> createHandout(@RequestBody HandOutCreateDto handOutCreateDto) {
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

    @GetMapping("/level/{level}")
    public ResponseEntity<?> findHandOutBylevel(@PathVariable int level, @RequestParam(defaultValue = "1") int pageNum,
                                                @RequestParam(defaultValue = "10") int pageSize) {
        return handoutService.findHandOutBylevel(level, pageNum, pageSize);
    }

    @GetMapping("/courseCode/{courseCode}")
    public ResponseEntity<?> findHandOutByCourseCode(@PathVariable String courseCode) {
        return handoutService.findHandOutByCourseCode(courseCode);
    }

    @GetMapping("/courseTitle/{courseTitle}")
    public ResponseEntity<?> findHandOutByCourseTitle(@PathVariable String courseTitle) {
        return handoutService.findHandOutByCourseTitle(courseTitle);
    }

    @PutMapping("/update")
    public ResponsePojo<HandOut> updateHandOut(@RequestBody HandOutUpdateDto handOutUpdateDto) {
        return handoutService.updateHandOut(handOutUpdateDto);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteHandOut(String serialNumber) {
        return handoutService.deleteHandOut(serialNumber);
    }
}
