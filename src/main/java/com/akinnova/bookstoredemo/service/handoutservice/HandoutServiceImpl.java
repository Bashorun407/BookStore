package com.akinnova.bookstoredemo.service.handoutservice;

import com.akinnova.bookstoredemo.Exception.ApiException;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutCreateDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutResponseDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutSearchDto;
import com.akinnova.bookstoredemo.dto.handoutdto.HandOutUpdateDto;
import com.akinnova.bookstoredemo.entity.HandOut;
import com.akinnova.bookstoredemo.repository.HandOutRepository;
import com.akinnova.bookstoredemo.response.ResponsePojo;
import com.akinnova.bookstoredemo.response.ResponseUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class HandoutServiceImpl implements IHandOutService {

    private final HandOutRepository handOutRepository;

    public HandoutServiceImpl(HandOutRepository handOutRepository) {
        this.handOutRepository = handOutRepository;
    }

    @Override
    public ResponsePojo<HandOutResponseDto> createHandout(HandOutCreateDto handOutCreateDto) {
        //Check if book with the same title for an institution, faculty and department exists
        //Throw an exception if a book with same details already exist
        handOutRepository.findByCourseCode(handOutCreateDto.getCourseCode())
                .filter(x-> (x.getSchoolName().equals(handOutCreateDto.getSchoolName()) && (x.getFaculty().equals(handOutCreateDto.getFaculty()))
                 && (x.getDepartment().equals(handOutCreateDto.getDepartment())) && (x.getLevel().equals(handOutCreateDto.getLevel()))))
                        .orElseThrow(()-> new ApiException(String.format("Hand-out with course code: %s, already exists for the Institution" +
                                        ", faculty, department and level. Upload a new book.",
                        handOutCreateDto.getCourseCode())));


        //If hand out is not already in the database/repository, create new one.
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

        HandOutResponseDto responseDto = HandOutResponseDto.builder()
                .imageAddress(handOutToReturn.getImageAddress())
                .courseCode(handOutToReturn.getCourseCode())
                .courseTitle(handOutToReturn.getCourseTitle())
                .summary(handOutToReturn.getSummary())
                .build();



        ResponsePojo<HandOutResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage("Successfully created");
        responsePojo.setData(responseDto);

        return responsePojo;
    }

    @Override
    public ResponseEntity<List<HandOutResponseDto>> findHandOutBySchool(String schoolName, int pageNum, int pageSize) {

        List<HandOut> handOutList = handOutRepository.findBySchoolName(schoolName)
                .orElseThrow(()->
                        new ApiException(String.format("Hand-outs with this school: %s, are not available yet", schoolName)));
        List<HandOutResponseDto> responseDtoList = new ArrayList<>();

        //Preparing objects that will be returned back to the user; book has to be active and from the department specified
        handOutList.stream().skip(pageNum - 1).limit(pageSize).filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .map(
                        handOut -> HandOutResponseDto.builder()
                                .imageAddress(handOut.getImageAddress())
                                .courseCode(handOut.getCourseCode())
                                .courseTitle(handOut.getCourseTitle())
                                .level(handOut.getLevel())
                                .summary(handOut.getSummary())
                                .build()
                ).forEach(responseDtoList::add);

        //return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Handout-Page-Number", String.valueOf(pageNum))
                .header("Handout-Page-Size", String.valueOf(pageSize))
                .header("Handout-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findHandOutByFaculty(String faculty, int pageNum, int pageSize) {
        List<HandOut> handOutList = handOutRepository.findByFaculty(faculty)
                .orElseThrow(()->
                        new ApiException(String.format("Hand-outs with this faculty: %s, are not available yet", faculty)));
        List<HandOutResponseDto> responseDtoList = new ArrayList<>();

        //Preparing objects that will be returned back to the user; book has to be active and from the department specified
        handOutList.stream().skip(pageNum - 1).limit(pageSize).filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .map(
                        handOut -> HandOutResponseDto.builder()
                                .imageAddress(handOut.getImageAddress())
                                .courseCode(handOut.getCourseCode())
                                .courseTitle(handOut.getCourseTitle())
                                .level(handOut.getLevel())
                                .summary(handOut.getSummary())
                                .build()
                ).forEach(responseDtoList::add);

        //return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Handout-Page-Number", String.valueOf(pageNum))
                .header("Handout-Page-Size", String.valueOf(pageSize))
                .header("Handout-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<List<HandOutResponseDto>> findHandOutByDepartment(String department, int pageNum, int pageSize) {
        List<HandOut> handOutList = handOutRepository.findByDepartment(department)
                .orElseThrow(()->
                        new ApiException(String.format("Hand-outs with this department: %s, are not available yet", department)));
        List<HandOutResponseDto> responseDtoList = new ArrayList<>();

        //Preparing objects that will be returned back to the user; book has to be active and from the department specified
        handOutList.stream().skip(pageNum - 1).limit(pageSize).filter(x-> x.getActiveStatus().equals(Boolean.TRUE))
                .map(
                        handOut -> HandOutResponseDto.builder()
                                .imageAddress(handOut.getImageAddress())
                                .courseCode(handOut.getCourseCode())
                                .courseTitle(handOut.getCourseTitle())
                                .level(handOut.getLevel())
                                .summary(handOut.getSummary())
                                .build()
                ).forEach(responseDtoList::add);

        //return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Handout-Page-Number", String.valueOf(pageNum))
                .header("Handout-Page-Size", String.valueOf(pageSize))
                .header("Handout-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findHandOutByLevel(HandOutSearchDto searchDto, int pageNum, int pageSize) {
        List<HandOut> handOutList = handOutRepository.findByLevel(searchDto.getLevel())
                .orElseThrow(()->
                        new ApiException(String.format("Hand-outs with this department: %d, are not available yet",
                                searchDto.getLevel())));
        List<HandOutResponseDto> responseDtoList = new ArrayList<>();

        //Preparing objects that will be returned back to the user; book has to be active and from the department specified
        handOutList.stream().skip(pageNum - 1).limit(pageSize).filter(x-> x.getActiveStatus().equals(Boolean.TRUE)
                        && x.getSchoolName().equals(searchDto.getSchoolName()))
                .map(
                        handOut -> HandOutResponseDto.builder()
                                .imageAddress(handOut.getImageAddress())
                                .courseCode(handOut.getCourseCode())
                                .courseTitle(handOut.getCourseTitle())
                                .level(handOut.getLevel())
                                .summary(handOut.getSummary())
                                .build()
                ).forEach(responseDtoList::add);

        //return new ResponseEntity<>(handOutList, HttpStatus.FOUND);
        return ResponseEntity.ok()
                .header("Handout-Page-Number", String.valueOf(pageNum))
                .header("Handout-Page-Size", String.valueOf(pageSize))
                .header("Handout-Total-Count", String.valueOf(responseDtoList.size()))
                .body(responseDtoList);
    }

    @Override
    public ResponseEntity<?> findHandOutByCourseCode(HandOutSearchDto searchDto) {
        HandOut handOut = handOutRepository.findByCourseCode(searchDto.getCourseCode())
                .filter(x-> x.getSchoolName().equals(searchDto.getSchoolName()) && x.getActiveStatus().equals(Boolean.TRUE))
           .orElseThrow(()->
                new ApiException(String.format("Hand-outs with this department: %s, are not available yet",
                        searchDto.getCourseCode())));

        //Preparing objects that will be returned back to the user; book has to be active and from the department specified
        HandOutResponseDto responseDto = HandOutResponseDto.builder()
                .imageAddress(handOut.getImageAddress())
                .courseCode(handOut.getCourseCode())
                .courseTitle(handOut.getCourseTitle())
                .level(handOut.getLevel())
                .summary(handOut.getSummary())
                .build();

                return ResponseEntity.ok(responseDto);
    }

    @Override
    public ResponseEntity<?> findHandOutByCourseTitle(HandOutSearchDto searchDto) {

        HandOut handOut = handOutRepository.findByCourseCode(searchDto.getCourseTitle())
                .orElseThrow(()-> new ApiException(String.format("Hand out with this course title: %s, is not available",
                        searchDto.getCourseTitle())));


        return new ResponseEntity<>(handOut, HttpStatus.FOUND);
    }

    @Override
    public ResponsePojo<HandOutResponseDto> updateHandOut(HandOutUpdateDto handOutUpdateDto) {
        HandOut handOutUpdate = handOutRepository.findByCourseCode(handOutUpdateDto.getCourseCode())
                .filter(x -> x.getActiveStatus().equals(Boolean.TRUE)
                        && x.getSchoolName().equals(handOutUpdateDto.getSchoolName()))
                .orElseThrow(()-> new ApiException(String.format("Hand-out with course code: %s, does not exist",
                        handOutUpdateDto.getCourseCode())));

        //Update handout
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

        //Save changes to repository
        handOutRepository.save(handOutUpdate);

        HandOutResponseDto responseDto = HandOutResponseDto.builder()
                .imageAddress(handOutUpdate.getImageAddress())
                .courseCode(handOutUpdate.getCourseCode())
                .courseTitle(handOutUpdate.getCourseTitle())
                .level(handOutUpdate.getLevel())
                .summary(handOutUpdate.getSummary())
                .build();

        //Response POJO to return
        ResponsePojo<HandOutResponseDto> responsePojo = new ResponsePojo<>();
        responsePojo.setMessage(String.format("Hand-out with course code: %s has been updated successfully",
                handOutUpdateDto.getCourseCode()));
        responsePojo.setData(responseDto);

        return responsePojo;
    }

    @Override
    public ResponseEntity<?> deleteHandOut(HandOutSearchDto searchDto) {

        HandOut handOut = handOutRepository.findByCourseCode(searchDto.getCourseCode())
                .filter(x-> x.getSchoolName().equals(searchDto.getSchoolName()) && x.getActiveStatus().equals(Boolean.TRUE))
                .orElseThrow(()-> new ApiException(String.format("There are no hand-out with course: %s does not exist",
                        searchDto.getCourseCode())));

        //Change the active Status of the book
        handOut.setActiveStatus(false);
        //Save change to repository
        handOutRepository.save(handOut);

        return new ResponseEntity<>("Hand-out was successfully deleted.", HttpStatus.ACCEPTED);
    }
}
