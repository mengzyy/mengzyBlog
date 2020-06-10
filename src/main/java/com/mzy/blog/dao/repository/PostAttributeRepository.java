package com.mzy.blog.dao.repository;


import com.mzy.blog.bean.entity.PostAttribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * Created by langhsu on 2018/9/27.
 */
public interface PostAttributeRepository extends JpaRepository<PostAttribute, Long>, JpaSpecificationExecutor<PostAttribute> {
}
