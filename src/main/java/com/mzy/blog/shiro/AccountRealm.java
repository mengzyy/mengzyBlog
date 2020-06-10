package com.mzy.blog.shiro;


import com.mzy.blog.bean.data.AccountProfile;
import com.mzy.blog.bean.data.UserVO;
import com.mzy.blog.bean.entity.Role;
import com.mzy.blog.contanst.Consts;
import com.mzy.blog.service.UserRoleService;
import com.mzy.blog.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.AllowAllCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AccountRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleService userRoleService;

    public AccountRealm() {
        super(new AllowAllCredentialsMatcher());
        setAuthenticationTokenClass(UsernamePasswordToken.class);
    }
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //得到当前的user
        AccountProfile profile = (AccountProfile) SecurityUtils.getSubject().getPrincipal();
        if (profile != null) {
            //数据库user实体类
            UserVO user = userService.get(profile.getId());
            if (user != null) {
                SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
                //得到用户的权限
                List<Role> roles = userRoleService.listRoles(user.getId());

                //赋予角色，注意：一个用户含有多个角色，这里是拿到全部的角色权限
                roles.forEach(role -> {

                    info.addRole(role.getName());
                    //赋予权限
                    role.getPermissions().forEach(permission -> info.addStringPermission(permission.getName()));
                });
                return info;
            }
        }
        return null;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        //查询数据库是否可以得到当前用户
        AccountProfile profile = getAccount(userService, token);

        if (null == profile) {
            throw new UnknownAccountException(upToken.getUsername());
        }

        if (profile.getStatus() == Consts.STATUS_CLOSED) {
            throw new LockedAccountException(profile.getName());
        }
        //将当前用户信息存放在Authen中，之后可以用户权限访问
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(profile, token.getCredentials(), getName());
        Session session = SecurityUtils.getSubject().getSession();
        session.setAttribute("profile", profile);
        return info;
    }

    protected AccountProfile getAccount(UserService userService, AuthenticationToken token) {
        UsernamePasswordToken upToken = (UsernamePasswordToken) token;
        return userService.login(upToken.getUsername(), String.valueOf(upToken.getPassword()));
    }
}
