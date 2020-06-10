package com.mzy.blog.service;


import com.mzy.blog.bean.data.FavoriteVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 收藏记录
 * @author langhsu
 */
public interface FavoriteService {
    /**
     * 查询用户收藏记录
     * @param pageable
     * @param userId
     * @return
     */
    Page<FavoriteVO> pagingByUserId(Pageable pageable, long userId);

    void add(long userId, long postId);
    void delete(long userId, long postId);
    void deleteByPostId(long postId);
}
