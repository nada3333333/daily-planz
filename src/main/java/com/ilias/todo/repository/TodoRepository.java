package com.ilias.todo.repository;

import com.ilias.todo.model.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    // كل المهام مرتبة من الأحدث للأقدم
    List<Todo> findAllByOrderByCreatedAtDesc();

    // المهام المكتملة فقط
    List<Todo> findByCompletedTrue();

    // المهام غير المكتملة فقط
    List<Todo> findByCompletedFalse();
}
