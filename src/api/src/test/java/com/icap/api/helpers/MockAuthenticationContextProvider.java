package com.icap.api.helpers;

import com.icap.axon.common.domain.Role;
import com.icap.axon.common.domain.User;
import com.icap.security.AuthenticationContextProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collection;
import java.util.EnumSet;
import java.util.stream.Collectors;

import static com.icap.users.UserTestHelper.ADMIN_USER;
import static java.util.UUID.randomUUID;

public class MockAuthenticationContextProvider extends AuthenticationContextProvider {

    static ThreadLocal<User> userHolder = new ThreadLocal<>();

    @Override
    public User getUser() {
        return captureUser();
    }

    public static User getAuthUser() {
        return captureUser();
    }

    private static User captureUser() {
        User user = userHolder.get();
        if (user != null) {
            return user;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }
        if (ADMIN_USER.getUsername().equals(authentication.getName())) {
           user = ADMIN_USER;
        } else {
           user = new User(randomUUID(), randomUUID(),
                    authentication.getName(), authentication.getName(), authentication.getName(),
                    false, convertTolRoles(authentication.getAuthorities()));
        }
        userHolder.set(user);
        return user;
    }

    private static EnumSet<Role> convertTolRoles(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::toString)
                .map(e -> StringUtils.removeStart(e, "ROLE_"))
                .map(Role::valueOf)
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));
    }
}
