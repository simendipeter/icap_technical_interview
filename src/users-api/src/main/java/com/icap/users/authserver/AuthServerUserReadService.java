package com.icap.users.authserver;

public interface AuthServerUserReadService {

    AuthServerUser getByUsername(String username);

}
