package cn.xuele.tag.infrastructure.adapter.repository;

import cn.xuele.tag.domain.adapter.ITagRepository;
import cn.xuele.tag.domain.model.entity.CrowdTagsJobEntity;
import cn.xuele.tag.infrastructure.dao.ICrowdTagsDao;
import cn.xuele.tag.infrastructure.dao.ICrowdTagsDetailDao;
import cn.xuele.tag.infrastructure.dao.ICrowdTagsJobDao;
import cn.xuele.tag.infrastructure.dao.po.CrowdTags;
import cn.xuele.tag.infrastructure.dao.po.CrowdTagsDetail;
import cn.xuele.tag.infrastructure.dao.po.CrowdTagsJob;
import cn.xuele.tag.infrastructure.redis.TagBitmapUtils;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBatch;
import org.redisson.api.RBitSet;
import org.redisson.api.RBitSetAsync;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: 类描述
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 18:18
 */
@Repository
@RequiredArgsConstructor
public class TagRepository implements ITagRepository {

    private final ICrowdTagsDetailDao crowdTagsDetailDao;
    private final ICrowdTagsJobDao crowdTagsJobDao;
    private final ICrowdTagsDao crowdTagsDao;
    private final RedissonClient redissonClient;

    @Override
    public boolean isUserMatchedTag(String userId, String tagId) {
        if (userId == null || userId.isBlank() || tagId == null || tagId.isBlank()) {
            return false;
        }

        RBitSet bitSet = redissonClient.getBitSet(TagBitmapUtils.tagBitmapKey(tagId));
        return bitSet.get(TagBitmapUtils.offsetOf(userId));
    }

    @Override
    public CrowdTagsJobEntity queryCrowdTagsJob(String tagId, String batchId) {
        CrowdTagsJob crowdTagsJobReq = new CrowdTagsJob();
        crowdTagsJobReq.setTagId(tagId);
        crowdTagsJobReq.setBatchId(batchId);

        CrowdTagsJob crowdTagsJob = crowdTagsJobDao.queryCrowdTagsJobEntity(crowdTagsJobReq);
        if (crowdTagsJob == null) {
            return null;
        }

        return CrowdTagsJobEntity.builder()
                .tagType(crowdTagsJob.getTagType())
                .tagRule(crowdTagsJob.getTagRule())
                .statStartTime(crowdTagsJob.getStatStartTime() == null ? null : crowdTagsJob.getStatStartTime().toLocalDate())
                .statEndTime(crowdTagsJob.getStatEndTime() == null ? null : crowdTagsJob.getStatEndTime().toLocalDate())
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCrowdTagUsers(String tagId, List<String> userIdList) {
        if (tagId == null || tagId.isBlank() || userIdList == null || userIdList.isEmpty()) {
            return;
        }

        List<CrowdTagsDetail> crowdTagsDetailReqList = new ArrayList<>(userIdList.size());
        for (String userId : userIdList) {
            if (userId == null || userId.isBlank()) {
                continue;
            }

            crowdTagsDetailReqList.add(CrowdTagsDetail.builder()
                    .tagId(tagId)
                    .userId(userId)
                    .build());
        }

        if (crowdTagsDetailReqList.isEmpty()) {
            return;
        }

        crowdTagsDetailDao.addCrowdTagsUsers(crowdTagsDetailReqList);

        RBatch batch = redissonClient.createBatch();
        RBitSetAsync bitSetAsync = batch.getBitSet(TagBitmapUtils.tagBitmapKey(tagId));
        for (CrowdTagsDetail crowdTagsDetail : crowdTagsDetailReqList) {
            bitSetAsync.setAsync(TagBitmapUtils.offsetOf(crowdTagsDetail.getUserId()), true);
        }
        batch.execute();
    }

    @Override
    public void updateCrowdTagStatistics(String tagId, int count) {
        CrowdTags crowdTagsReq = new CrowdTags();
        crowdTagsReq.setTagId(tagId);
        crowdTagsReq.setStatistics(count);

        crowdTagsDao.updateCrowdTagStatistics(crowdTagsReq);
    }
}
