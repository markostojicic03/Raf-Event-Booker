package rs.raf.backend.service;

import rs.raf.backend.model.EventModel;
import rs.raf.backend.repository.event.EventRepository;

import java.util.List;

public class EventService {
    private final EventRepository eventRepository;

    // Ubacuješ konkretan repo (npr. MySqlEventRepository)
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventModel> getAllEvents() {
        return eventRepository.findAll();
    }

    public EventModel getEvent(Long id) {
        return eventRepository.findById(id);
    }

    public void createEvent(EventModel event) {
        eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.delete(id);
    }

    // Vrati najskorije događaje (limit možeš promeniti u pozivu)
    public List<EventModel> getLatestEvents(int limit) {
        return eventRepository.findLatest(limit);
    }
}
