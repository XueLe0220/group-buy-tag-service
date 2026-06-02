package cn.xuele.tag.domain.service;


import cn.xuele.tag.domain.adapter.ITagRepository;
import cn.xuele.tag.domain.model.entity.CrowdTagsJobEntity;

import java.util.List;

/**
 * 人群标签领域服务实现
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:29
 */

public class TagService implements ITagService {


    private final ITagRepository tagRepository;

    public TagService(ITagRepository tagRepository){
        this.tagRepository = tagRepository;
    }

    @Override
    public boolean matchCrowdTag(String userId, String tagId) {
        if (userId == null || userId.isBlank() || tagId == null || tagId.isBlank()) {
            return false;
        }

        return tagRepository.isUserMatchedTag(userId, tagId);
    }

    @Override
    public void executeCrowdTagBatch(String tagId, String batchId) {
        if (tagId == null || tagId.isBlank() || batchId == null || batchId.isBlank()) {
            return;
        }

        CrowdTagsJobEntity jobEntity = tagRepository.queryCrowdTagsJob(tagId, batchId);
        if (jobEntity == null) {
            return;
        }

        List<String> userIdList = List.of("xuele", "keke", "bangzhi");
        tagRepository.saveCrowdTagUsers(tagId, userIdList);
        tagRepository.updateCrowdTagStatistics(tagId, userIdList.size());
    }
}
