package com.example.firmaplatform.Repository;

import com.example.firmaplatform.Model.Roles;
import com.example.firmaplatform.Role.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Roles,Integer> {
    Roles findByRoleName(RoleName roleName);
}
