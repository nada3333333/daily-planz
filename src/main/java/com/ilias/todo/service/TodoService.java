package com.ilias.todo.service;

import com.ilias.todo.model.Todo;
import com.ilias.todo.repository.TodoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // جيب كل المهام
    public List<Todo> getAllTodos() {
        return todoRepository.findAllByOrderByCreatedAtDesc();
    }

    // جيب مهمة بـ ID
    public Optional<Todo> getTodoById(Long id) {
        return todoRepository.findById(id);
    }

    // سجل مهمة جديدة
    public Todo saveTodo(Todo todo) {
        return todoRepository.save(todo);
    }

    // حذف مهمة
    public void deleteTodo(Long id) {
        todoRepository.deleteById(id);
    }

    // بدّل الحالة (مكتمل / غير مكتمل)
    public void toggleCompleted(Long id) {
        todoRepository.findById(id).ifPresent(todo -> {
            todo.setCompleted(!todo.isCompleted());
            todoRepository.save(todo);
        });
    }

    // عدد المهام غير المكتملة
    public long countPending() {
        return todoRepository.findByCompletedFalse().size();
    }

    // عدد المهام المكتملة
    public long countCompleted() {
        return todoRepository.findByCompletedTrue().size();
    }
}
