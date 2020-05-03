package com.upgrad.videohoster.service.business;


import com.upgrad.videohoster.service.dao.UserDao;
import com.upgrad.videohoster.service.entity.UserAuthTokenEntity;
import com.upgrad.videohoster.service.entity.UserEntity;
import com.upgrad.videohoster.service.exception.AuthenticationFailedException;
import org.hibernate.type.descriptor.java.ZonedDateTimeJavaDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider CryptographyProvider;

    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthTokenEntity authenticate(final String username, final String password) throws AuthenticationFailedException {
        UserEntity userEntity = userDao.getUserByEmail(username);

        final String encryptedPassword = CryptographyProvider.encrypt(password, userEntity.getSalt());
        if(encryptedPassword.equals(userEntity.getPassword())) {
            UserAuthTokenEntity userAuth=new UserAuthTokenEntity();
            userAuth.setUser(userEntity);
            userAuth.setAccessToken(new JwtTokenProvider("thisissecretkey").generateToken(userEntity.getUuid(),  ZonedDateTime.now(),  ZonedDateTime.now().plusHours(3)));
            userAuth.setExpiresAt(ZonedDateTime.now().plusHours(3));
            userAuth.setLoginAt(ZonedDateTime.now());
            userDao.createAuthToken(userAuth);
           //3 hours sessions
            return userAuth;
        }
        return null;
    }
}


