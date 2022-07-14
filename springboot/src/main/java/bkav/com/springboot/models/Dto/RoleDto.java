package bkav.com.springboot.models.Dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleDto {
    private int id;
    private String name;
    private List<String> permissions;
    private List<String> pages;
}
