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
 * 人群标签仓储服务
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

    /**
     * 判断用户是否命中人群标签
     * @param userId    用户 ID
     * @param tagId     标签 ID
     * @return 是否匹配
     */
    @Override
    public boolean isUserMatchedTag(String userId, String tagId) {
        //参数校验
        if (userId == null || userId.isBlank() || tagId == null || tagId.isBlank()) {
            return false;
        }

        // 根据 tagId 获取 bitset，计算userid对应的offset，返回结果。
        RBitSet bitSet = redissonClient.getBitSet(TagBitmapUtils.tagBitmapKey(tagId));
        return bitSet.get(TagBitmapUtils.offsetOf(userId));
    }

    /**
     * 查找人群标签任务
     * @param tagId     标签 ID
     * @param batchId   任务批次 ID
     * @return  人群标签任务实体
     */
    @Override
    public CrowdTagsJobEntity queryCrowdTagsJob(String tagId, String batchId) {

        // 构建请求对象
        CrowdTagsJob crowdTagsJobReq = new CrowdTagsJob();
        crowdTagsJobReq.setTagId(tagId);
        crowdTagsJobReq.setBatchId(batchId);

        // 查询任务
        CrowdTagsJob crowdTagsJob = crowdTagsJobDao.queryCrowdTagsJobEntity(crowdTagsJobReq);
        if (crowdTagsJob == null) {
            return null;
        }

        // 返回任务实体
        return CrowdTagsJobEntity.builder()
                .tagType(crowdTagsJob.getTagType())
                .tagRule(crowdTagsJob.getTagRule())
                .statStartTime(crowdTagsJob.getStatStartTime() == null ? null : crowdTagsJob.getStatStartTime().toLocalDate())
                .statEndTime(crowdTagsJob.getStatEndTime() == null ? null : crowdTagsJob.getStatEndTime().toLocalDate())
                .build();
    }


    /**
     * 保存标签目标用户
     * TODO: 当前 DB 与 Redis bitmap 非同一事务，Redis 写入失败或部分成功时可能产生短暂不一致，后续通过补偿任务或 bitmap 重建机制兜底。
     * @param tagId         标签 ID
     * @param userIdList    用户列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCrowdTagUsers(String tagId, List<String> userIdList) {
        // 参数校验
        if (tagId == null || tagId.isBlank() || userIdList == null || userIdList.isEmpty()) {
            return;
        }

        // 构建批量插入明细表 List
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

        // 批量插入明细表
        crowdTagsDetailDao.addCrowdTagsUsers(crowdTagsDetailReqList);

        // 设置bitset
        RBatch batch = redissonClient.createBatch();
        RBitSetAsync bitSetAsync = batch.getBitSet(TagBitmapUtils.tagBitmapKey(tagId));
        for (CrowdTagsDetail crowdTagsDetail : crowdTagsDetailReqList) {
            bitSetAsync.setAsync(TagBitmapUtils.offsetOf(crowdTagsDetail.getUserId()), true);
        }
        batch.execute();
    }

    // 更新标签人数
    @Override
    public void updateCrowdTagStatistics(String tagId, int count) {
        CrowdTags crowdTagsReq = new CrowdTags();
        crowdTagsReq.setTagId(tagId);
        crowdTagsReq.setStatistics(count);

        crowdTagsDao.updateCrowdTagStatistics(crowdTagsReq);
    }
}
