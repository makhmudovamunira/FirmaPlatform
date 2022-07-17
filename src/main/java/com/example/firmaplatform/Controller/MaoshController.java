package com.example.firmaplatform.Controller;

import com.example.firmaplatform.DTO.ApiResponse;
import com.example.firmaplatform.DTO.GetXodimDTO;
import com.example.firmaplatform.DTO.MaoshDTO;
import com.example.firmaplatform.Service.MaoshService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/salary")
public class MaoshController {
    @Autowired
    MaoshService maoshService;
    @PostMapping("/pay")
    public HttpEntity<?> salaryPay(@RequestBody MaoshDTO salaryDTO){
        ApiResponse apiResponse=maoshService.salaryTo(salaryDTO);
        return ResponseEntity.status(apiResponse.getType()?200:408).body(apiResponse.getMessage());
    }
    @PostMapping("/readPay")
    public HttpEntity<?> readPay(@RequestBody GetXodimDTO getXodimDTO){
        return ResponseEntity.ok(maoshService.salaryList(getXodimDTO));
    }
}
