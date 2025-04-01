package com.owasptopten.secure.security.authorization;

import com.owasptopten.secure.userdetails.domain.User;
import com.owasptopten.secure.userdetails.dto.UserDetailDto;
import com.owasptopten.secure.userdetails.enums.Roles;
import com.owasptopten.secure.userdetails.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final UserDetailService userDetailService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if (authentication == null || !(permission instanceof Roles)
                || !(targetId instanceof Long)) {
            return false;
        }

        return hasAccessToEntity((User) authentication.getPrincipal(), targetType, (Long) targetId, (Roles) permission);
    }

    private boolean hasAccessToEntity(User loggedInUser, String targetType, Long targetId, Roles permission) {

        if (targetType.equals("User")) {
            Optional<UserDetailDto> optTargetUserDetail = userDetailService.getUserDetail(targetId);
            if (optTargetUserDetail.isEmpty()) {
                return false;
            }
            UserDetailDto targetUser = optTargetUserDetail.get();
            List<Roles> roles = loggedInUser.getRoles();
            return roles.contains(Roles.ADMIN)
                    || (roles.contains(permission) && loggedInUser.getUsername().equals(targetUser.username()));
        }
        return false;
    }
}
