package com.example.firmaplatform.Service;

import com.example.firmaplatform.DTO.*;
import com.example.firmaplatform.Model.Roles;
import com.example.firmaplatform.Model.Xodim;
import com.example.firmaplatform.Repository.RoleRepository;
import com.example.firmaplatform.Repository.TopshiriqRepository;
import com.example.firmaplatform.Repository.XodimRepository;
import com.example.firmaplatform.Role.RoleName;
import com.example.firmaplatform.WebToken.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class XodimService implements UserDetailsService {

    @Autowired
    XodimRepository xodimRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    Token token;
    @Autowired
    TopshiriqRepository topshiriqRepository;

    public ApiResponse createDirector(DirektorDTO direktorDTO){
        try{
            Xodim xodim=new Xodim();
            xodim.setFirstName(direktorDTO.getFirstName());
            xodim.setLastName(direktorDTO.getLastName());
            xodim.setEmail(direktorDTO.getEmail());
            xodim.setPhoneNumber(direktorDTO.getPhoneNumber());
            xodim.setPassword(direktorDTO.getPassword());
            xodim.setRoles(roleRepository.findByRoleName(RoleName.DIREKTOR));
            xodim.setType(false);
            xodim.setEnabled(true);
            xodimRepository.save(xodim);
            return new ApiResponse("Direktor muvaffaqiyatli joylandi",true);
        }
        catch (Exception e){
            e.printStackTrace();
            return new ApiResponse("Allqachon ro'yxatdan o'tgan",true);
        }
    }

    public ApiResponse createManager(XodimDTO xodimDTO){
        Optional<Xodim> optionalXodim=xodimRepository.findById(xodimDTO.getStaffId());
        Optional<Roles> optionalRoles=roleRepository.findById(xodimDTO.getTypeRole());
        if(optionalXodim.isPresent()){
            if (optionalXodim.get().getRoles().getRoleName().equals(RoleName.DIREKTOR) || optionalXodim.get().getRoles().getRoleName().equals(RoleName.MANAGER)){
                Xodim xodim=new Xodim();
                xodim.setFirstName(xodimDTO.getFirstName());
                xodim.setLastName(xodim.getLastName());
                xodim.setPhoneNumber(xodimDTO.getPhoneNumber());
                xodim.setEmail(xodimDTO.getEmail());
                xodim.setPassword(passwordEncoder.encode(xodim.getPassword()));
                xodim.setRoles(roleRepository.findByRoleName(RoleName.DIREKTOR));
                xodim.setEmailCode(UUID.randomUUID().toString());
                if ((optionalRoles.get().getRoleName().equals(RoleName.DIREKTOR) || optionalRoles.get().getRoleName().equals(RoleName.MANAGER)) && (optionalRoles.get().getRoleName().equals(RoleName.USER) || optionalRoles.get().getRoleName().equals(RoleName.MANAGER))){
                    return  new ApiResponse(optionalRoles.get().getRoleName()+" qo'shishga ruxsat berilmagan"+optionalXodim.get().getRoles().getRoleName().toString(),false);
                }
                xodim.setRoles(roleRepository.findByRoleName(optionalRoles.get().getRoleName()));
                xodim.setType(false);
                boolean verification=emailVerification(xodim.getEmail(),xodim.getEmailCode());
                if (verification){
                    xodimRepository.save(xodim);
                    return new ApiResponse("Xodim muvaffaqiyatli ro'yhatdan o'tkazildi,Biz email verifcaion codni yubordikiltimos oling",true);

                }
                else return new ApiResponse("Not available", false);

            }
            return new ApiResponse("No add a staff", false);
        }
        return new ApiResponse("No login", false);
    }

    private boolean emailVerification(String useremail, String userCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("sirliboyevuz@gmail.com");
            mailMessage.setTo(useremail);
            mailMessage.setSubject("Email verification");
            mailMessage.setText("<a href='http://localhost:8080/auth/emailConfirm?userCode="+userCode+"&userEmail="+useremail+"'>Confirm email</a>");
            javaMailSender.send(mailMessage);
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public List<Xodim> readXodim(GetXodimDTO getXodimDTO){
        Optional<Roles> optionalRoles=roleRepository.findById(getXodimDTO.getId());
        if (optionalRoles.get().getRoleName().equals(RoleName.DIREKTOR) || optionalRoles.get().getRoleName().equals(RoleName.MANAGER)){
            return xodimRepository.findAll();
        }
        return null;
    }

    public ApiResponse updatePassword(LoginDTO loginDTO){
        Optional<Xodim> byEmail = xodimRepository.findByEmail(loginDTO.getLogin());
        if(byEmail.isPresent()){
            Xodim xodim=byEmail.get();
            xodim.setPassword(loginDTO.getPassword());
            return new ApiResponse("Password successfully updated", true);
        }
        return new ApiResponse("Not found username",false);
    }

    public ApiResponse emailConfirmation(String userEmail,String userCode){
        Optional<Xodim> optionalXodim = xodimRepository.findByEmailAndEmailCode(userEmail, userCode);
        if (optionalXodim.isPresent()){
            Xodim xodim=optionalXodim.get();
            xodim.setEnabled(true);
            xodim.setPassword(null);
            xodimRepository.save(xodim);
            String token1=token.getToken("makhmudovamuni007@gmail.com",xodim.getRoles());
            System.out.println(token1);
            return  new ApiResponse("Successfully confirmation",true);
        }
        return new ApiResponse("Already actived", false);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return xodimRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username+"Bunday username topilmadi"));
    }

}
