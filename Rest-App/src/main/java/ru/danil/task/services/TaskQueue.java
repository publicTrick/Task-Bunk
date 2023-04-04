package ru.danil.task.services;

import org.springframework.stereotype.Service;
import ru.danil.task.models.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


@Service
public class TaskQueue {

    private final BlockingQueue<Task> queue = new LinkedBlockingQueue<>();

    public void add(Task task) {
        queue.offer(task);
    }

    public List<Task> takeAll() throws InterruptedException {
        List<Task> tasks = new ArrayList<>();
        queue.drainTo(tasks);
        return tasks;
    }
}