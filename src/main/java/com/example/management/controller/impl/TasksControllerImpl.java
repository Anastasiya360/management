package com.example.management.controller.impl;

import com.example.management.controller.TasksController;
import com.example.management.entity.Tasks;
import com.example.management.service.TasksService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TasksControllerImpl implements TasksController {

    private final TasksService tasksService;

    @Override
    public void deleteById(@PathVariable Integer id) {
        tasksService.deleteById(id);
    }

    @Override
    public Tasks create(@RequestBody Tasks tasks) {
        return tasksService.create(tasks);
    }

    @Override
    public List<Tasks> getAll(@RequestParam(required = false) Integer pageNum,
                              @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return tasksService.getAll(null);
        }
        return tasksService.getAll(PageRequest.of(pageNum, pageSize));
    }

    @Override
    public void changeStatus(@PathVariable Integer id, @RequestParam String status) {
        tasksService.changeStatus(id, status);
    }

    @Override
    public void appointmentExecutor(@PathVariable Integer id, @RequestParam Integer executorId) {
        tasksService.appointmentExecutor(id, executorId);
    }

    @Override
    public List<Tasks> findTaskByExecutorSurname(@RequestParam String userSurname, @RequestParam(required = false) Integer pageNum,
                                                 @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return tasksService.findTaskByExecutorSurname(userSurname, null);
        }
        return tasksService.findTaskByExecutorSurname(userSurname, PageRequest.of(pageNum, pageSize));

    }

    @Override
    public List<Tasks> findTaskByAuthorSurname(@RequestParam String userSurname, @RequestParam(required = false) Integer pageNum,
                                               @RequestParam(required = false) Integer pageSize) {
        if (pageNum == null || pageSize == null) {
            return tasksService.findTaskByAuthorSurname(userSurname, null);
        }
        return tasksService.findTaskByAuthorSurname(userSurname, PageRequest.of(pageNum, pageSize));
    }

    @Override
    public void changeTask(@PathVariable Integer id, @RequestParam String title, @RequestParam String description) {
        tasksService.changeTask(id, title, description);
    }
}
