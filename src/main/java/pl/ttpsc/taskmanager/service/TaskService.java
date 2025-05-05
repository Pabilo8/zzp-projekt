package pl.ttpsc.taskmanager.service;

import org.springframework.stereotype.Service;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Task;
import pl.ttpsc.taskmanager.repository.TaskRepository;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class TaskService {
	private final TaskRepository taskRepository;

	public TaskService(TaskRepository taskRepository) {
		this.taskRepository = taskRepository;
	}

	public List<Task> getTasksByUser(AppUser user) {
		return taskRepository.findAllByUser(user);
	}

	public Task getTaskById(Long id) {
		return taskRepository.findById(id).orElse(null);
	}

	public boolean saveTask(Task task) {
		try {
			taskRepository.save(task);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void updateStatus(Long id, Task.TaskStatus status, AppUser user) throws AccessDeniedException {
		Task task = taskRepository.findById(id).orElse(null);
		if (task != null && task.getUser().getId().equals(user.getId())) {
			task.setStatus(status);
			taskRepository.save(task);
		} else if (task != null) {
			throw new AccessDeniedException("Brak dostępu do tego zadania");
		}
	}

	public void deleteTask(Long id, AppUser user) throws AccessDeniedException {
		Task task = taskRepository.findById(id).orElse(null);
		if (task != null && task.getUser().getId().equals(user.getId())) {
			taskRepository.delete(task);
		} else if (task != null) {
			throw new AccessDeniedException("Brak dostępu do tego zadania");
		}
	}
}