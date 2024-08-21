package com.example.management.controller;

import com.example.management.entity.Comment;
import com.example.management.entity.Tasks;
import com.example.management.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Tag(name = "Comment", description = "Interaction with comments")
public class CommentController {
    private CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(
            summary = "Create comment"
    )
    @PostMapping(path = "comment/create")
    public Comment create(@RequestBody Comment comment) {
        return commentService.create(comment);
    }

    @Operation(
            summary = "Get information about all comments by task id"
    )

    @GetMapping(path = "comment/get/all/by/task/{id}")
    public List<Comment> findCommentByTaskId (@PathVariable Integer id, @RequestParam(required = false) Integer pageNum,
                                               @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return commentService.findCommentByTaskId(id, null);
        }
        return commentService.findCommentByTaskId(id, PageRequest.of(pageNum, pageSize));
    }
}
