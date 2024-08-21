package com.example.management.service;

import com.example.management.entity.Tasks;
import com.example.management.entity.User;
import com.example.management.enums.Priority;
import com.example.management.enums.TaskStatus;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.TasksRepository;
import com.example.management.repository.UserRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TasksService {
    private TasksRepository tasksRepository;
    private UserRepository userRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository, UserRepository userRepository) {
        this.tasksRepository = tasksRepository;
        this.userRepository = userRepository;
    }

    public Tasks existById(Integer id) {
        if (!tasksRepository.existsById(id)) {
            throw new ApiException("Задача не найдена", 404);
        }
        return null;
    }


    public void deleteById(Integer taskId) {
        Optional<Tasks> tasks = tasksRepository.findById(taskId);
        if (existById(taskId) == null) {
            throw new ApiException("Задача не найдена", 404);
        }
        if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() == tasks.get().getAuthor().getId()) {
            throw new ApiException("Пользователь не автор задачи", 403);
        }
        tasksRepository.deleteById(taskId);
    }

    public void checkParam(Tasks tasks) {
        if (tasks.getTitle() == null || tasks.getTitle().isBlank()) {
            throw new ApiException("Заголовок не передан", 400);
        }
        if (tasks.getDescription() == null || tasks.getDescription().isBlank()) {
            throw new ApiException("Описание не передано", 400);
        }
        if (tasks.getPriority() == null || tasks.getPriority().isBlank()) {
            throw new ApiException("Приоритет не передан", 400);
        }
        if (!userRepository.existsById(tasks.getAuthor().getId())) {
            throw new ApiException("Пользователь(автор) не найден", 404);
        }
    }

    public Tasks create(Tasks tasks) {
        tasks.setId(null);
        tasks.setStatus("pending");
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tasks.setAuthor(currentUser);
        checkParam(tasks);
        if (!EnumUtils.isValidEnum(Priority.class, tasks.getPriority())) {
            throw new ApiException("Приоритет задачи задан не верно", 400);
        }
        return tasksRepository.save(tasks);
    }

    public List<Tasks> getAll() {
        return tasksRepository.findAll();
    }

    public Tasks changeStatus(Integer taskId, String status) {
        Optional<Tasks> task = tasksRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ApiException("Задача не найдена", 404);
        }
       User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(task.get().getExecutor().getId())) {
            throw new ApiException("Пользователь не исполнитель задачи", 403);
        }
        if (!EnumUtils.isValidEnum(TaskStatus.class, status)) {
            throw new ApiException("Статус задачи задан не верно", 400);
        }
        task.get().setStatus(status);
        return tasksRepository.save(task.get());
    }

    public Tasks appointmentExecutor(Integer taskId, Integer executorId) {
        Optional<Tasks> task = tasksRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ApiException("Задача не найдена", 404);
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(task.get().getAuthor().getId())) {
            throw new ApiException("Пользователь не автор задачи", 403);
        }
        task.get().setExecutor(userRepository.findById(executorId).get());
        return tasksRepository.save(task.get());
    }

}

