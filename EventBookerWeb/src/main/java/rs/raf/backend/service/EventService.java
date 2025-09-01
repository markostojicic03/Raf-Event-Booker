package rs.raf.backend.service;

import rs.raf.backend.model.EventModel;
import rs.raf.backend.model.TagModel;
import rs.raf.backend.repository.event.EventRepository;
import rs.raf.backend.repository.tag.TagRepository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EventService {
    private final EventRepository eventRepository;
    private final TagRepository tagRepository;

    // Ubacuješ konkretan repo (npr. MySqlEventRepository)
    public EventService(EventRepository eventRepository, TagRepository tagRepository) {
        this.eventRepository = eventRepository;
        this.tagRepository = tagRepository;
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

    public EventModel updateEvent(Long id, EventModel e) {
        EventModel existing = eventRepository.findById(id);
        if (existing == null) return null;
        e.setId(id);
        return eventRepository.save(e);
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
            eventRepository.save(e);   // merge
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
