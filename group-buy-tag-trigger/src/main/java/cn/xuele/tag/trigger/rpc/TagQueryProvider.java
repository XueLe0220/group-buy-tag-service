package cn.xuele.tag.trigger.rpc;

import cn.xuele.api.tag.ITagQueryService;
import cn.xuele.api.tag.dto.TagQueryRequestDTO;
import cn.xuele.api.tag.dto.TagQueryResponseDTO;
import cn.xuele.common.types.enums.ResponseCode;
import cn.xuele.common.types.response.Response;
import cn.xuele.tag.domain.service.ITagService;
import org.apache.dubbo.common.utils.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * 人群标签查询 RPC 契约实现
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/03 16:33
 */
@DubboService
public class TagQueryProvider implements ITagQueryService {

    private final ITagService tagService;

    public TagQueryProvider(ITagService tagService) {
        this.tagService = tagService;
    }


    @Override
    public Response<TagQueryResponseDTO> matchCrowdTag(TagQueryRequestDTO request) {

        if (request == null) {
            return Response.failure(ResponseCode.ILLEGAL_PARAMETER);
        }
        String tagId = request.getTagId();
        String userId = request.getUserId();

        if (StringUtils.isBlank(tagId) || StringUtils.isBlank(userId)) {
            return Response.failure(ResponseCode.ILLEGAL_PARAMETER);
        }

        boolean matched = tagService.matchCrowdTag(userId, tagId);

        return Response.success(TagQueryResponseDTO.builder()
                .userId(userId)
                .tagId(tagId)
                .matched(matched)
                .build());

    }

}
