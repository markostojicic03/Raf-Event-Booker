package rs.raf.backend.repository.comment;

import rs.raf.backend.model.CommentModel;

import java.util.List;

public interface CommentRepository {

    List<CommentModel> findByEventIdOrderByCreatedAtDesc(Long eventId);
    void save(CommentModel comment);
}
