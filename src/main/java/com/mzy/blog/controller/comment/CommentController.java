/**
 *
 */
package com.mzy.blog.controller.comment;



import com.mzy.blog.bean.data.AccountProfile;
import com.mzy.blog.bean.data.CommentVO;
import com.mzy.blog.bean.data.Result;
import com.mzy.blog.contanst.Consts;
import com.mzy.blog.controller.BaseController;
import com.mzy.blog.event.MessageEvent;
import com.mzy.blog.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author langhsu
 */
@RestController
@RequestMapping("/comment")
@ConditionalOnProperty(name = "site.controls.comment", havingValue = "true", matchIfMissing = true)
public class CommentController extends BaseController {
    @Autowired
    private CommentService commentService;
    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping("/list/{toId}")
    public Page<CommentVO> view(@PathVariable Long toId) {
        Pageable pageable = wrapPageable(Sort.by(Sort.Direction.DESC, "id"));
        return commentService.pagingByPostId(pageable, toId);
    }

    @RequestMapping("/submit")
    public Result post(Long toId, String text, HttpServletRequest request) {
        if (!isAuthenticated()) {
            return Result.failure("请先登录在进行操作");
        }

        long pid = ServletRequestUtils.getLongParameter(request, "pid", 0);

        if (toId <= 0 || StringUtils.isBlank(text)) {
            return Result.failure("操作失败");
        }

        AccountProfile profile = getProfile();

        CommentVO c = new CommentVO();
        c.setPostId(toId);
        c.setContent(HtmlUtils.htmlEscape(text));
        c.setAuthorId(profile.getId());

        c.setPid(pid);

        commentService.post(c);

        sendMessage(profile.getId(), toId, pid);

        return Result.successMessage("发表成功");
    }

    @RequestMapping("/delete")
    public Result delete(@RequestParam(name = "id") Long id) {
        Result data;
        try {
            commentService.delete(id, getProfile().getId());
            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        return data;
    }

    /**
     * 发送通知
     *
     * @param userId
     * @param postId
     */
    private void sendMessage(long userId, long postId, long pid) {
        MessageEvent event = new MessageEvent("MessageEvent");
        event.setFromUserId(userId);

        if (pid > 0) {
            event.setEvent(Consts.MESSAGE_EVENT_COMMENT_REPLY);
            event.setCommentParentId(pid);
        } else {
            event.setEvent(Consts.MESSAGE_EVENT_COMMENT);
        }
        // 此处不知道文章作者, 让通知事件系统补全
        event.setPostId(postId);
        applicationContext.publishEvent(event);
    }
}