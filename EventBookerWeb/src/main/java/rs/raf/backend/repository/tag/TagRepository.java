package rs.raf.backend.repository.tag;
import rs.raf.backend.model.TagModel;

import java.util.List;
import java.util.Set;

public interface TagRepository {
    List<TagModel> findAll();
    TagModel findById(Long id);
    Set<TagModel> findAllByIds(Set<Long> ids);
    void save(TagModel tag);
    void delete(Long id);
}
