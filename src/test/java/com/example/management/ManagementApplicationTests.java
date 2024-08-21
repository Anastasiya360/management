package com.example.management;

import com.example.management.entity.Tasks;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.UserRepository;
import com.example.management.service.TasksService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class ManagementApplicationTests {
    private TasksService tasksService;
    private UserRepository userRepository;

    @Autowired
    public ManagementApplicationTests(TasksService tasksService, UserRepository userRepository) {
        this.tasksService = tasksService;
        this.userRepository = userRepository;
    }

    @Test
    void testTaskCreateTitleNull() {
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            Tasks tasks = new Tasks(2, null, "Описание задачи", "pending", "low", userRepository.findById(4).get());
            Tasks tasks1 = tasksService.create(tasks, 4);
        });
        Assertions.assertEquals("Заголовок не передан", thrown.getMessage());
    }

    @Test
    void testTaskCreateTitleIsBlank() {
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            Tasks tasks = new Tasks(2, " ", "Описание задачи", "pending", "low", userRepository.findById(4).get());
            Tasks tasks1 = tasksService.create(tasks, 4);
        });
        Assertions.assertEquals("Заголовок не передан", thrown.getMessage());
    }

    @Test
    void testTaskCreateDescriptionNull() {
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            Tasks tasks = new Tasks(2, "Заголовок", null, "pending", "low", userRepository.findById(4).get());
            Tasks tasks1 = tasksService.create(tasks, 4);
        });
        Assertions.assertEquals("Описание не передано", thrown.getMessage());
    }

    @Test
    void testTaskCreateDescriptionIsBlank() {
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            Tasks tasks = new Tasks(2, "Заголовок", " ", "pending", "low", userRepository.findById(4).get());
            Tasks tasks1 = tasksService.create(tasks, 4);
        });
        Assertions.assertEquals("Описание не передано", thrown.getMessage());
    }

    @Test
    void testTaskCreatePriorityNull() {
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            Tasks tasks = new Tasks(2, "Заголовок", "Описание задачи", "pending", null, userRepository.findById(4).get());
            Tasks tasks1 = tasksService.create(tasks, 4);
        });
        Assertions.assertEquals("Приоритет не передан", thrown.getMessage());
    }
    @Test
    void testTaskCreatePriorityIsBlank() {
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            Tasks tasks = new Tasks(2, "Заголовок", "Описание задачи", "pending", " ", userRepository.findById(4).get());
            Tasks tasks1 = tasksService.create(tasks, 4);
        });
        Assertions.assertEquals("Приоритет не передан", thrown.getMessage());
    }
}
