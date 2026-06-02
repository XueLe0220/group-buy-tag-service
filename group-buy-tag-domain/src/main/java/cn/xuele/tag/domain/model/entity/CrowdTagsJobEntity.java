package cn.xuele.tag.domain.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 人群标签任务实体对象
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:31
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrowdTagsJobEntity {

    /** 标签类型（参与量、消费金额等） */
    private Integer tagType;
    /** 标签规则（如：限定类型 N次） */
    private String tagRule;
    /** 统计数据开始时间（业务时间窗口起始） */
    private LocalDate statStartTime;
    /** 统计数据结束时间（业务时间窗口结束） */
    private LocalDate statEndTime;

}
