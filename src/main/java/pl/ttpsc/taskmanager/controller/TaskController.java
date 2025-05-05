package pl.ttpsc.taskmanager.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Category;
import pl.ttpsc.taskmanager.model.Task;
import pl.ttpsc.taskmanager.service.CategoryService;
import pl.ttpsc.taskmanager.service.TaskService;

import java.nio.file.AccessDeniedException;

@Controller
public class TaskController {
	private final TaskService taskService;
	private final CategoryService categoryService;

	public TaskController(TaskService taskService, CategoryService categoryService) {
		this.taskService = taskService;
		this.categoryService = categoryService;
	}

	@GetMapping("/tasks")
	@Secured("ROLE_USER")
	public String tasks(Model model, @AuthenticationPrincipal AppUser user) {
		model.addAttribute("tasks", taskService.getTasksByUser(user));
		model.addAttribute("categories", categoryService.getCategoriesByUser(user));
		model.addAttribute("statuses", Task.TaskStatus.values());
		return "tasks";
	}

	@PostMapping("/addTask")
	@Secured("ROLE_USER")
	public String addTask(String title, String description, Long categoryId,
						  @AuthenticationPrincipal AppUser user) {
		Category category = categoryService.getCategoryById(categoryId);
		if (category != null && category.getUser().getId().equals(user.getId())) {
			Task task = new Task();
			task.setTitle(title);
			task.setDescription(description);
			task.setStatus(Task.TaskStatus.NEW);
			task.setUser(user);
			task.setCategory(category);
			taskService.saveTask(task);
		}
		return "redirect:/tasks";
	}

	@PostMapping("/task/status/{id}")
	@Secured("ROLE_USER")
	public String updateStatus(@PathVariable Long id, Task.TaskStatus status,
							   @AuthenticationPrincipal AppUser user) throws AccessDeniedException {
		taskService.updateStatus(id, status, user);
		return "redirect:/tasks";
	}

	@PostMapping("/task/delete/{id}")
	@Secured("ROLE_USER")
	public String deleteTask(@PathVariable Long id,
							 @AuthenticationPrincipal AppUser user) throws AccessDeniedException {
		taskService.deleteTask(id, user);
		return "redirect:/tasks";
	}
}