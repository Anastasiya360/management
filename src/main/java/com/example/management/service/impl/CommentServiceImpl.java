package com.example.management.service.impl;

import com.example.management.entity.Comment;
import com.example.management.entity.User;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.CommentRepository;
import com.example.management.repository.TasksRepository;
import com.example.management.service.CommentService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final TasksRepository tasksRepository;

    @Override
    public Comment create(Comment comment) {
        comment.setId(null);
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        comment.setAuthor(currentUser);
        if (comment.getDescription() == null || comment.getDescription().isBlank()) {
            throw new ApiException("Описание не передано", HttpServletResponse.SC_BAD_REQUEST);
        }
        comment.setDateCreate(LocalDate.now());
        if (comment.getTaskId() == null) {
            throw new ApiException("Задача не передана", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (!tasksRepository.existsById(comment.getTaskId())) {
            throw new ApiException("Задача не найдена", HttpServletResponse.SC_NOT_FOUND);
        }
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> findCommentByTaskId(Integer taskId, Pageable pageable) {
        if (pageable == null) {
            return commentRepository.findCommentByTaskId(taskId);
        } else {
            return commentRepository.findCommentByTaskId(taskId, pageable);
        }
    }
}
