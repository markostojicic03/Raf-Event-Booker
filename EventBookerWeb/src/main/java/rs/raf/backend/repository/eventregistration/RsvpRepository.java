package rs.raf.backend.repository.eventregistration;

import rs.raf.backend.model.RsvpModel;

import java.util.List;
import java.util.Optional;

public interface RsvpRepository {
    List<RsvpModel> findByEventId(Long eventId);
    Optional<RsvpModel> findByEventIdAndEmail(Long eventId, String email);
    void save(RsvpModel r);
    int countByEventId(Long eventId);
}