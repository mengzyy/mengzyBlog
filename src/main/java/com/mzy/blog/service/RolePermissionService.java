package com.mzy.blog.service;



import com.mzy.blog.bean.entity.Permission;
import com.mzy.blog.bean.entity.RolePermission;

import java.util.List;
import java.util.Set;

/**
 * @author - langhsu
 * @create - 2018/5/18
 */
public interface RolePermissionService {
    List<Permission> findPermissions(long roleId);
    void deleteByRoleId(long roleId);
    void add(Set<RolePermission> rolePermissions);

}
