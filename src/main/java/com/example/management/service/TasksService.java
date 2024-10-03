package com.example.management.service;

import com.example.management.entity.Tasks;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TasksService {

    /**
     * Удаление задачи по id
     */
    void deleteById(Integer taskId);

    /**
     * Проверка параметров задачу
     */
    void checkParam(Tasks tasks);

    /**
     * Создание задачи
     *
     * @return созданная задача
     */
    Tasks create(Tasks tasks);

    /**
     * Получение всех задач
     *
     * @return все задачи
     */
    List<Tasks> getAll(Pageable pageable);

    /**
     * Изменение статуса задачи, только для исполнтелей задачи
     *
     * @return задача с измененным статусом
     */
    Tasks changeStatus(Integer taskId, String status);

    /**
     * Назначение исполнителя задачи, только для автора задачи
     *
     * @return задача с назначенным исполнителем
     */
    Tasks appointmentExecutor(Integer taskId, Integer executorId);

    /**
     * Получение всех задач по исполнтелю задачи
     *
     * @return список задач
     */
    List<Tasks> findTaskByExecutorSurname(String userSurname, Pageable pageable);

    /**
     * Получение всех задач по автору задачи
     *
     * @return список задач
     */
    List<Tasks> findTaskByAuthorSurname(String userSurname, Pageable pageable);

    /**
     * Изменение заголовка и описания задачи, только для автора задачи
     *
     * @return измененная задача
     */
    Tasks changeTask(Integer taskId, String title, String description);
}
