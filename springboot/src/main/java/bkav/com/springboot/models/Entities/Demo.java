package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Demo", uniqueConstraints = {
        @UniqueConstraint(columnNames = "DemoName")
})
@Getter
@Setter
public class Demo  implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 255)
    @Column(name = "DemoName")
    private String demoName;

    @Column(name = "type")
    private boolean demoType;

    @Column(name = "date")
    private Date createTime = new Date();

    @Column(name = "status")
    private boolean status;

}
