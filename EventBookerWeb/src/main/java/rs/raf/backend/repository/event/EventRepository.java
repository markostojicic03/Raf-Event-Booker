package rs.raf.backend.repository.event;

import rs.raf.backend.model.EventModel;

import java.util.List;

public interface EventRepository {
    List<EventModel> findAll();
    List<EventModel> findAllByCategoryId(Long categoryId);
    List<EventModel> findAllBySearch(String querySearch);
    EventModel findById(Long id);
    void save(EventModel event);
    void delete(Long id);

    // Nova metoda
    List<EventModel> findLatest(int limit);
    List<EventModel> findMostViewed(int limit);
    public List<EventModel> findByTagId(Long tagId);
}
