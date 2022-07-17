package com.example.firmaplatform.Controller;

import com.example.firmaplatform.DTO.*;
import com.example.firmaplatform.Repository.XodimRepository;
import com.example.firmaplatform.Service.XodimService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.spi.DirStateFactory;

@RestController
@RequestMapping("/auth")
public class XodimController {
    @Autowired
    XodimService xodimService;

    @PostMapping("/createDirektor")
    public HttpEntity<?> createDirector(@RequestBody DirektorDTO direktorDTO){
        ApiResponse apiResponse=xodimService.createDirector(direktorDTO);
        return ResponseEntity.status(apiResponse.getType()?201:409).body(apiResponse.getMessage());
    }
    @PostMapping("/createManager")
    public HttpEntity<?> createManager(@RequestBody XodimDTO xodimDTO){
        ApiResponse apiResponse =xodimService.createManager(xodimDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }

    @GetMapping("/emailConfirm")
    public HttpEntity<?> email(@RequestParam String userEmail, @RequestParam String userCode){
        ApiResponse apiResponse = xodimService.emailConfirmation(userEmail, userCode);
        return ResponseEntity.status(apiResponse.getType()?201:409).body(apiResponse.getMessage());
    }

    @PostMapping("/updatePassword")
    public HttpEntity<?> updatePassword(@RequestBody LoginDTO loginDTO){
        ApiResponse apiResponse=xodimService.updatePassword(loginDTO);
        return ResponseEntity.status(apiResponse.getType()?200:409).body(apiResponse.getMessage());
    }
    @PostMapping("/getStaff")
    public HttpEntity<?> readStaff(@RequestBody GetXodimDTO getXodimDTO){
        return ResponseEntity.ok(xodimService.readXodim(getXodimDTO));
    }

}
