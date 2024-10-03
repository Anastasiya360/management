package com.example.management.service.impl;

import com.example.management.entity.Tasks;
import com.example.management.entity.User;
import com.example.management.enums.Priority;
import com.example.management.enums.TaskStatus;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.TasksRepository;
import com.example.management.repository.UserRepository;
import com.example.management.service.TasksService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TasksServiceImpl implements TasksService {

    private final TasksRepository tasksRepository;

    private final UserRepository userRepository;

    @Override
    public void deleteById(Integer taskId) {
        Optional<Tasks> tasks = tasksRepository.findById(taskId);
        if (tasks.isEmpty()) {
            throw new ApiException("Задача не найдена", HttpServletResponse.SC_NOT_FOUND);
        }
        if (!((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId().equals(tasks.get().getAuthor().getId())) {
            throw new ApiException("Пользователь не автор задачи", HttpServletResponse.SC_FORBIDDEN);
        }
        tasksRepository.deleteById(taskId);
    }

    @Override
    public void checkParam(Tasks tasks) {
        if (tasks.getTitle() == null || tasks.getTitle().isBlank()) {
            throw new ApiException("Заголовок не передан", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (tasks.getDescription() == null || tasks.getDescription().isBlank()) {
            throw new ApiException("Описание не передано", HttpServletResponse.SC_BAD_REQUEST);
        }
        if (tasks.getPriority() == null || tasks.getPriority().isBlank()) {
            throw new ApiException("Приоритет не передан", HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    public Tasks create(Tasks tasks) {
        tasks.setId(null);
        tasks.setStatus("pending");
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        tasks.setAuthor(currentUser);
        checkParam(tasks);
        if (!EnumUtils.isValidEnum(Priority.class, tasks.getPriority())) {
            throw new ApiException("Приоритет задачи задан не верно", HttpServletResponse.SC_BAD_REQUEST);
        }
        return tasksRepository.save(tasks);
    }

    @Override
    public List<Tasks> getAll(Pageable pageable) {
        if (pageable == null) {
            return tasksRepository.findAll();
        } else {
            return tasksRepository.findAll(pageable).getContent();
        }
    }

    @Override
    public Tasks changeStatus(Integer taskId, String status) {
        Optional<Tasks> task = tasksRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ApiException("Задача не найдена", HttpServletResponse.SC_NOT_FOUND);
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(task.get().getExecutor().getId())) {
            throw new ApiException("Пользователь не исполнитель задачи", HttpServletResponse.SC_FORBIDDEN);
        }
        if (!EnumUtils.isValidEnum(TaskStatus.class, status)) {
            throw new ApiException("Статус задачи задан не верно", HttpServletResponse.SC_BAD_REQUEST);
        }
        task.get().setStatus(status);
        return tasksRepository.save(task.get());
    }

    @Override
    public Tasks appointmentExecutor(Integer taskId, Integer executorId) {
        Optional<Tasks> task = tasksRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ApiException("Задача не найдена", HttpServletResponse.SC_NOT_FOUND);
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(task.get().getAuthor().getId())) {
            throw new ApiException("Пользователь не автор задачи", HttpServletResponse.SC_FORBIDDEN);
        }
        task.get().setExecutor(userRepository.findById(executorId).get());
        return tasksRepository.save(task.get());
    }

    @Override
    public List<Tasks> findTaskByExecutorSurname(String userSurname, Pageable pageable) {
        if (pageable == null) {
            return tasksRepository.findTaskByExecutorSurname(userSurname);
        } else {
            return tasksRepository.findTaskByExecutorSurname(userSurname, pageable).getContent();
        }
    }

    @Override
    public List<Tasks> findTaskByAuthorSurname(String userSurname, Pageable pageable) {
        if (pageable == null) {
            return tasksRepository.findTaskByAuthorSurname(userSurname);
        } else {
            return tasksRepository.findTaskByAuthorSurname(userSurname, pageable).getContent();
        }
    }

    @Override
    public Tasks changeTask(Integer taskId, String title, String description) {
        Optional<Tasks> task = tasksRepository.findById(taskId);
        if (task.isEmpty()) {
            throw new ApiException("Задача не найдена", HttpServletResponse.SC_NOT_FOUND);
        }
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!currentUser.getId().equals(task.get().getAuthor().getId())) {
            throw new ApiException("Пользователь не автор задачи", HttpServletResponse.SC_FORBIDDEN);
        }
        task.get().setTitle(title);
        task.get().setDescription(description);
        return tasksRepository.save(task.get());
    }
}

