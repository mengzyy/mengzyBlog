package com.mzy.blog.controller.user;


import com.mzy.blog.bean.data.AccountProfile;
import com.mzy.blog.bean.data.Result;
import com.mzy.blog.bean.data.UploadResult;
import com.mzy.blog.bean.data.UserVO;
import com.mzy.blog.contanst.Consts;
import com.mzy.blog.contanst.Views;
import com.mzy.blog.controller.BaseController;
import com.mzy.blog.controller.posts.UploadController;
import com.mzy.blog.service.SecurityCodeService;
import com.mzy.blog.service.UserService;
import com.mzy.blog.utils.FileKit;
import com.mzy.blog.utils.FilePathUtils;
import com.mzy.blog.utils.ImageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Controller
@RequestMapping("/settings")
public class SettingsController extends BaseController {
    @Autowired
    private UserService userService;
    //验证码功能暂时先不做
//    @Autowired
//    private SecurityCodeService securityCodeService;

    @GetMapping(value = "/profile")
    public String view(ModelMap model) {
        AccountProfile profile = getProfile();
        UserVO view = userService.get(profile.getId());
        model.put("view", view);
        return view(Views.SETTINGS_PROFILE);
    }

    @GetMapping(value = "/email")
    public String email() {
        return view(Views.SETTINGS_EMAIL);
    }

    @GetMapping(value = "/avatar")
    public String avatar() {
        return view(Views.SETTINGS_AVATAR);
    }

    @GetMapping(value = "/password")
    public String password() {
        return view(Views.SETTINGS_PASSWORD);
    }

    @PostMapping(value = "/profile")
    public String updateProfile(String name, String signature, ModelMap model) {
        Result data;
        AccountProfile profile = getProfile();

        try {
            UserVO user = new UserVO();
            user.setId(profile.getId());
            user.setName(name);
            user.setSignature(signature);

            putProfile(userService.update(user));

            // put 最新信息
            UserVO view = userService.get(profile.getId());
            model.put("view", view);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_PROFILE);
    }

    @PostMapping(value = "/email")
    public String updateEmail(String email, String code, ModelMap model) {
        Result data;
        AccountProfile profile = getProfile();
        try {
            Assert.hasLength(email, "请输入邮箱地址");
            Assert.hasLength(code, "请输入验证码");
            //先不做验证码
//            securityCodeService.verify(String.valueOf(profile.getId()), Consts.CODE_BIND, code);
            // 先执行修改，判断邮箱是否更改，或邮箱是否被人使用
            AccountProfile p = userService.updateEmail(profile.getId(), email);
            putProfile(p);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_EMAIL);
    }

    @PostMapping(value = "/password")
    public String updatePassword(String oldPassword, String password, ModelMap model) {
        Result data;
        try {
            AccountProfile profile = getProfile();
            userService.updatePassword(profile.getId(), oldPassword, password);

            data = Result.success();
        } catch (Exception e) {
            data = Result.failure(e.getMessage());
        }
        model.put("data", data);
        return view(Views.SETTINGS_PASSWORD);
    }

    @PostMapping("/avatar")
    @ResponseBody
    public UploadResult updateAvatar(@RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        //文件返回信息状态码
        UploadResult result = new UploadResult();
        AccountProfile profile = getProfile();

        // 检查空
        if (null == file || file.isEmpty()) {
            return result.error(UploadController.errorInfo.get("NOFILE"));
        }
        //返回文件原始名称
        String fileName = file.getOriginalFilename();

        // 检查类型
        if (!FileKit.checkFileType(fileName)) {
            return result.error(UploadController.errorInfo.get("TYPE"));
        }

        // 保存图片
        try {
            //保存路径
            String ava100 = Consts.avatarPath + getAvaPath(profile.getId(), 240);
            //图片压缩为字节
            byte[] bytes = ImageUtils.screenshot(file, 240, 240);
            String path = storageFactory.get().writeToStore(bytes, ava100);

            AccountProfile user = userService.updateAvatar(profile.getId(), path);
            putProfile(user);

            result.ok(UploadController.errorInfo.get("SUCCESS"));
            result.setName(fileName);
            result.setPath(path);
            result.setSize(file.getSize());
        } catch (Exception e) {
            result.error(UploadController.errorInfo.get("UNKNOWN"));
        }
        return result;
    }

    private String getAvaPath(long uid, int size) {
        String base = FilePathUtils.getAvatar(uid);
        return String.format("/%s_%d.jpg", base, size);
    }
}
