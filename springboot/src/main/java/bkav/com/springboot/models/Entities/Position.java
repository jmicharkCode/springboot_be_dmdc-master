package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Setter
@Getter
@Entity

@Table(name = "position", uniqueConstraints = {
        @UniqueConstraint(columnNames = "PositionId")
})

public class Position implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PositionId", unique = true)
    @NotNull
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long positionId;

    @NotNull
    @Size(max = 500)
    @Column(name = "PositionName")
    private String positionName;

    @Column(name = "PriorityLevel")
    @NotNull
    private int priorityLevel;  

    @NotNull
    @Column(name = "IsApproved")
    private boolean isApproved;

    @Column(name = "CanSetDateOverdue")
    private Boolean canSetDateOverdue;

    @Column(name = "ApiId")
    private Integer apiId;

    @Size(max = 200)
    @Column(name = "PositionIdHRM")
    private String positionIdHRM;
}
