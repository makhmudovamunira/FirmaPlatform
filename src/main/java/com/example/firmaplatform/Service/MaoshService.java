package com.example.firmaplatform.Service;

import com.example.firmaplatform.DTO.ApiResponse;
import com.example.firmaplatform.DTO.GetXodimDTO;
import com.example.firmaplatform.DTO.MaoshDTO;
import com.example.firmaplatform.Model.Maosh;
import com.example.firmaplatform.Model.Roles;
import com.example.firmaplatform.Model.Xodim;
import com.example.firmaplatform.Repository.MaoshRepository;
import com.example.firmaplatform.Repository.RoleRepository;
import com.example.firmaplatform.Repository.XodimRepository;
import com.example.firmaplatform.Role.RoleName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;
import java.util.Optional;

@Service
public class MaoshService {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    MaoshRepository maoshRepository;
    @Autowired
    XodimRepository xodimRepository;
    public ApiResponse salaryTo(MaoshDTO maoshDTO){
        Optional<Xodim> optionalXodim = xodimRepository.findByEmail(maoshDTO.getToWho());
        if (optionalXodim.get().getRoles().getRoleName().equals(RoleName.DIREKTOR) || optionalXodim.get().getRoles().getRoleName().equals(RoleName.MANAGER)) {
            Optional<Xodim> optional = xodimRepository.findByEmail(maoshDTO.getToWho());
            if (optional.isPresent()){
                Maosh maosh=new Maosh();
                maosh.setSum(maoshDTO.getSum());
                maosh.setToWho(optional.get());
                maoshRepository.save(maosh);
                return new ApiResponse("Payment was Succesfully", true);
            }
            return new ApiResponse("Not found staff", false);
        }
        return new ApiResponse("No payment add", false);
    }

    public List<Maosh> salaryList(GetXodimDTO getXodimDTO){
        Optional<Roles> optionalRoles=roleRepository.findById(getXodimDTO.getId());
        if (optionalRoles.get().getRoleName().equals(RoleName.DIREKTOR) || optionalRoles.get().getRoleName().equals(RoleName.MANAGER)){
            return maoshRepository.findAll();
        }
        return null;
    }

}
