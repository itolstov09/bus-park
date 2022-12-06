package dev.tolstov.buspark.controller;

import dev.tolstov.buspark.json_reader.JsonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @Autowired
    JsonService jsonService;

    @GetMapping("/rpc/loadFromJSON")
    public void loadFromJSON() {
        jsonService.loadAndSaveToDB();
    }

}
