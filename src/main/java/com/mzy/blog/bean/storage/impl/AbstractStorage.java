/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2019 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mzy.blog.bean.storage.impl;

import com.mzy.blog.bean.storage.Storage;
import com.mzy.blog.bean.data.MtonsException;
import com.mzy.blog.bean.entity.Resource;
import com.mzy.blog.config.SiteOptions;
import com.mzy.blog.dao.repository.ResourceRepository;
import com.mzy.blog.utils.FileKit;
import com.mzy.blog.utils.FilePathUtils;
import com.mzy.blog.utils.ImageUtils;
import com.mzy.blog.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

/**
 * @author langhsu
 * @since 3.0
 */
@Slf4j
public abstract class AbstractStorage implements Storage {
    @Autowired
    protected SiteOptions options;
    @Autowired
    protected ResourceRepository resourceRepository;

    protected void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new MtonsException("文件不能为空");
        }

        if (!FileKit.checkFileType(file.getOriginalFilename())) {
            throw new MtonsException("文件格式不支持");
        }
    }

    @Override
    public String store(MultipartFile file, String basePath) throws Exception {
        validateFile(file);
        return writeToStore(file.getBytes(), basePath, file.getOriginalFilename());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int maxWidth) throws Exception {
        validateFile(file);
        byte[] bytes = ImageUtils.scaleByWidth(file, maxWidth);
        return writeToStore(bytes, basePath, file.getOriginalFilename());
    }

    @Override
    public String storeScale(MultipartFile file, String basePath, int width, int height) throws Exception {
        validateFile(file);
        byte[] bytes = ImageUtils.screenshot(file, width, height);
        return writeToStore(bytes, basePath, file.getOriginalFilename());
    }

    public String writeToStore(byte[] bytes, String src, String originalFilename) throws Exception {
        String md5 = MD5.md5File(bytes);
        Resource resource = resourceRepository.findByMd5(md5);
        if (resource != null) {
            return resource.getPath();
        }
        //如果不存在这个资源，返回路径
        String path = FilePathUtils.wholePathName(src, originalFilename, md5);
        path = writeToStore(bytes, path);

        // 图片入库
        resource = new Resource();
        resource.setMd5(md5);
        resource.setPath(path);
        resource.setCreateTime(LocalDateTime.now());
        resourceRepository.save(resource);
        return path;
    }

}
