package ru.danil.task.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.danil.task.models.Task;
import ru.danil.task.models.TaskDTO;
import ru.danil.task.util.TaskNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    public TaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Task task) {
        String sql = "INSERT INTO tasks (title, description, time, status, performer_id) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, task.getTitle(), task.getDescription(), task.getTime(), task.getStatus(), task.getPerformerId());
    }

    public List<TaskDTO> getAllTaskSummaries() {
        String sql = "SELECT id, title, status FROM tasks";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            TaskDTO summary = new TaskDTO(rs.getLong("id"), rs.getString("title"),
                    rs.getString("status"));
            return summary;
        });
    }

    public Task getTaskById(Long taskId) {
        String sql = "SELECT t.id, t.title, t.description, t.status, t.time, t.performer_id " +
                "FROM tasks t LEFT JOIN workers tp ON t.performer_id = tp.id " +
                "WHERE t.id = ?";

        List<Task> tasks = jdbcTemplate.query(sql, new Object[]{taskId}, (rs, rowNum) -> {
            Task task = new Task();
            task.setId(rs.getLong("id"));
            task.setTitle(rs.getString("title"));
            task.setDescription(rs.getString("description"));
            task.setStatus(rs.getString("status"));
            task.setTime(rs.getTimestamp("time").toLocalDateTime());
            Long performerId = rs.getLong("performer_id");
            if(rs.wasNull()) {
                task.setPerformerId(null);
            } else {
                task.setPerformerId(performerId);
            }
            return task;
        });

        if(tasks.isEmpty()) {
            return null;
         //   throw new RuntimeException("Task not found");
        }
        return tasks.get(0);
    }


    public Task updateTask(Long taskId, Task updatedTask) {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, time = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, updatedTask.getTitle(), updatedTask.getDescription(),
                updatedTask.getStatus().toString(), LocalDateTime.now(), taskId);

        if (rowsUpdated == 0) {
            throw new TaskNotFoundException(taskId);
        }
        Task task = getTaskById(taskId);
        task.setTitle(updatedTask.getTitle());
        task.setDescription(updatedTask.getDescription());
        task.setStatus(updatedTask.getStatus());
        task.setTime(LocalDateTime.now());

        return task;
    }

    public void assignPerformer(Long taskId, Long performerId) {
        String sql = "UPDATE tasks SET performer_id = ? WHERE id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, performerId, taskId);
        if (rowsUpdated == 0) {
            throw new TaskNotFoundException(taskId);
        }
    }
}

