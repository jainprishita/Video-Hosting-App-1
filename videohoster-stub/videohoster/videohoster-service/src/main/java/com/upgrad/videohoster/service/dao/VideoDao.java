package com.upgrad.videohoster.service.dao;

import com.upgrad.videohoster.service.entity.UserEntity;
import com.upgrad.videohoster.service.entity.VideoEntity;
import com.upgrad.videohoster.service.entity.UserAuthTokenEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
public class VideoDao {

    @PersistenceContext
    private EntityManager entityManager;

    public VideoEntity createVideo(VideoEntity videoEntity) {
        entityManager.persist(videoEntity);
        return videoEntity;
    }

    public UserAuthTokenEntity getUserAuthToken(final String accesstoken) {
        Query q=entityManager.createNamedQuery("userAuthTokenByAccessToken");
        q.setParameter("accessToken", accesstoken);
        List<UserAuthTokenEntity> list=q.getResultList();
        if(list.size()>=1)
        return list.get(0);
         return null;
    }

    public VideoEntity getVideo(final String videoUuid) {
        Query q=entityManager.createNamedQuery("VideoEntityByUuid");
        q.setParameter("uuid", videoUuid);
        List<VideoEntity> list=q.getResultList();
        if(list.size()>=1)
            return list.get(0);
        return null;
    }

    public VideoEntity getVideoById(final long Id) {
        return  null;
    }

    public VideoEntity updateVideo(final VideoEntity videoEntity) {
        return entityManager.merge(videoEntity);
    }
}
