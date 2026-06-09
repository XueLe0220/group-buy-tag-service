package cn.xuele.tag.app.config;

import cn.xuele.tag.domain.adapter.ITagRepository;
import cn.xuele.tag.domain.service.ITagService;
import cn.xuele.tag.domain.service.TagService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 领域服务装配类
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/03 15:34
 */
@Configuration
public class DomainServiceConfig {

    @Bean
    public ITagService tagService(ITagRepository tagRepository) {
        return new TagService(tagRepository);
    }

}
