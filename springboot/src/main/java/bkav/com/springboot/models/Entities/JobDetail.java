package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Setter
@Getter
@Entity
@NoArgsConstructor

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

}
