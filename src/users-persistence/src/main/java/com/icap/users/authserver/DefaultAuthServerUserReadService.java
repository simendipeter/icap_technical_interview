package com.icap.users.authserver;

import com.icap.users.persistence.UsersRepository;
import org.springframework.stereotype.Service;

@Service
public class DefaultAuthServerUserReadService implements AuthServerUserReadService {

    private final UsersRepository usersRepository;

    public DefaultAuthServerUserReadService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public AuthServerUser getByUsername(String username) {
        var persistableUser = usersRepository.findByUsernameIgnoreCase(username).orElseThrow();
        return new AuthServerUser(persistableUser.getUsername(), persistableUser.getEncodedPassword(),
                persistableUser.isDisabled());
    }
}
