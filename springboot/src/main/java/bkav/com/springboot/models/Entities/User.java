package bkav.com.springboot.models.Entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "Username"),
                @UniqueConstraint(columnNames = "Email")
        })
@Getter
@Setter
public class User extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 6, max = 20)
    @Column(name = "Username")
    private String username;

    @NotBlank
    @Size(min = 6, max = 50)
    @Email
    @Column(name = "Email")
    private String email;

    @NotBlank
    @Size(min = 6, max = 120)
    @Column(name = "Password")
    private String password;

    @Column(name = "RememberPassword")
    private int rememberPassword;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "UserRoleGroup",
            joinColumns = @JoinColumn(name = "UserId"),
            inverseJoinColumns = @JoinColumn(name = "RoleId"))
    private Set<RoleGroup> roles = new HashSet<>();

    @Column(name = "Status")
    private boolean status;

    @Column(name = "IsDelete")
    private boolean delete;

    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
