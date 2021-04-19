package com.elite.employee.management.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class EmployeeRequestDTO {

    @CsvBindByName(column = "name")
    public String name;

    @CsvBindByName(column = "age")
    public String age;
}
