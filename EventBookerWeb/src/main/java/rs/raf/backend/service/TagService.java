package rs.raf.backend.service;

import rs.raf.backend.model.TagModel;
import rs.raf.backend.repository.tag.TagRepository;

import java.util.List;
import java.util.Optional;

public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagModel> getAlltags() {
        return tagRepository.findAll();
    }

    public TagModel getTagById(Long id) {
        return tagRepository.findById(id);
    }

    public TagModel createTag(TagModel tag) {
        tagRepository.save(tag);
        return tag;
    }

    public TagModel updateTag(Long id, TagModel updatedTag) {
        TagModel existing = tagRepository.findById(id);
        if (existing != null) {
            existing.setTagName(updatedTag.getTagName());
            tagRepository.save(existing);
        }
        return null;
    }

    public boolean deleteTag(Long id) {
        TagModel existing = tagRepository.findById(id);
        if (existing != null) {
            tagRepository.delete(existing.getId());
            return true;
        }
        return false;
    }

}
