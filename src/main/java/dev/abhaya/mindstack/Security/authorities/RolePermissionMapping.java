package dev.abhaya.mindstack.Security.authorities;


import dev.abhaya.mindstack.model.Role;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RolePermissionMapping {

    public static final Map<Role, Set<Permission>> PERMISSION_MAPPING = Map.of(
            Role.USER, Set.of(
                    Permission.NOTEBOOK_CREATE,
                    Permission.NOTEBOOK_VIEW,
                    Permission.NOTEBOOK_UPDATE,
                    Permission.NOTEBOOK_DELETE,

                    Permission.CHAPTER_CREATE,
                    Permission.CHAPTER_VIEW,
                    Permission.CHAPTER_UPDATE,
                    Permission.CHAPTER_DELETE
            ),
            Role.ADMIN, Set.of(
                    Permission.NOTEBOOK_CREATE,
                    Permission.NOTEBOOK_VIEW,
                    Permission.NOTEBOOK_UPDATE,
                    Permission.NOTEBOOK_DELETE,

                    Permission.CHAPTER_CREATE,
                    Permission.CHAPTER_VIEW,
                    Permission.CHAPTER_UPDATE,
                    Permission.CHAPTER_DELETE,

                    Permission.USER_VIEW,
                    Permission.USER_UPDATE,
                    Permission.USER_DELETE
            )
    );

    public static Set<Permission> getPermissions(Role role){
        return PERMISSION_MAPPING.get(role);
    }
}
