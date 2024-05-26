package ru.mejdu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mejdu.model.Task;
import ru.mejdu.model.User;
import ru.mejdu.service.TaskService;
import ru.mejdu.service.UserService;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final UserService userService;

    @Autowired
    public TaskController(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("")
    public String getAllTasks(Model model){
        List<Task> tasks = taskService.findAll();
        tasks.sort(Comparator.comparingLong(Task::getId));
        model.addAttribute("tasks",tasks);
        return "task_list";
    }

    @GetMapping("/{taskId}")
    public String getTask(@PathVariable Long taskId, Model model){
        Task task = taskService.findById(taskId);
        model.addAttribute("task",task);
        return "task_details";
    }

    @GetMapping("/create")
    public String showCreateTaskForm(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("task", new Task());
        return "create_task";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task,
                             @RequestParam("user_id") Long userId,
                             @RequestParam("deadline") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime deadline){
        User assignedUser = userService.findById(userId);
        task.setAssignedUser(assignedUser);
        task.setCreatedAt(LocalDateTime.now());
        task.setDeadline(deadline);
        taskService.save(task);
        return "redirect:/tasks";
    }

    @GetMapping("/{taskId}/update")
    public String showEditTaskForm(@PathVariable Long taskId,Model model){
        Task task = taskService.findById(taskId);
        model.addAttribute("task", task);
        return "update_task";
    }

    @PostMapping("/{taskId}/update")
    public String updateTask(@PathVariable Long taskId, @ModelAttribute Task updatedTask){
        Task task = taskService.findById(taskId);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        taskService.save(task);
        return "redirect:/tasks";
    }

    @PostMapping("/{taskId}/delete")
    public String deleteTask(@PathVariable Long taskId){
        taskService.deleteById(taskId);
        return "redirect:/tasks";
    }
}
