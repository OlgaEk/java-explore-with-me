package ru.practicum.ewm.category.model;

import lombok.Getter;
import lombok.Setter;
import ru.practicum.ewm.category.validator.CategoryIdExist;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String name;
}
