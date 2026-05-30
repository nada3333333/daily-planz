package com.ilias.todo.controller;

import com.ilias.todo.model.Todo;
import com.ilias.todo.service.TodoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    // ===== عرض كل المهام =====
    @GetMapping
    public String listTodos(Model model) {
        model.addAttribute("todos", todoService.getAllTodos());
        model.addAttribute("newTodo", new Todo());
        model.addAttribute("pending", todoService.countPending());
        model.addAttribute("completed", todoService.countCompleted());
        return "todos/index";
    }

    // ===== إضافة مهمة جديدة =====
    @PostMapping("/add")
    public String addTodo(@Valid @ModelAttribute("newTodo") Todo todo,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("todos", todoService.getAllTodos());
            model.addAttribute("pending", todoService.countPending());
            model.addAttribute("completed", todoService.countCompleted());
            return "todos/index";
        }
        todoService.saveTodo(todo);
        redirectAttributes.addFlashAttribute("successMsg", "Tâche ajoutée avec succès !");
        return "redirect:/todos";
    }

    // ===== عرض فورم التعديل =====
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Todo todo = todoService.getTodoById(id)
                .orElseThrow(() -> new IllegalArgumentException("ID invalide: " + id));
        model.addAttribute("todo", todo);
        return "todos/edit";
    }

    // ===== حفظ التعديل =====
    @PostMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id,
                             @Valid @ModelAttribute("todo") Todo todo,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "todos/edit";
        }
        todo.setId(id);
        todoService.saveTodo(todo);
        redirectAttributes.addFlashAttribute("successMsg", "Tâche modifiée avec succès !");
        return "redirect:/todos";
    }

    // ===== حذف مهمة =====
    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        todoService.deleteTodo(id);
        redirectAttributes.addFlashAttribute("successMsg", "Tâche supprimée.");
        return "redirect:/todos";
    }

    // ===== تبديل حالة المهمة =====
    @GetMapping("/toggle/{id}")
    public String toggleTodo(@PathVariable Long id) {
        todoService.toggleCompleted(id);
        return "redirect:/todos";
    }
}
