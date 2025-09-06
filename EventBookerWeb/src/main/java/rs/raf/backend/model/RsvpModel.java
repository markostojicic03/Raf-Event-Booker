package rs.raf.backend.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rsvp")
@Getter
@Setter
public class RsvpModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private Long eventId;
    @Column(nullable = false)
    private String createdAt = LocalDateTime.now().toString();
}