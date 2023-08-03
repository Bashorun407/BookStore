package com.akinnova.bookstoredemo.service.handoutservice;

import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutUpdateDto;
import com.akinnova.bookstoredemo.entity.HandOut;
import com.akinnova.bookstoredemo.repository.HandOutRepository;
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
public class HandoutServiceImpl implements IHandOutService {

    private final HandOutRepository handOutRepository;

    public HandoutServiceImpl(HandOutRepository handOutRepository) {
        this.handOutRepository = handOutRepository;
    }

    @Override
    public ResponsePojo<HandOut> createHandout(HandOutCreateDto handOutCreateDto) {
        //Check if book with the same title for an institution, faculty and department exists
        Optional<HandOut> handOutOptional = handOutRepository.findByCourseCode(handOutCreateDto.getCourseCode())
                .filter(x-> (x.getSchoolName() == handOutCreateDto.getSchoolName()) && (x.getFaculty() == handOutCreateDto.getFaculty())
                 && (x.getDepartment() == handOutCreateDto.getDepartment()) && (x.getLevel() == handOutCreateDto.getLevel()));

        //If Handout already exists for the institution, faculty, department and level, notify user.
        if(!handOutOptional.isEmpty()){
            ResponsePojo<HandOut> responsePojo = new ResponsePojo<>();
            responsePojo.setStatusCode(ResponseUtils.BAD_REQUEST);
            responsePojo.setSuccess(false);
            responsePojo.setMessage(String.format("Hand-out with course code: %s, already exists for the Institution" +
                    ", faculty, department and level. Upload a new book.", handOutCreateDto.getCourseCode()));

            return responsePojo;
        }

        HandOut handOut = HandOut.builder()
                .imageAddress(handOutCreateDto.getImageAddress())
                .schoolName(handOutCreateDto.getSchoolName())
                .faculty(handOutCreateDto.getFaculty())
                .department(handOutCreateDto.getDepartment())
                .level(handOutCreateDto.getLevel())
                .courseCode(handOutCreateDto.getCourseCode())
                .courseTitle(handOutCreateDto.getCourseTitle())
                .summary(handOutCreateDto.getSummary())
                .serialNumber(ResponseUtils.generateBookSerialNumber(5,handOutCreateDto.getCourseTitle()))
                .createdBy(handOutCreateDto.getCreatedBy())
                .creatorEmail(handOutCreateDto.getEmail())
                .createdAt(LocalDateTime.now())
                .activeStatus(true)
                .build();

        //Save to repository
        HandOut handOutToReturn = handOutRepository.save(handOut);

        ResponsePojo<HandOut> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Successfully created");
        responsePojo.setData(handOutToReturn);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> findHandOutBySchool(String schoolName) {

        List<HandOut> handOutList = handOutRepository.findBySchoolName(schoolName).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(handOutList.isEmpty())
            return new ResponseEntity<>(String.format("Hand-outs with this school name: %s, are not available yet",
                    schoolName), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findHandOutByFaculty(String faculty) {
        List<HandOut> handOutList = handOutRepository.findByFaculty(faculty).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(handOutList.isEmpty())
            return new ResponseEntity<>(String.format("Hand-outs with faculty: %s, are not available yet",
                    faculty), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findHandOutByDepartment(String department) {
        List<HandOut> handOutList = handOutRepository.findByDepartment(department).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(handOutList.isEmpty())
            return new ResponseEntity<>(String.format("Hand-outs with this department: %s, are not available yet",
                    department), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findHandOutBylevel(int level) {
        List<HandOut> handOutList = handOutRepository.findByLevel(level).get()
                .stream().filter(x-> x.getActiveStatus().equals(true)).collect(Collectors.toList());

        if(handOutList.isEmpty())
            return new ResponseEntity<>(String.format("Hand-outs with this level: %d, are not available yet",
                    level), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findHandOutByCourseCode(String courseCode) {
        HandOut handOut = handOutRepository.findByCourseCode(courseCode).get();

        if(ObjectUtils.isEmpty(handOut) || (!handOut.getActiveStatus()))
            return new ResponseEntity<>(String.format("Hand out with this school name: %s, does not exist yet",
                    courseCode), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(handOut, HttpStatus.FOUND);
    }

    @Override
    public ResponseEntity<?> findHandOutByCourseTitle(String courseTitle) {

        HandOut handOut = handOutRepository.findByCourseCode(courseTitle).get();

        if(ObjectUtils.isEmpty(handOut) || (!handOut.getActiveStatus()))
            return new ResponseEntity<>(String.format("Hand out with this course title: %s, is not available",
                    courseTitle), HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(handOut, HttpStatus.FOUND);
    }

    @Override
    public ResponsePojo<HandOut> updateHandOut(HandOutUpdateDto handOutUpdateDto) {
        Optional<HandOut> handOut = handOutRepository.findByCourseCode(handOutUpdateDto.getCourseCode())
                .filter(x -> x.getActiveStatus().equals(true));

        if(ObjectUtils.isEmpty(handOut)){
            ResponsePojo<HandOut> responsePojo = new ResponsePojo<>();
            responsePojo.setStatusCode(ResponseUtils.NOT_FOUND);
            responsePojo.setSuccess(false);
            responsePojo.setMessage(String.format("Hand-out with course code: %s, does not exist", handOutUpdateDto.getCourseCode()));
            return responsePojo;
        }

        HandOut handOutUpdate = handOut.get();
        handOutUpdate.setImageAddress(handOutUpdateDto.getImageAddress());
        handOutUpdate.setSchoolName(handOutUpdateDto.getSchoolName());
        handOutUpdate.setFaculty(handOutUpdateDto.getFaculty());
        handOutUpdate.setDepartment(handOutUpdateDto.getDepartment());
        handOutUpdate.setLevel(handOutUpdateDto.getLevel());
        handOutUpdate.setCourseTitle(handOutUpdateDto.getCourseTitle());
        handOutUpdate.setSummary(handOutUpdateDto.getSummary());
        handOutUpdate.setModifiedBy(handOutUpdateDto.getModifiedBy());
        handOutUpdate.setModifierEmail(handOutUpdateDto.getModifierEmail());
        handOutUpdate.setActiveStatus(true);
        handOutUpdate.setModifiedAt(LocalDateTime.now());

        //Save to repository
        handOutRepository.save(handOutUpdate);

        //Response POJO to return
        ResponsePojo<HandOut> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format("Hand-out with course code: %s has been updated successfully",
                handOutUpdateDto.getCourseCode()));
        responsePojo.setData(handOutUpdate);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> deleteHandOut(String serialNumber) {

        HandOut handOut = handOutRepository.findBySerialNumber(serialNumber).get();

        if(ObjectUtils.isEmpty(handOut) || (!handOut.getActiveStatus()))
            return new ResponseEntity<>(String.format("Hand-out with serial number: %s does not exist", serialNumber),
                    HttpStatus.NOT_FOUND);

        //Change the active Status of the book
        handOut.setActiveStatus(false);
        //Save change to repository
        handOutRepository.save(handOut);

        return new ResponseEntity<>("Hand-out was successfully deleted.", HttpStatus.ACCEPTED);
    }
}
