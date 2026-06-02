package cn.xuele.tag.domain.service;


import cn.xuele.tag.domain.adapter.ITagRepository;

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
        return false;
    }

    @Override
    public void executeCrowdTagBatch(String tagId, String batchId) {

    }
}
