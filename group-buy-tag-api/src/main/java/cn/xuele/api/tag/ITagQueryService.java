package cn.xuele.api.tag;

import cn.xuele.api.tag.dto.TagQueryRequestDTO;
import cn.xuele.api.tag.dto.TagQueryResponseDTO;

/**
 * 人群标签查询 RPC 契约，供其他服务判断用户是否命中指定标签
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:25
 */
public interface ITagQueryService {

    TagQueryResponseDTO matchCrowdTag(TagQueryRequestDTO request);


}
