package com.icap.users.services;

import com.icap.axon.common.domain.User;
import com.icap.users.persistence.PersistableUser;
import com.icap.users.persistence.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class DefaultUsersReadService implements UsersReadService {

    private final UsersRepository usersRepository;

    @Autowired
    public DefaultUsersReadService(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }

    @Override
    public User getById(UUID id) {
        return convert(usersRepository.findById(id).orElseThrow());
    }

    @Override
    public List<User> getUsers() {
        return usersRepository.findAll().stream()
                .map(this::convert)
                .collect(toList());
    }

    @Override
    public List<User> getUsersForOrganization(UUID organizationId) {
        return usersRepository.findByOrganizationId(organizationId).stream()
                .map(this::convert)
                .collect(toList());
    }

    @Override
    public boolean exists(UUID userId) {
        return usersRepository.existsById(userId);
    }

    @Override
    public User getUserByUsername(String username) {
        return convert(usersRepository.findByEmailIgnoreCase(username).orElseThrow());
    }

    @Override
    public boolean hasUserWithEmail(String email) {
        return usersRepository.findByEmailIgnoreCase(email).isPresent();
    }

    private User convert(PersistableUser persistableUser) {
        return new User(persistableUser.getId(), persistableUser.getOrganizationId(), persistableUser.getUsername(),
                persistableUser.getDisplayName(), persistableUser.getEmail(),
                persistableUser.isDisabled(), persistableUser.getRoles());
    }
}
