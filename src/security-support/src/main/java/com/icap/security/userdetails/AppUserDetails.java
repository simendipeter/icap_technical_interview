package com.icap.security.userdetails;

import com.icap.axon.common.domain.Role;
import com.icap.axon.common.domain.User;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.springframework.security.core.userdetails.UserDetails;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AppUserDetails implements UserDetails {

    @Getter
    @EqualsAndHashCode.Include
    private final User user;

    @Delegate(types = UserDetails.class)
    private final UserDetails userDetails;

    public AppUserDetails(User user) {
        this.user = user;
        this.userDetails = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password("")
                .disabled(user.isDisabled())
                .roles(user.getRoles().stream().map(Role::toString).toArray(String[]::new))
                .build();
    }

}
