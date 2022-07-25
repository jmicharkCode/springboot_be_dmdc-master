<<<<<<< Updated upstream
package bkav.com.springboot.models.Entities;public class Department {
=======
package bkav.com.springboot.models.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Table(name = "department")
public class Department implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DepartmentId")
    private Long id;

    @Column(name = "ParentId")
    @ColumnDefault("null")
    private Long parentId;

    @Column(name = "DepartmentName")
    @Size(max = 500)
    @NotNull
    private String departmentName;

    @Column(name = "IsActivated", columnDefinition = "BIT")
    @NotNull
    private boolean isActivated;

    @ColumnDefault("null")
    @Size(max = 64)
    @Column(name = "DepartmentIdExt")
    private String departmentIdExt;

    @Column(name = "DepartmentPath")
    @ColumnDefault("null")
    @Size(max = 1000)
    private String departmentPath;

    // Để order k tạo ra bảng được
    @Column(name = "Orders")
    @NotNull
    private int order;

    @Column(name = "Level")
    @NotNull
    private int level;

    @Column(name = "CreatedByUserId")
    @ColumnDefault("null")
    @CreatedBy
    private Integer createdByUserId;

    @Column(name = "HasReceiveWarning", columnDefinition = "BIT")
    @NotNull
    private int hasReceiveWarning;

    @ColumnDefault("null")
    @Column(name = "Emails")
    @Size(max = 200)
    private String emails;

    @Column(name = "HasCalendar", columnDefinition = "BIT")
    @NotNull
    private int hasCalendar;

    @Column(name = "Domain")
    @ColumnDefault("null")
    @Size(max = 200)
    private String domain;

    @ColumnDefault("null")
    @Column(name = "ApiId")
    private Long apiId;

    @ColumnDefault("null")
    @Column(name = "DepartmentIdHRM")
    @Size(max = 200)
    private String departmentIdHRM;

    @ColumnDefault("null")
    @Column(name = "ParentIdHRM")
    @Size(max = 200)
    private String parentIdHRM;


    @ColumnDefault("null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "CreatedOnDate")
    @CreatedDate
    private Date createTime;

    @ColumnDefault("null")
    @LastModifiedBy
    @Column(name = "LastModifiedByUserId")
    private Integer lastModifiedByUserId;

    @ColumnDefault("null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "LastModifiedOnDate")
    @LastModifiedDate
    private Date lastModifiedOnDate;

    @Version
    private Date version;
>>>>>>> Stashed changes
}
