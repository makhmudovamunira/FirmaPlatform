package com.example.firmaplatform.Controller;

import com.example.firmaplatform.DTO.ApiResponse;
import com.example.firmaplatform.DTO.TopshiriqDTO;
import com.example.firmaplatform.Repository.TopshiriqRepository;
import com.example.firmaplatform.Service.TopshiriqService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class TopshiriqController {
    @Autowired
    TopshiriqService topshiriqService;
    @Autowired
    TopshiriqRepository topshiriqRepository;

    @PostMapping("/createTask")
    public HttpEntity<?> createTask(@RequestBody TopshiriqDTO topshiriqDTO){
        ApiResponse apiResponse = topshiriqService.createTask(topshiriqDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }
    @GetMapping("/getTask/{id}")
    public HttpEntity<?> readTask(@PathVariable Integer id){
        ApiResponse apiResponse=topshiriqService.readTask(id);
        return ResponseEntity.status(apiResponse.getType()?201:409).body(apiResponse.getMessage());
    }
    @GetMapping("/getAllTask")
    public HttpEntity<?> readAllTask(){
        return ResponseEntity.ok(topshiriqRepository.findAll());
    }
}