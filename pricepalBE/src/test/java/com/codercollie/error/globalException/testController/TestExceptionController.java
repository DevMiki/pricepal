package com.codercollie.error.globalException.testController;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestExceptionController {

    @GetMapping("/api/test")
    public String kaBoom() throws Exception {
        throw new Exception("Ka-boom!");
    }
}
