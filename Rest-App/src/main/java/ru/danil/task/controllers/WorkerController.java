package ru.danil.task.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.danil.task.models.TaskDTO;
import ru.danil.task.models.Worker;
import ru.danil.task.services.WorkerRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private final WorkerRepository workerRepository;

    public WorkerController(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

    @PostMapping("/create")
    public void create(@RequestBody Worker worker) {
        workerRepository.create(worker);
    }

    @GetMapping("/{id}")
    public Worker findById(@PathVariable int id) {
        return workerRepository.findById(id);
    }

    @GetMapping()
    public List<Worker> findAll() {
        return workerRepository.findAll();
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody Worker worker) {
        Optional<Worker> existingWorker = Optional.ofNullable(workerRepository.findById(id));
        if (existingWorker.isPresent()) {
            worker.setId(id);
            workerRepository.update(worker);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        workerRepository.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/tasks")
    public List<TaskDTO> getTasksByWorkerId(@PathVariable Long id) {
        return workerRepository.getTasksByWorkerId(id);
    }

}