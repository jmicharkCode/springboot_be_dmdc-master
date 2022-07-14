package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ActivityLog")
@Getter
@Setter
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private int id;

    @Column(name = "ActivityType")
    private int activityType;

    @Column(name = "Ip")
    private String ip;

    @Column(name = "UserId")
    private Long userId;

    @Column(name = "Username")
    private String username;

    @Column(name = "Content")
    private String content;

    @Column(name = "CreateTime")
    private Date createTime = new Date();
}
