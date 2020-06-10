package com.mzy.blog.controller.user;

import com.mzy.blog.contanst.Views;
import com.mzy.blog.controller.BaseController;
import com.mzy.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.RequestWrapper;

/**
 * 用户controller类
 *
 * @program: mengzyBlog
 * @author: mengzy 18306299232@163.com
 * @create: 2020-06-06 12:30
 **/
@Controller
@RequestMapping("/users")
public class UsersController extends BaseController {

    @Autowired
    private UserService userService;


    /**
     * 用户文章 ，通过传入 Views.METHOD_POSTS 寻找对应的视图
     *
     * @param userId 用户ID
     * @param model  ModelMap
     * @return template name
     */
    @GetMapping(value = "/{userId}")
    //PathVariable注解与变量上，这样可以为我们的变量实现赋值操作
    public String posts(@PathVariable(value = "userId") Long userId,
                        ModelMap model, HttpServletRequest request) {
        //寻找对应的视图
        return method(userId, Views.METHOD_POSTS, model, request);
    }

    /**
     * 通用方法, 访问 users 目录下的页面
     *
     * @param userId  用户ID
     * @param method  调用方法
     * @param model   ModelMap
     * @param request 对应的request域解析器
     * @return template name
     */
    @GetMapping(value = "/{userId}/{method}")
    public String method(@PathVariable(value = "userId") Long userId, @PathVariable(value = "method") String method, ModelMap model, HttpServletRequest request) {
        //如果request含有页码，则实现分页
        model.put("pageNo", ServletRequestUtils.getIntParameter(request, "pageNo", 1));
        //初始化用户信息，暂时不做验证的处理
        initUser(userId, model);
        return view(String.format(Views.USER_METHOD_TEMPLATE, method));

    }

    private void initUser(long userId, ModelMap model) {
        model.put("user", userService.get(userId));
        boolean owner = true;
        model.put("owner", owner);
    }


}
