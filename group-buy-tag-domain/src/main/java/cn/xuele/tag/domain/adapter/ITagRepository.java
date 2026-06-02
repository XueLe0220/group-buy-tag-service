package cn.xuele.tag.domain.adapter;

import cn.xuele.tag.domain.model.entity.CrowdTagsJobEntity;

import java.util.List;

/**
 * 人群标签领域仓储接口
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:29
 */
public interface ITagRepository {

    boolean isUserMatchedTag(String userId, String tagId);

    CrowdTagsJobEntity queryCrowdTagsJob(String tagId, String batchId);

    void saveCrowdTagUsers(String tagId, List<String> userIdList);

    void updateCrowdTagStatistics(String tagId, int count);
}
