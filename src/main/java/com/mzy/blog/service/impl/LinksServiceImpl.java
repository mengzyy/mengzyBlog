package com.mzy.blog.service.impl;


import com.mzy.blog.bean.entity.Links;
import com.mzy.blog.dao.repository.LinksRepository;
import com.mzy.blog.service.LinksService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * @author : landy
 * @version : 1.0
 * @date : 2019/11/6
 */
@Service
@Transactional(readOnly = true)
public class LinksServiceImpl implements LinksService {
    @Autowired
    private LinksRepository linksRepository;

    @Override
    public List<Links> findAll() {
        return linksRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Override
    @Transactional
    public void update(Links links) {
        Optional<Links> optional = linksRepository.findById(links.getId());
        Links po = optional.orElse(new Links());
        BeanUtils.copyProperties(links, po, "created", "updated");
        linksRepository.save(po);
    }

    @Override
    @Transactional
    public void delete(long id) {
        linksRepository.deleteById(id);
    }
}
