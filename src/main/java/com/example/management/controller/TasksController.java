package com.example.management.controller;

import com.example.management.entity.Tasks;
import com.example.management.service.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@Tag(name = "Tasks", description = "Interaction with tasks")
public class TasksController {
    private TasksService tasksService;

    @Autowired
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }
    @Operation(
            summary = "Delete task",
            description = "Delete task by id"
    )
    @DeleteMapping(path = "task/delete/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Integer id) {
        return tasksService.deleteById(id);
    }

    @Operation(
            summary = "Create task"
    )
    @PostMapping(path = "task/create")
    public ResponseEntity<?> create(@RequestBody Tasks tasks) {
        return tasksService.create(tasks);
    }

    @Operation(
            summary = "Get information about all tasks"
    )
    @GetMapping(path = "task/get/all")
    public ResponseEntity<?> getAll() {
        return tasksService.getAll();
    }
    @Operation(
            summary = "Change user",
            description = "Change user by id"
    )
    @PutMapping(path = "task/change/status")
    public ResponseEntity<?> put(@RequestBody Tasks tasks) {
        return tasksService.changeStatus(tasks);
    }
}
