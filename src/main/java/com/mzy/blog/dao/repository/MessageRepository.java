package com.mzy.blog.dao.repository;


import com.mzy.blog.bean.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author langhsu
 */
public interface MessageRepository extends JpaRepository<Message, Long>, JpaSpecificationExecutor<Message> {
    Page<Message> findAllByUserId(Pageable pageable, long userId);

    /**
     * 查询我的未读消息
     *
     * @param userId
     * @return
     */
    int countByUserIdAndStatus(long userId, int status);

    /**
     * 标记我的消息为已读
     */
    @Modifying
    @Query("update Message n set n.status = 1 where n.status = 0 and n.userId = :uid")
    int updateReadedByUserId(@Param("uid") Long uid);

    int deleteByPostId(long postId);
}
