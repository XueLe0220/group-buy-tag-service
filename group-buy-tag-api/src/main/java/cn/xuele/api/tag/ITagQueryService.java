package cn.xuele.api.tag;

import cn.xuele.api.tag.dto.TagQueryRequestDTO;
import cn.xuele.api.tag.dto.TagQueryResponseDTO;

/**
 * TODO: 类描述
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:25
 */
public interface ITagQueryService {

    TagQueryResponseDTO matchCrowdTag(TagQueryRequestDTO request);

}
