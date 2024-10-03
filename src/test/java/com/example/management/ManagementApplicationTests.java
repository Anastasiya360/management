package com.example.management;

import com.example.management.entity.Comment;
import com.example.management.entity.Tasks;
import com.example.management.entity.User;
import com.example.management.exceptoin.ApiException;
import com.example.management.repository.CommentRepository;
import com.example.management.repository.TasksRepository;
import com.example.management.repository.UserRepository;
import com.example.management.service.impl.CommentServiceImpl;
import com.example.management.service.impl.TasksServiceImpl;
import com.example.management.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


@SpringBootTest
class ManagementApplicationTests {
    @Autowired
    private TasksServiceImpl tasksServiceImpl;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private CommentServiceImpl commentServiceImpl;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private TasksRepository tasksRepository;

    @MockBean
    private CommentRepository commentRepository;


    @Test
    void testTaskDeleteNotFound() {
        Mockito.when(tasksRepository.findById(1)).thenReturn(Optional.empty());

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.deleteById(1);
        });
        Assertions.assertEquals(404, thrown.getStatusCode());
        Assertions.assertEquals("Задача не найдена", thrown.getMessage());
    }

    @Test
    void testTaskDeleteForbidden() {
        Tasks task = new Tasks();
        task.setId(1);
        User author = new User();
        author.setId(1);
        task.setAuthor(author);
        Mockito.when(tasksRepository.findById(1)).thenReturn(Optional.of(task));

        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.deleteById(1);
        });
        Assertions.assertEquals(403, thrown.getStatusCode());
        Assertions.assertEquals("Пользователь не автор задачи", thrown.getMessage());
    }

    @Test
    void testTaskDeleteSuccess() {
        Tasks task = new Tasks();
        task.setId(6);
        User author = new User();
        author.setId(1);
        task.setAuthor(author);
        Mockito.when(tasksRepository.findById(6)).thenReturn(Optional.of(task));

        mockSecurity(1);

        Assertions.assertDoesNotThrow(() -> {
            tasksServiceImpl.deleteById(6);
        });
    }

    @Test
    void testTaskCreateTitleNull() {
        Tasks task = new Tasks(2, null, "Description", "pending", "low");
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Заголовок не передан", thrown.getMessage());
    }

    @Test
    void testTaskCreateTitleIsBlank() {
        Tasks task = new Tasks(2, " ", "Description", "pending", "low");
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Заголовок не передан", thrown.getMessage());
    }

    @Test
    void testTaskCreateDescriptionNull() {
        Tasks task = new Tasks(2, "Title", null, "pending", "low");
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Описание не передано", thrown.getMessage());
    }

    @Test
    void testTaskCreateDescriptionIsBlank() {
        Tasks task = new Tasks(2, "Title", "", "pending", "low");
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Описание не передано", thrown.getMessage());
    }

    @Test
    void testTaskCreatePriorityIsBlank() {
        Tasks task = new Tasks(2, "Title", "Description", "pending", "");
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Приоритет не передан", thrown.getMessage());
    }

    @Test
    void testTaskCreatePriorityNull() {
        Tasks task = new Tasks(2, "Title", "Description", "pending", null);
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Приоритет не передан", thrown.getMessage());
    }

    @Test
    void testTaskCreatePriorityNotValid() {
        Tasks task = new Tasks(2, "Title", "Description", "pending", "big");
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.create(task);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Приоритет задачи задан не верно", thrown.getMessage());
    }

    @Test
    void testTaskCreatePrioritySuccess() {
        Tasks task = new Tasks(2, "Title", "Description", "pending", "low");
        mockSecurity(12);

        Assertions.assertDoesNotThrow(() -> {
            tasksServiceImpl.create(task);
        });
    }

    @Test
    void testTaskChangeStatusNotFound() {
        Mockito.when(tasksRepository.findById(1)).thenReturn(Optional.empty());

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.changeStatus(1,"inProgress");
        });
        Assertions.assertEquals(404, thrown.getStatusCode());
        Assertions.assertEquals("Задача не найдена", thrown.getMessage());
    }

    @Test
    void testTaskChangeStatusForbidden() {
        User user = new User();
        user.setId(1);
        Tasks task = new Tasks(2, "Title", "Description", "pending", "big", user, user);
        Mockito.when(tasksRepository.findById(2)).thenReturn(Optional.of(task));
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.changeStatus(2,"inProgress");
        });
        Assertions.assertEquals(403, thrown.getStatusCode());
        Assertions.assertEquals("Пользователь не исполнитель задачи", thrown.getMessage());
    }

    @Test
    void testTaskChangeStatusNotValid() {
        User user = new User();
        user.setId(12);
        Tasks task = new Tasks(2, "Title", "Description", "pending", "big", user, user);
        Mockito.when(tasksRepository.findById(2)).thenReturn(Optional.of(task));
        mockSecurity(12);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.changeStatus(2,"low");
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Статус задачи задан не верно", thrown.getMessage());
    }

    @Test
    void testTaskChangeStatusSuccess() {
        User user = new User();
        user.setId(12);
        Tasks task = new Tasks(2, "Title", "Description", "pending", "big", user, user);
        Mockito.when(tasksRepository.findById(2)).thenReturn(Optional.of(task));
        mockSecurity(12);

        Assertions.assertDoesNotThrow(() -> {
            tasksServiceImpl.changeStatus(2, "inProgress");
        });
    }
    @Test
    void testTaskAppointmentExecutorNotFound() {
        Mockito.when(tasksRepository.findById(1)).thenReturn(Optional.empty());
        mockSecurity(12);
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.appointmentExecutor(1,12);
        });
        Assertions.assertEquals(404, thrown.getStatusCode());
        Assertions.assertEquals("Задача не найдена", thrown.getMessage());
    }

    @Test
    void testTaskAppointmentExecutorForbidden() {
        User author = new User();
        author.setId(12);
        User executor = new User();
        executor.setId(5);
        Tasks task = new Tasks(2, "Title", "Description", "pending", "low", author);
        Mockito.when(tasksRepository.findById(2)).thenReturn(Optional.of(task));
        mockSecurity(4);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            tasksServiceImpl.appointmentExecutor(2,5);
        });
        Assertions.assertEquals(403, thrown.getStatusCode());
        Assertions.assertEquals("Пользователь не автор задачи", thrown.getMessage());
    }

    @Test
    void testTaskAppointmentExecutorSuccess() {
        User author = new User();
        author.setId(12);
        User executor = new User();
        executor.setId(5);
        Tasks task = new Tasks(2, "Title", "Description", "pending", "low", author);
        Mockito.when(tasksRepository.findById(2)).thenReturn(Optional.of(task));
        Mockito.when(userRepository.findById(5)).thenReturn(Optional.of(executor));
        mockSecurity(12);

        Assertions.assertDoesNotThrow(() -> {
            tasksServiceImpl.appointmentExecutor(2,5);
        });
    }

    @Test
    void testUserCreateNameNull() {
        User user = new User(null,"Surname","123@gmail.com", "12345678");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Имя не передано", thrown.getMessage());
    }

    @Test
    void testUserCreateNameIsBlank() {
        User user = new User("","Surname","123@gmail.com", "12345678");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Имя не передано", thrown.getMessage());
    }

    @Test
    void testUserCreateEmailNull() {
        User user = new User("Name","Surname",null, "12345678");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Email не передан или имеет менее 5 символов", thrown.getMessage());
    }

    @Test
    void testUserCreateEmailIsBlank() {
        User user = new User("Name","Surname","", "12345678");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Email не передан или имеет менее 5 символов", thrown.getMessage());
    }

    @Test
    void testUserCreateEmailNotValid() {
        User user = new User("Name","Surname","1234", "12345678");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Email не передан или имеет менее 5 символов", thrown.getMessage());
    }

    @Test
    void testUserCreatePasswordIsBlank() {
        User user = new User("Name","Surname","123@gmail.com", null);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Пароль не передан или имеет менее 8 символов", thrown.getMessage());
    }

    @Test
    void testUserCreatePasswordNull() {
        User user = new User("Name","Surname","123@gmail.com", "");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Пароль не передан или имеет менее 8 символов", thrown.getMessage());
    }

    @Test
    void testUserCreatePasswordNotValid() {
        User user = new User("Name","Surname","123@gmail.com", "12345");

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.create(user);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Пароль не передан или имеет менее 8 символов", thrown.getMessage());
    }

    @Test
    void testUserGetByEmailNotFound() {
        Mockito.when(userRepository.findUserByEmail("123@gmail.com")).thenReturn(null);

        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            userService.getByUserEmail("123@gmail.com");
        });
        Assertions.assertEquals(404, thrown.getStatusCode());
        Assertions.assertEquals("Пользователь не найден", thrown.getMessage());
    }

    @Test
    void testUserGetByEmailSuccess() {
        User user = new User("Name", "Surname", "123@gmail.com", "12345678");
        Mockito.when(userRepository.findUserByEmail("123@gmail.com")).thenReturn(user);

        Assertions.assertDoesNotThrow(() -> {
            userRepository.findUserByEmail("123@gmail.com");
        });
    }

    @Test
    void testCommentCreateDescriptionNull() {
        Tasks tasks = new Tasks();
        tasks.setId(2);
        User author = new User();
        author.setId(4);
        Comment comment = new Comment(author,null,2);
        mockSecurity(4);
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            commentServiceImpl.create(comment);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Описание не передано", thrown.getMessage());
    }

    @Test
    void testCommentCreateDescriptionIsBlank() {
        Tasks tasks = new Tasks();
        tasks.setId(2);
        User author = new User();
        author.setId(4);
        Comment comment = new Comment(author,"",2);
        mockSecurity(4);
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            commentServiceImpl.create(comment);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Описание не передано", thrown.getMessage());
    }

    @Test
    void testCommentCreateTaskIdNull() {
        User author = new User();
        author.setId(4);
        Comment comment = new Comment(author,"Comment",null);
        mockSecurity(4);
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            commentServiceImpl.create(comment);
        });
        Assertions.assertEquals(400, thrown.getStatusCode());
        Assertions.assertEquals("Задача не передана", thrown.getMessage());
    }

    @Test
    void testCommentCreateTaskIdNotValid() {
        User author = new User();
        author.setId(4);
        Mockito.when(tasksRepository.findById(2)).thenReturn(Optional.empty());
        Comment comment = new Comment(author,"Comment",2);
        mockSecurity(4);
        ApiException thrown = Assertions.assertThrows(ApiException.class, () -> {
            commentServiceImpl.create(comment);
        });
        Assertions.assertEquals(404, thrown.getStatusCode());
        Assertions.assertEquals("Задача не найдена", thrown.getMessage());
    }

    @Test
    void testCommentCreateSuccess() {
        User author = new User();
        author.setId(4);

        Mockito.when(tasksRepository.existsById(2)).thenReturn(true);
        Comment comment = new Comment(author,"Comment",2);
        mockSecurity(4);
        Assertions.assertDoesNotThrow(() -> {
            commentServiceImpl.create(comment);
        });
    }

    private void mockSecurity(Integer currentUserIdMock) {
        User currentUserMock = new User();
        currentUserMock.setId(currentUserIdMock);

        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(currentUserMock);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

}
