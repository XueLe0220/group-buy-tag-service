package cn.xuele.tag.domain.service;

/**
 * 人群标签领域服务
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:28
 */
public interface ITagService {

    /**
     *  判断用户是否命中标签
     * @param userId 用户 ID
     * @param tagId 标签 ID
     * @return  true：命中
     */
    boolean matchCrowdTag(String userId, String tagId);

    /**
     * 执行标签批次任务
     * @param tagId     标签 ID
     * @param batchId   批次 ID
     */
    void executeCrowdTagBatch(String tagId, String batchId);

}
