package com.example.firmaplatform.Model;

import com.example.firmaplatform.Role.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Topshiriq {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String taskName;
    @Column(nullable = false)
    private String taskInfo;
    @Column(nullable = false)
    private String taskExpDate;

    @OneToOne
    private Xodim xodim;


}
