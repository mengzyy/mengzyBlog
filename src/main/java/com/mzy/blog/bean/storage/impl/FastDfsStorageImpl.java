package com.mzy.blog.bean.storage.impl;

import com.mzy.blog.utils.fastDfsFileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: mengzyBlog
 * @author: mengzy 18306299232@163.com
 * @create: 2020-06-10 23:37
 **/
@Slf4j
@Component
public class FastDfsStorageImpl extends AbstractStorage {

    @Autowired
    fastDfsFileUtil fastDfsFileUtil;


    @Override
    public void deleteFile(String storePath) {
        fastDfsFileUtil.deleteFile(storePath);
    }

    @Override
    public String writeToStore(byte[] bytes, String pathAndFileName) throws Exception {
        return fastDfsFileUtil.uploadFileByBytes(bytes, "jpg");


    }
}
