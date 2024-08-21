package com.example.management.controller;

import com.example.management.entity.Tasks;
import com.example.management.entity.User;
import com.example.management.service.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public void deleteById(@PathVariable Integer id) {

    }

    @Operation(
            summary = "Create task"
    )
    @PostMapping(path = "task/create")
    public Tasks create(@RequestBody Tasks tasks) {
        return tasksService.create(tasks);
    }

    @Operation(
            summary = "Get information about all tasks"
    )
    @GetMapping(path = "task/get/all")
    public List<Tasks> getAll() {
        return tasksService.getAll();
    }

    @Operation(
            summary = "Change task's status",
            description = "Change task's status by id, only for executor"
    )
    @PutMapping(path = "task/change/status/{id}")
    public void changeStatus(@PathVariable Integer id, @RequestParam String status) {
        tasksService.changeStatus(id, status);
    }

    @Operation(
            summary = "Change task's executor",
            description = "Change task's executor by id, only for author"
    )
    @PutMapping(path = "task/change/executor/{id}")
    public void appointmentExecutor(@PathVariable Integer id, @RequestParam Integer executorId) {
        tasksService.appointmentExecutor(id, executorId);
    }
}
