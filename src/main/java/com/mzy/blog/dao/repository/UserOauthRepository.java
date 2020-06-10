/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mzy.blog.dao.repository;

import com.mzy.blog.bean.entity.UserOauth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 第三方开发授权登录
 *
 * @author langhsu on 2015/8/12.
 */
public interface UserOauthRepository extends JpaRepository<UserOauth, Long>, JpaSpecificationExecutor<UserOauth> {
    UserOauth findByAccessToken(String accessToken);
    UserOauth findByOauthUserId(String oauthUserId);
    UserOauth findByUserId(long userId);
}
