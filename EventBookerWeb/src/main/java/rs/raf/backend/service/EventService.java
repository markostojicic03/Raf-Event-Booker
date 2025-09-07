package rs.raf.backend.service;

import rs.raf.backend.model.CategoryModel;
import rs.raf.backend.model.EventModel;
import rs.raf.backend.model.TagModel;
import rs.raf.backend.repository.category.CategoryRepository;
import rs.raf.backend.repository.event.EventRepository;
import rs.raf.backend.repository.tag.TagRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventService {
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;


    public EventService(EventRepository eventRepository, TagRepository tagRepository, CategoryRepository categoryRepository) {
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }
    public List<EventModel> getAllEventsByCategoryId(Long categoryId) {
        return eventRepository.findAllByCategoryId(categoryId);
    }

    public List<EventModel> getAllEventsBySearch(String querySearch) {
        return eventRepository.findAllBySearch(querySearch);
    }


    public EventModel getEvent(Long id) {
        return eventRepository.findById(id);
    }

    public EventModel createEvent(EventModel event) {
        Set<Long> tagIds = event.getTags()
                .stream()
                .map(TagModel::getId)
                .collect(Collectors.toSet());

        Set<TagModel> resolved = tagRepository.findAllByIds(tagIds);
        event.setTags(resolved);

        return eventRepository.save(event);
    }

    public EventModel updateEvent(Long id, EventModel dto) {
        EventModel existing = eventRepository.findById(id);
        if (existing == null) return null;


        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setEventDate(dto.getEventDate());
        existing.setLocation(dto.getLocation());
        existing.setMaxCapacity(dto.getMaxCapacity());


        if (dto.getCategory() != null && dto.getCategory().getId() != null) {
            CategoryModel cat =
                    categoryRepository.findById(dto.getCategory().getId());
            if (cat == null) throw new IllegalArgumentException("Unknown category");
            existing.setCategory(cat);
        }


        if (dto.getTags() != null) {
            Set<Long> tagIds = dto.getTags()
                    .stream()
                    .map(TagModel::getId)
                    .collect(Collectors.toSet());
            Set<TagModel> resolvedTags = tagRepository.findAllByIds(tagIds);
            existing.setTags(resolvedTags);
        }

        return eventRepository.save(existing);
    }

    public boolean deleteEvent(Long id) {
        if (eventRepository.findById(id) == null) return false;
        eventRepository.delete(id);
        return true;
    }

    public EventModel save(EventModel e) { return eventRepository.save(e); }


    public List<EventModel> getLatestEvents(int limit) {
        return eventRepository.findLatest(limit);
    }

    public List<EventModel> getMostViewedEvents(int limit) {
        return eventRepository.findMostViewed(limit);
    }

    public void incrementViewCount(Long id) {
        EventModel e = eventRepository.findById(id);
        if (e != null) {
            e.setViews(e.getViews() + 1);
            eventRepository.save(e);
        }
    }

    public void vote(Long id, String type) {
        EventModel e = eventRepository.findById(id);
        if (e == null) return;
        if ("like".equals(type)) e.setLikes(e.getLikes() + 1);
        else e.setDislikes(e.getDislikes() + 1);
        eventRepository.save(e);
    }

    public List<EventModel> findByTag(Long tagId) {
        return eventRepository.findByTagId(tagId);
    }
    public List<EventModel> findRelated(List<Long> tagIds, Long excludeId, int limit) {
        return eventRepository.findRelatedByTags(tagIds, excludeId, limit);
    }

    public List<EventModel> findTopByReactions(int limit) {
        return eventRepository.findTopByReactions(limit);
    }
}
