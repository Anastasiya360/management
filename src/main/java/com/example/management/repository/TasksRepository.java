package com.example.management.repository;

import com.example.management.entity.Tasks;
import com.example.management.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    @Query(nativeQuery = true, value = "select * from management.tasks\n" +
            "         join management.users u on u.id = tasks.user_id_executor\n" +
            "         where u.surname like '%:userSurname%';")
    Optional<Tasks> findTaskByExecutorSurname (String userSurname);
}
