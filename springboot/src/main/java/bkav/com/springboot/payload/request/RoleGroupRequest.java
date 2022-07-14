package bkav.com.springboot.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Set;

public class RoleGroupRequest {

    @NotBlank
    @Size(min = 6, max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String description;

    private Set<Integer> permissions;

    private Set<Integer> pages;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Integer> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Integer> permissions) {
        this.permissions = permissions;
    }

    public Set<Integer> getPages() {
        return pages;
    }

    public void setPages(Set<Integer> pages) {
        this.pages = pages;
    }
}
