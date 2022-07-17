package com.example.firmaplatform.Repository;

import com.example.firmaplatform.Model.Topshiriq;
import com.example.firmaplatform.Model.Xodim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TopshiriqRepository extends JpaRepository<Topshiriq,Integer> {
    Optional<Topshiriq> findByXodim(Xodim xodim);

}
