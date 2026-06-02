package cn.xuele.tag.infrastructure.dao;

import cn.xuele.tag.infrastructure.dao.po.CrowdTags;
import org.apache.ibatis.annotations.Mapper;

/**
 * 人群标签主表 DAO 接口
 * <p>
 * 对应表：crowd_tags
 * 负责管理标签的基础信息和统计数据。
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 23:23
 */
@Mapper
public interface ICrowdTagsDao {

    /**
     * 更新人群标签统计量
     *
     * @param crowdTagsReq 包含 tagId 和 statistics(统计数量) 的 PO 对象
     */
    void updateCrowdTagStatistics(CrowdTags crowdTagsReq);
}