package com.example.management.service;

import com.example.management.entity.Comment;
import com.example.management.entity.User;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.CommentRepository;
import com.example.management.repository.TasksRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CommentService {
    private CommentRepository commentRepository;
    private TasksRepository tasksRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, TasksRepository tasksRepository) {
        this.commentRepository = commentRepository;
        this.tasksRepository = tasksRepository;
    }

    /**
     * Создание комментария
     *
     * @return созданный комментарий
     */
    public Comment create(Comment comment) {
        comment.setId(null);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setAuthor(currentUser);
        if (comment.getDescription() == null || comment.getDescription().isBlank()) {
            throw new ApiException("Описание не передано", 400);
        }
        comment.setDateCreate(LocalDate.now());
        if (comment.getTaskId() == null) {
            throw new ApiException("Задача не передана", 400);
        }
        if (!tasksRepository.existsById(comment.getTaskId())) {
            throw new ApiException("Задача не найдена", 404);
        }
        return commentRepository.save(comment);
    }

    /**
     * Получение всех комментариев по id задачи
     *
     * @return список комментариев
     */
    public List<Comment> findCommentByTaskId(Integer taskId, Pageable pageable) {
        if (pageable == null) {
            return commentRepository.findCommentByTaskId(taskId);
        } else {
            return commentRepository.findCommentByTaskId(taskId, pageable);
        }
    }
}
