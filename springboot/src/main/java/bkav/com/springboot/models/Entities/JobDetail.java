package bkav.com.springboot.models.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Setter
@Getter
@Entity
@NoArgsConstructor

@Table(name = "jobtitles", uniqueConstraints = {@UniqueConstraint(columnNames = "JobTitlesId")})
public class JobDetail  implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "JobTitlesId")
    @NotNull
    private Long id;

    @NotNull
    @Column(name = "JobTitleName")
    @Size(max = 500)
    private String name;

    @NotNull
    @Column(name = "IsApproved")
    private boolean isApproved;

    @NotNull
    @Column(name = "PriorityLevel")
    private int levelPriority;

    @NotNull
    @Column(name = "IsClercial")
    private int isClerical;

    @NotNull
    @Column(name = "CanGetDocumentCome")
    private int canGetDocumentCome;

    @ColumnDefault("null")
    @Column(name = "Options")
    private String options;

    @Column(name = "IsStatistics", columnDefinition = "BIT")
    private Boolean isStatistics;

    @ColumnDefault("null")
    @Column(name = "ApiId")
    private Long apiId;

    @ColumnDefault("null")
    @Column(name = "JobTitleIdHRM")
    private String jobTitleIdHRM;
}
