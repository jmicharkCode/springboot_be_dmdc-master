package bkav.com.springboot.models.Entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "RoleGroup", uniqueConstraints = {
        @UniqueConstraint(columnNames = "Name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleGroup extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotBlank
    @Size(min = 6, max = 255)
    @Column(name = "Name", length = 20)
    private String name;

    @NotBlank
    @Column(name = "Description")
    @Size(max = 255)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "RoleGroupPermission",
            joinColumns = @JoinColumn(name = "RoleId"),
            inverseJoinColumns = @JoinColumn(name = "PermissionId"))
    private Set<Permission> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "RoleGroupPage",
            joinColumns = @JoinColumn(name = "RoleId"),
            inverseJoinColumns = @JoinColumn(name = "PageId"))
    private Set<Page> pages = new HashSet<>();

    @Column(name = "Status")
    private boolean status;

    @Column(name = "IsDelete")
    private boolean delete;
}