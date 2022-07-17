package com.example.firmaplatform.Service;

import com.example.firmaplatform.DTO.ApiResponse;
import com.example.firmaplatform.DTO.TopshiriqDTO;
import com.example.firmaplatform.Model.Roles;
import com.example.firmaplatform.Model.Topshiriq;
import com.example.firmaplatform.Model.Xodim;
import com.example.firmaplatform.Repository.RoleRepository;
import com.example.firmaplatform.Repository.TopshiriqRepository;
import com.example.firmaplatform.Repository.XodimRepository;
import com.example.firmaplatform.Role.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TopshiriqService {
    @Autowired
    TopshiriqRepository topshiriqRepository;
    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    XodimRepository xodimRepository;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse createTask(TopshiriqDTO topshiriqDTO){
        Optional<Xodim> optionalStaff = xodimRepository.findById(topshiriqDTO.getStaffId());
        Optional<Roles> optionalRoles=roleRepository.findById(topshiriqDTO.getMaker());
        if (optionalStaff.isPresent()){
            if (optionalRoles.get().getRoleName().equals(RoleName.DIREKTOR) || optionalRoles.get().getRoleName().equals(RoleName.MANAGER)){
                Topshiriq topshiriq=new Topshiriq();
                topshiriq.setTaskName(topshiriqDTO.getTaskName());
                topshiriq.setTaskInfo(topshiriqDTO.getTaskInfo());
                topshiriq.setTaskExpDate(topshiriqDTO.getTaskExpDate());
                topshiriq.setXodim(optionalStaff.get());
                if (optionalRoles.get().getRoleName().equals(RoleName.DIREKTOR) && !optionalStaff.get().getRoles().equals(RoleName.DIREKTOR) || optionalRoles.get().getRoleName().equals(RoleName.MANAGER) && (!optionalStaff.get().getRoles().equals(RoleName.DIREKTOR) && !optionalStaff.get().getRoles().equals(RoleName.MANAGER))){
                    topshiriqRepository.save(topshiriq);
                    emailVerification(optionalRoles.get().getRoleName().toString(), optionalStaff.get().getEmail(), topshiriqDTO.getTaskName(), topshiriqDTO.getTaskInfo());
                    return new ApiResponse("Successfully send task", true);
                }
                return new ApiResponse(optionalRoles.get().getRoleName()+" You are not allowed to add task!",false);
            }
            return new ApiResponse("Not add", true);
        }
        return new ApiResponse("Not staff id",false);
    }
    public ApiResponse readTask(Integer id){
        Optional<Xodim> optionalXodim = xodimRepository.findById(id);
        if (optionalXodim.isPresent()){
            Optional<Topshiriq> optionalTasks=topshiriqRepository.findByXodim(optionalXodim.get());
            return optionalTasks.map(tasks -> new ApiResponse(tasks.getTaskName() + "\n" + tasks.getTaskInfo() + "\n" + tasks.getTaskExpDate(), true)).orElseGet(() -> new ApiResponse("this staff has no tasks", false));
        }
        return new ApiResponse("staff not found", false);
    }
    public boolean emailVerification(String fromEmail, String userEmail,String taskName, String taskInfo){
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(fromEmail);
            mailMessage.setTo(userEmail);
            mailMessage.setSubject("Task: "+taskName);
            mailMessage.setText(taskInfo);
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
}
