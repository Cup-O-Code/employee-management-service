package com.elite.employee.management.service;

import com.elite.employee.management.common.Status;
import com.elite.employee.management.entity.Tracker;
import com.elite.employee.management.model.EmployeeDTO;
import com.elite.employee.management.model.EmployeeRequestDTO;
import com.elite.employee.management.repository.TrackerRepository;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;

@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    @Autowired
    private TrackerRepository trackerRepository;

    @Autowired
    EmployeeService employeeService;

    @Override
    public void loadFile(MultipartFile file) {
        try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CsvToBean<EmployeeRequestDTO> employeeDTOCsvToBean = new CsvToBeanBuilder(reader)
                    .withType(EmployeeRequestDTO.class)
                    .withSeparator('|')
                    .withIgnoreEmptyLine(true)
                    .withIgnoreQuotations(true)
                    .build();

            Iterator<EmployeeRequestDTO> employeeDTOSIterator = employeeDTOCsvToBean.parse().iterator();

            employeeDTOSIterator.forEachRemaining(employeeDTO -> {
                EmployeeDTO dto = new EmployeeDTO();
                dto.setName(employeeDTO.getName());
                dto.setAge(Long.valueOf(employeeDTO.getAge()));
                employeeService.create(dto);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Tracker generateTask(String originalFilename) {
        Tracker tracker = new Tracker();
        tracker.setFileName("");
        tracker.setStatus(Status.ONGOING.name());
        return trackerRepository.save(tracker);
    }

    @Override
    public Tracker updateTask(Tracker tracker) {
        tracker.setStatus(Status.COMPLETED.name());
        return trackerRepository.save(tracker);
    }
}
