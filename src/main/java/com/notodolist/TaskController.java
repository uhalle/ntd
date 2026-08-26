package com.notodolist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskRepository repository;
    
    private final GroupRepository gRepository;

    @Autowired
    public TaskController(TaskRepository repository, GroupRepository gRepository) {
        this.repository = repository;
        this.gRepository = gRepository;
    }

    @GetMapping
    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    @PostMapping
    @Transactional
    public Task createTask(@RequestBody Task task) {
    	Group group = new Group();
    	group.setName(task.getGroup().getName());
    	group.setUser(task.getUser());
    	gRepository.save(group);
    	task.setGroup(group);
        return repository.save(task);
    }

    @PutMapping("/{id}")
    public Task updateTask(@PathVariable Long id, @RequestBody Task updatedTask) {
        return repository.findById(id)
                .map(task -> {
                    task.setDescription(updatedTask.getDescription());
                    task.setPriority(updatedTask.getPriority());
                    return repository.save(task);
                })
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        repository.deleteById(id);
    }
    
    @GetMapping("/byUserAndGroup/{userId}/{groupId}")
    public List<Task> getTasksByUserAndGroup(
    		@PathVariable Long userId,
    		@PathVariable Long groupId) {
        return repository.findByUserIdAndGroupId(userId, groupId);
    }
    
    @GetMapping("/{userId}")
    public List<TaskDTO> getTasksByUser(
            @PathVariable Long userId) {

    	System.out.println("trying to get tasks");
    	List<Task> tasks = repository.findByUserId(userId);
    	List<TaskDTO> tasksDTO = tasks.stream().map((task)->mapTaskToTaskDTO(task)).toList();
        return tasksDTO;
    }
    
    private TaskDTO mapTaskToTaskDTO(Task task) {
		TaskDTO taskDTO = new TaskDTO();
		taskDTO.setId(task.getId());
		taskDTO.setGroup(task.getGroup());
		taskDTO.setUser(task.getUser());
		taskDTO.setPriority(task.getPriority());
		taskDTO.setDescription(task.getDescription());
		return taskDTO;
	}

	@GetMapping("/groups/{userId}")
    public List<Group> getGroupsByUser(
            @PathVariable Long userId) {
        return gRepository.findGroupsByUserId(userId);
    }
    
}