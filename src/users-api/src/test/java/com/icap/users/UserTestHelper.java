package com.icap.users;

import com.icap.axon.common.domain.Role;
import com.icap.axon.common.domain.User;

import java.util.EnumSet;

import static com.icap.axon.common.domain.Role.ADMIN;
import static com.icap.axon.common.domain.User.ADMIN_ID;

public class UserTestHelper {

    public static final String ADMIN_USERNAME = "admin@umbrella.com";
    public static final String ADMIN_DISPLAY_NAME = "Admin";
    public static final EnumSet<Role> ADMIN_ROLES = EnumSet.of(ADMIN);
    public static final User ADMIN_USER = new User(ADMIN_ID, null,
            ADMIN_USERNAME, ADMIN_DISPLAY_NAME, ADMIN_USERNAME, false, ADMIN_ROLES);

}
