package rs.raf.backend.model;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "event_model")
@Getter
@Setter
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title; // Naslov

    @Column(nullable = false, length = 2000)
    private String description; // Opis

    @Column(nullable = false)
    private LocalDateTime createdAt; // Vreme kreiranja

    @Column(nullable = false)
    private LocalDateTime eventDate; // Datum održavanja

    @Column(nullable = false)
    private String location; // Lokacija

    @Column(nullable = false)
    private Integer views = 0; // Broj pregleda

    @ManyToOne(fetch = FetchType.LAZY) // Veza sa korisnikom koji je autor
    @JoinColumn(name = "author_id", nullable = false)
    private UserModel author;

    @ElementCollection
    @CollectionTable(name = "event_tags", joinColumns = @JoinColumn(name = "event_id"))
    @Column(name = "tag")
    private Set<String> tags = new HashSet<>(); // Tagovi

//    @ManyToOne(fetch = FetchType.LAZY) // Veza sa kategorijom
//    @JoinColumn(name = "category_id", nullable = false)
//    private CategoryModel category;

    private Integer maxCapacity; // Max kapacitet (opciono)

    public EventModel() {
    }

    public EventModel(String title, String description, LocalDateTime createdAt, LocalDateTime eventDate,
                 String location, UserModel author, CategoryModel category, Integer maxCapacity) {
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.eventDate = eventDate;
        this.location = location;
        this.author = author;
      //  this.category = category;
        this.maxCapacity = maxCapacity;
        this.views = 0;
    }



}
