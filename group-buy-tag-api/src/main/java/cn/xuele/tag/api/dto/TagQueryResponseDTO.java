package cn.xuele.tag.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 人群标签查询响应 DTO
 *
 * @author XueLe
 * @version 1.0.0
 * @since 2026/06/02 15:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagQueryResponseDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String userId;
    private String tagId;
    private boolean matched;

}
