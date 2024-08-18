package com.example.management.entity;

import com.example.management.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(schema = "management", name = "tasks")
public class Tasks implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "status")
    private String status;

    @Column(name = "priority")
    private String priority;

    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id_author")
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.DETACH)
    private User author;

    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id_executor")
    @ManyToOne(targetEntity = User.class, cascade = CascadeType.DETACH)
    private User executor;

    @Transient
    private UserDto userAuthorDto;

    @Transient
    private UserDto userExecutorDto;

    @PostLoad
    public void postLoad(){
        if (author != null){
            userAuthorDto = new UserDto(author.getName(),author.getSurname());
        }
        if (executor != null){
            userExecutorDto = new UserDto(executor.getName(),executor.getSurname());
        }
    }
}
