package com.elite.employee.management.service;

import com.elite.employee.management.entity.Tracker;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

    void loadFile(MultipartFile file);

    Tracker generateTask(String originalFilename);

    Tracker updateTask(Tracker tracker);

}
