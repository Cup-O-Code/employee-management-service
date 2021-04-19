package com.elite.employee.management.controller;

import com.elite.employee.management.common.Status;
import com.elite.employee.management.entity.Tracker;
import com.elite.employee.management.model.EmployeeDTO;
import com.elite.employee.management.service.EmployeeService;
import com.elite.employee.management.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLDataException;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RestController
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    StorageService storageService;

    //    @Transactional
    @PostMapping(value = "/employee", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<Tracker> upload(@RequestParam("file") MultipartFile file, @RequestParam("action") String action) {
        ResponseEntity<Tracker> response = ResponseEntity.badRequest().build();
        if ("upload".equals(action)) {
            Tracker task = storageService.generateTask(file.getOriginalFilename());
            response = ResponseEntity.ok(task);
            CompletableFuture.runAsync(() -> {
                storageService.loadFile(file);
                task.setStatus(Status.COMPLETED.name());
                storageService.updateTask(task);
            });
        }
        return response;
    }

//    @PostMapping
//    ResponseEntity<EmployeeDTO> create(EmployeeDTO employeeDTO) {
//        EmployeeDTO createdEmployee = employeeService.create(employeeDTO);
//        return ResponseEntity.ok(createdEmployee);
//    }

    @GetMapping("/employee/{id}")
    ResponseEntity<EmployeeDTO> find(@PathVariable("id") String id) {
        log.info("find" + id);
        EmployeeDTO emp = employeeService.find(id);
        return ResponseEntity.ok(emp);
    }

    @PutMapping("/employee")
    ResponseEntity<EmployeeDTO> update(EmployeeDTO employeeDTO) {
        log.info("update" + employeeDTO.toString());
        EmployeeDTO emp = null;
        try {
            emp = employeeService.update(employeeDTO);
        } catch (SQLDataException throwables) {
            throwables.printStackTrace();
        }
        return ResponseEntity.ok(emp);
    }

    @DeleteMapping("/employee/{id}")
    ResponseEntity<String> delete(@PathVariable("id") String id) {
        log.info("delete" + id);
        String deleteId = employeeService.delete(id);
        return ResponseEntity.ok(deleteId);
    }

}
