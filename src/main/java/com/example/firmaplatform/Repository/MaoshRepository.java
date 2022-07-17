package com.example.firmaplatform.Repository;

import com.example.firmaplatform.Model.Maosh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaoshRepository extends JpaRepository<Maosh,Integer> {
}
