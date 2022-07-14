package bkav.com.springboot.security.jwt;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    @Override
    public boolean hasPermission(Authentication authentication, Object accessType, Object permission) {
        try {
            if (authentication != null && accessType instanceof String) {
                List<String> roles = new ArrayList<>();
                List<String> permissions = new ArrayList<>();
                authentication.getAuthorities().stream().forEach(r -> {
                    String rolePer = String.valueOf(r);
                    roles.add(rolePer.split("_")[0]);
                    permissions.add(rolePer.split("_")[1]);
                });
                if (checkRole(String.valueOf(accessType), roles)) {
                    return checkPermission(String.valueOf(permission), permissions);
                }
                return false;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean checkPermission(String name, List<String> roles) {
        return roles.stream().anyMatch(user -> user.equalsIgnoreCase(name));
    }

    private boolean checkRole(String name, List<String> roles) {
        return roles.stream().anyMatch(user -> user.equalsIgnoreCase(name));
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable serializable, String targetType,
                                 Object permission) {
        return false;
    }
}
