package com.mzy.blog.template.directive;


import com.mzy.blog.service.LinksService;
import com.mzy.blog.template.DirectiveHandler;
import com.mzy.blog.template.TemplateDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Eg:
 * <@links>
 *     <#list results as row>
 * 		<li><a href="${row.url}">${row.name}</a></li>
 * 	   </#list>
 * </@links>
 *
 * @author : landy
 * @version : 1.0
 * @date : 2019/11/6
 */
@Component
public class LinksDirective extends TemplateDirective {
    @Autowired
    private LinksService linksService;

    @Override
    public String getName() {
        return "links";
    }

    @Override
    public void execute(DirectiveHandler handler) throws Exception {
        handler.put(RESULTS, linksService.findAll()).render();
    }
}
