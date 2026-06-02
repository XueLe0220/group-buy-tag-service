package cn.xuele.tag.infrastructure.adapter.repository;

import cn.xuele.tag.domain.adapter.ITagRepository;
import cn.xuele.tag.domain.model.entity.CrowdTagsJobEntity;

import java.util.List;

/**
 * TODO: 类描述
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 18:18
 */
public class TagRepository implements ITagRepository {
    @Override
    public boolean isUserMatchedTag(String userId, String tagId) {
        return false;
    }

    @Override
    public CrowdTagsJobEntity queryCrowdTagsJob(String tagId, String batchId) {
        return null;
    }

    @Override
    public void saveCrowdTagUsers(String tagId, List<String> userIdList) {

    }

    @Override
    public void updateCrowdTagStatistics(String tagId, int count) {

    }
}
