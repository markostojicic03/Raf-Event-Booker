package rs.raf.backend.service;

import rs.raf.backend.model.RsvpModel;
import rs.raf.backend.repository.eventregistration.RsvpRepository;

import java.util.List;

public class RsvpService {
    private final RsvpRepository repo;
    public RsvpService(RsvpRepository repo){ this.repo = repo; }

    public List<RsvpModel> getByEvent(Long eventId){ return repo.findByEventId(eventId); }
    public int countByEvent(Long eventId){ return repo.countByEventId(eventId); }

    public RsvpModel register(Long eventId, String email){
        if (repo.findByEventIdAndEmail(eventId, email).isPresent())
            throw new IllegalStateException("Već ste prijavljeni na ovaj događaj.");
        RsvpModel r = new RsvpModel();
        r.setEventId(eventId); r.setEmail(email);
        repo.save(r); return r;
    }
}