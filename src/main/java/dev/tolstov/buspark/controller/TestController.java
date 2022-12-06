package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.json_reader.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class TestController {
    @Autowired
    JsonService jsonService;


    @PostMapping("/loadFromJSON")
    public ResponseEntity<HttpStatus> handleFileUpload(
            @RequestParam("file") MultipartFile file
    ) {
        jsonService.loadAndSaveToDB(file);
        return ResponseEntity.ok().build();
    }

}
