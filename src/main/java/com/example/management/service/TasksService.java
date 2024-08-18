package com.example.management.service;

import com.example.management.entity.Tasks;
import com.example.management.enums.Priority;
import com.example.management.enums.TaskStatus;
import com.example.management.repository.TasksRepository;
import com.example.management.repository.UserRepository;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TasksService {
    private TasksRepository tasksRepository;
    private UserRepository userRepository;

    @Autowired
    public TasksService(TasksRepository tasksRepository, UserRepository userRepository) {
        this.tasksRepository = tasksRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<?> existById(Integer id) {
        if (!tasksRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        return null;
    }

    public ResponseEntity<?> deleteById(Integer id) {
        ResponseEntity<?> responseEntity = existById(id);
        if (responseEntity != null) {
            return responseEntity;
        }
        tasksRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<?> checkParam(Tasks tasks) {
        if (tasks.getTitle() == null || tasks.getTitle().isBlank()) {
            return ResponseEntity.badRequest().body("Заголовок не передан");
        }
        if (tasks.getDescription() == null || tasks.getDescription().isBlank()) {
            return ResponseEntity.badRequest().body("Описание не передано");
        }
//        if (tasks.getStatus() == null || tasks.getStatus().isBlank()) {
//            return ResponseEntity.badRequest().body("Статус не передан");
//        }
        if (tasks.getPriority() == null || tasks.getPriority().isBlank()) {
            return ResponseEntity.badRequest().body("Приоритет не передан");
        }
        if (tasks.getAuthor() == null) {
            return ResponseEntity.badRequest().body("Автор не передан");
        }
        if (!userRepository.existsById(tasks.getAuthor().getId())){
            return ResponseEntity.badRequest().body("Пользователь(автор) не найден");
        }
        if (!userRepository.existsById(tasks.getExecutor().getId())){
            return ResponseEntity.badRequest().body("Пользователь(исполнитель) не найден");
        }
        return null;
    }
    public ResponseEntity<?> create(Tasks tasks) {
        tasks.setId(null);
        tasks.setStatus("pending");
        ResponseEntity<?> responseEntity = checkParam(tasks);
        if (responseEntity != null) {
            return responseEntity;
        }
        if (!EnumUtils.isValidEnum(Priority.class, tasks.getPriority())) {
            return ResponseEntity.badRequest().body("Приоритет задачи задан не верно");
        }
        return ResponseEntity.ok(tasksRepository.save(tasks));
    }
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(tasksRepository.findAll());
    }

    public ResponseEntity<?> changeStatus(Tasks tasks){
        ResponseEntity<?> responseEntity = checkParam(tasks);
        if (responseEntity != null) {
            return responseEntity;
        }
        if (!EnumUtils.isValidEnum(TaskStatus.class, tasks.getStatus())) {
            return ResponseEntity.badRequest().body("Статус задачи задан не верно");
        }
        return ResponseEntity.ok(tasksRepository.save(tasks));
    }
}
