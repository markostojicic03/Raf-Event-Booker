package rs.raf.backend.service;

import rs.raf.backend.model.CommentModel;
import rs.raf.backend.repository.comment.CommentRepository;

import javax.inject.Inject;
import java.util.List;

public class CommentService {


    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<CommentModel> getCommentsForEvent(Long eventId) {
        return commentRepository.findByEventIdOrderByCreatedAtDesc(eventId);
    }

    public void addComment(CommentModel comment) {
        commentRepository.save(comment);
    }

    public void likeComment(Long id) {
        commentRepository.likeComment(id);
    }

    public void dislikeComment(Long id) {
        commentRepository.dislikeComment(id);
    }
}