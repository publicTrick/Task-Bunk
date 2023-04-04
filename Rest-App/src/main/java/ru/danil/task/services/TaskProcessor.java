package ru.danil.task.services;

import org.springframework.stereotype.Component;
import ru.danil.task.models.Task;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class TaskProcessor {

    private final TaskQueue taskQueue;
    private final TaskRepository taskRepository;
    private final ExecutorService executorService;
    private boolean isRecording;

    public TaskProcessor(TaskQueue taskQueue, TaskRepository taskRepository) {
        this.taskQueue = taskQueue;
        this.taskRepository = taskRepository;
        this.executorService = Executors.newFixedThreadPool(3);
        this.isRecording = false;
    }

    public void start() {
        if (!isRecording) {
            isRecording = true;
            executorService.submit(this::consumeQueue);
        }
    }

    public void stop() {
        executorService.shutdown();
    }

    private void consumeQueue() {
        try {
            while (!Thread.currentThread().isInterrupted()) {
                List<Task> tasks = taskQueue.takeAll();
                if (tasks.isEmpty()) {
                    isRecording = false;
                    return;
                }
                for (Task task : tasks) {
                    taskRepository.create(task);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}



