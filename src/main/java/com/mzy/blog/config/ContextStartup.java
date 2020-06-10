package com.mzy.blog.config;


import com.mzy.blog.bean.entity.Options;
import com.mzy.blog.contanst.Consts;
import com.mzy.blog.service.ChannelService;
import com.mzy.blog.service.MailService;
import com.mzy.blog.service.OptionsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Map;


@Slf4j
@Order(2)
@Component
public class ContextStartup implements ApplicationRunner,ServletContextAware {

    @Autowired
    private OptionsService optionsService;
    //栏目管理
    @Autowired
    private ChannelService channelService;
    //邮箱服务
    @Autowired
    private MailService mailService;
    //网站配置
    @Autowired
    private SiteOptions siteOptions;

    private ServletContext servletContext;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        log.info("加载配置文件 ...");
        //加载全部配置至web全局servletContext
        reloadOptions(true);
        resetChannels();
        log.info("配置文件加载成功");
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    public void reloadOptions(boolean startup) {
        //获取数据库中的网站全部配置
        List<Options> options = optionsService.findAll();
        log.info("加载配置项 ({})...", options.size());
        //添加数据库配置同时添加配置文件配置
        Map<String, String> map = siteOptions.getOptions();
        options.forEach(opt -> {
            if (StringUtils.isNoneBlank(opt.getKey(), opt.getValue())) {
                map.put(opt.getKey(), opt.getValue());
            }
        });
        //添加至Web应用全局对象servletContext
        servletContext.setAttribute("options", map);
        servletContext.setAttribute("site", siteOptions);
        //暂时不添加邮箱服务
//        mailService.config();

        System.setProperty("site.location", siteOptions.getLocation());
    }

    public void resetChannels() {
        servletContext.setAttribute("channels", channelService.findAll(Consts.STATUS_NORMAL));
    }

}
