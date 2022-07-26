package bkav.com.springboot.models.Entities;

<<<<<<< Updated upstream
import com.fasterxml.jackson.annotation.JsonFormat;
=======
>>>>>>> Stashed changes
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
<<<<<<< Updated upstream
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
=======
import javax.validation.constraints.NotNull;
import java.io.Serializable;
>>>>>>> Stashed changes

@Setter
@Getter
@Entity
@NoArgsConstructor

<<<<<<< Updated upstream
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

    @Column(name = "Options")
    private String options;

    @Column(name = "IsStatistics")
    private int isStatistics;

    @Column(name = "ApiId")
    private Long apiId;

    @Column(name = "JobTitleIdHRM")
    private String jobTitleIdHRM;
=======
@Table(name = "jobtitles")
public class JobDetail extends  BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotNull
    @Column(name = "jobTitleName")
    private String name;

    @NotNull
    private int isApproved;

    @NotNull
    private int levelPriority;

    @NotNull
    private int isClerical;

    @NotNull
    private int canGetDocumentCome;

    private String options;

    private int isStatistics;

    private int apiId;

    private String jobTitleIdHRM;

>>>>>>> Stashed changes
}
