package com.example.firmaplatform.Repository;

import com.example.firmaplatform.Model.Xodim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface XodimRepository extends JpaRepository<Xodim,Integer> {
    Optional<Xodim> findByEmailAndEmailCode(String email,String emailCode);
    Optional<Xodim> findByEmail(String email);
}
