package com.upgrad.videohoster.service.business;

import com.upgrad.videohoster.service.dao.VideoDao;
import com.upgrad.videohoster.service.entity.VideoEntity;
import com.upgrad.videohoster.service.entity.UserAuthTokenEntity;
import com.upgrad.videohoster.service.exception.UploadFailedException;
import com.upgrad.videohoster.service.exception.UserNotSignedInException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VideoUploadService {

    @Autowired
    private VideoDao videoDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public VideoEntity upload(VideoEntity videoEntity, final String authorizationToken) throws UploadFailedException {
        UserAuthTokenEntity userAuthTokenEntity = videoDao.getUserAuthToken(authorizationToken);
        if(userAuthTokenEntity==null)
            throw new UploadFailedException("VUF-001", "Upload Failure");
        videoEntity.setUser_id(userAuthTokenEntity.getUser());
        videoDao.createVideo(videoEntity);
        return videoEntity;
    }
}
