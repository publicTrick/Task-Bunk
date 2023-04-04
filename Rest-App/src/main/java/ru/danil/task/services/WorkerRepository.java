package ru.danil.task.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.danil.task.models.TaskDTO;
import ru.danil.task.models.Worker;

import java.util.List;

@Service
public class WorkerRepository {
    private final JdbcTemplate jdbcTemplate;

    public WorkerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Worker worker) {
        String sql = "INSERT INTO workers (name, position, avatar) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, worker.getName(), worker.getPosition(), worker.getAvatar());
    }

    public Worker findById(long id) {
        String sql = "SELECT id, name, position, avatar FROM workers WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, (rs, rowNum) -> {
            Worker worker = new Worker();
            worker.setId((long) rs.getInt("id"));
            worker.setName(rs.getString("name"));
            worker.setPosition(rs.getString("position"));
            worker.setAvatar(rs.getString("avatar"));
            return worker;
        });
    }

    public List<Worker> findAll() {
        String query = "SELECT * FROM workers";
        return jdbcTemplate.query(query, (rs, rowNum) -> {
            Worker worker = new Worker();
            worker.setId(rs.getLong("id"));
            worker.setName(rs.getString("name"));
            worker.setPosition(rs.getString("position"));
            worker.setAvatar(rs.getString("avatar"));
            return worker;
        });
    }

    public void update(Worker worker) {
        String query = "UPDATE workers SET name = ?, position = ?, avatar = ? WHERE id = ?";
        jdbcTemplate.update(query, worker.getName(), worker.getPosition(), worker.getAvatar(), worker.getId());
    }

    public void delete(Long id) {
        String query = "DELETE FROM workers WHERE id = ?";
        jdbcTemplate.update(query, id);
    }

    public List<TaskDTO> getTasksByWorkerId(Long id) {
        String query = "SELECT t.* FROM tasks t JOIN workers tw ON t.performer_id = tw.id WHERE tw.id = ?";
        return jdbcTemplate.query(query, (rs, rowNum) -> new TaskDTO(rs.getLong("id"), rs.getString("title"),rs.getString("status")), id);
    }

}
