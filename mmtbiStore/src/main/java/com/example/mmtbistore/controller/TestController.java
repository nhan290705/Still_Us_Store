package com.example.mmtbistore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Test Controller", description = "API dùng để kiểm thử Swagger")
public class TestController {

    @GetMapping("/hello")
    @Operation(summary = "Chào mừng", description = "Trả về chuỗi text đơn giản")
    public String sayHello(@RequestParam(defaultValue = "World") String name) {
        return "Xin chào, " + name + "!";
    }

    @PostMapping("/echo")
    @Operation(summary = "Nhận và trả về dữ liệu")
    public String echoData(@RequestBody String data) {
        return "Dữ liệu bạn gửi: " + data;
    }
}
