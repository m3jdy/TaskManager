package ru.mejdu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.mejdu.model.Task;
import ru.mejdu.service.TaskService;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
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
        model.addAttribute("task", new Task());
        return "create_task";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute Task task){
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
