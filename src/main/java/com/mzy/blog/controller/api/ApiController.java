/*
+--------------------------------------------------------------------------
|   Mblog [#RELEASE_VERSION#]
|   ========================================
|   Copyright (c) 2014, 2015 mtons. All Rights Reserved
|   http://www.mtons.com
|
+---------------------------------------------------------------------------
*/
package com.mzy.blog.controller.api;


import com.mzy.blog.bean.data.CommentVO;
import com.mzy.blog.bean.data.PostVO;
import com.mzy.blog.bean.data.Result;
import com.mzy.blog.contanst.Consts;
import com.mzy.blog.controller.BaseController;
import com.mzy.blog.service.CommentService;
import com.mzy.blog.service.PostService;
import com.mzy.blog.utils.BeanMapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 侧边栏数据加载
 *
 * @author langhsu
 */
@RestController
@RequestMapping("/api")
public class ApiController extends BaseController {
    @Autowired
    private PostService postService;
    @Autowired
    private CommentService commentService;

    @PostMapping(value = "/login")
    public Result login(String username, String password) {
        return executeLogin(username, password, false);
    }

    @RequestMapping("/posts")
    public Page<PostVO> posts(HttpServletRequest request) {
        String order = ServletRequestUtils.getStringParameter(request, "order", Consts.order.NEWEST);
        int channelId = ServletRequestUtils.getIntParameter(request, "channelId", 0);
        return postService.paging(wrapPageable(Sort.by(Sort.Direction.DESC, BeanMapUtils.postOrder(order))), channelId, null);
    }

    @RequestMapping(value = "/latest_comments")
    public List<CommentVO> latestComments(@RequestParam(name = "size", defaultValue = "6") Integer size) {
        return commentService.findLatestComments(size);
    }
}
