package ru.danil.task.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danil.task.models.Task;
import ru.danil.task.models.TaskDTO;
import ru.danil.task.services.TaskProcessor;
import ru.danil.task.services.TaskQueue;
import ru.danil.task.services.TaskRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/task")
public class TaskController {
    private final TaskRepository taskRepository;
    private final TaskQueue taskQueue;
    private final TaskProcessor taskProcessor;

    public TaskController(TaskRepository taskRepository, TaskQueue taskQueue, TaskProcessor taskProcessor) {
        this.taskRepository = taskRepository;
        this.taskQueue = taskQueue;
        this.taskProcessor = taskProcessor;
    }

    @PostMapping("/add")
    public void addToQueue(@RequestBody Task task) {
        taskQueue.add(task);
    }

    @GetMapping("/record")
    public void recordQueue() {
        taskProcessor.start();
    }

    @GetMapping("/all")
    public List<TaskDTO> getAllTasks() {
        return taskRepository.getAllTaskSummaries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskRepository.getTaskById(id);

        if (task == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(task);
        }
    }

    @PutMapping("/{id}/edit")
    public ResponseEntity<String> updateTask(@PathVariable Long id, @RequestBody Task taskToUpdate) {
        Task currentTask = taskRepository.getTaskById(id);

        if (currentTask == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task with id " + id + " not found");
        }

        currentTask.setTitle(taskToUpdate.getTitle());
        currentTask.setDescription(taskToUpdate.getDescription());
        currentTask.setStatus(taskToUpdate.getStatus());
        currentTask.setTime(taskToUpdate.getTime());

        taskRepository.updateTask(id, currentTask);

        return ResponseEntity.ok("Task with id " + id + " updated successfully");
    }

    @PutMapping("/{id}/assign")
    public ResponseEntity<Void> assignPerformer(@PathVariable Long id, @RequestBody Map<String, Long> requestBody) {
        Long performerId = requestBody.get("performerId");
        taskRepository.assignPerformer(id, performerId);
        return ResponseEntity.ok().build();
    }
}





// TODO   or swap ?

//    @PutMapping("/{id}/assign")
//    public ResponseEntity<Void> assignPerformer(@PathVariable Long id, @RequestParam Long performerId) {
//        taskRepository.assignPerformer(id, performerId);
//        return ResponseEntity.ok().build();
//    }




