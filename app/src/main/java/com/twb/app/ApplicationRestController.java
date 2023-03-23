package com.twb.app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApplicationRestController {

    @RequestMapping("/")
    public String index() {
        return "Hello, World!\n";
    }

}
