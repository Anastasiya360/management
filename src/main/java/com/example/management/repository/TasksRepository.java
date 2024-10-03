package com.example.management.repository;

import com.example.management.entity.Tasks;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    @Query(nativeQuery = true, value = "select tasks.id, tasks.title, tasks.description, tasks.status, tasks.priority, tasks.user_id_author, tasks.user_id_executor from management.tasks" +
            "         join management.users u on u.id = tasks.user_id_executor " +
            "         where u.surname like concat('%', :userSurname, '%')")
    List<Tasks> findTaskByExecutorSurname (String userSurname);

    @Query(nativeQuery = true, value = "select tasks.id, tasks.title, tasks.description, tasks.status, tasks.priority, tasks.user_id_author, tasks.user_id_executor from management.tasks" +
            "         join management.users u on u.id = tasks.user_id_executor " +
            "         where u.surname like concat('%', :userSurname, '%')")
    Page<Tasks> findTaskByExecutorSurname (String userSurname, Pageable pageable);

    @Query(nativeQuery = true, value = "select tasks.id, tasks.title, tasks.description, tasks.status, tasks.priority, tasks.user_id_author, tasks.user_id_executor from management.tasks" +
            "         join management.users u on u.id = tasks.user_id_author " +
            "         where u.surname like concat('%', :userSurname, '%')")
    List<Tasks> findTaskByAuthorSurname (String userSurname);

    @Query(nativeQuery = true, value = "select tasks.id, tasks.title, tasks.description, tasks.status, tasks.priority, tasks.user_id_author, tasks.user_id_executor from management.tasks" +
            "         join management.users u on u.id = tasks.user_id_author " +
            "         where u.surname like concat('%', :userSurname, '%')")
    Page<Tasks> findTaskByAuthorSurname (String userSurname, Pageable pageable);
}
