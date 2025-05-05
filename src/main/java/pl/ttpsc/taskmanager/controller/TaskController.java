package pl.ttpsc.taskmanager.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Category;
import pl.ttpsc.taskmanager.model.Status;
import pl.ttpsc.taskmanager.model.Task;
import pl.ttpsc.taskmanager.service.CategoryService;
import pl.ttpsc.taskmanager.service.StatusService;
import pl.ttpsc.taskmanager.service.TaskService;

import java.nio.file.AccessDeniedException;

@Controller
public class TaskController
{
	private final TaskService taskService;
	private final CategoryService categoryService;
	private final StatusService statusService;

	public TaskController(TaskService taskService, CategoryService categoryService, StatusService statusService)
	{
		this.taskService = taskService;
		this.categoryService = categoryService;
		this.statusService = statusService;
	}

	@GetMapping("/tasks")
	@Secured("ROLE_USER")
	public String tasks(Model model, @AuthenticationPrincipal AppUser user)
	{
		model.addAttribute("tasks", taskService.getTasksByUser(user));
		model.addAttribute("categories", categoryService.getCategoriesByUser(user));
		model.addAttribute("statuses", statusService.getStatusesByUser(user));
		return "tasks";
	}

	@PostMapping("/addTask")
	@Secured("ROLE_USER")
	public String addTask(String title, String description, Long categoryId,
						  @AuthenticationPrincipal AppUser user)
	{
		Category category = categoryService.getCategoryById(categoryId);
		if(category!=null&&category.getUser().getId().equals(user.getId()))
		{
			Task task = new Task();
			task.setTitle(title);
			task.setDescription(description);
			task.setStatus(statusService.getStatusById(1L)); // Domyślny status
			task.setUser(user);
			task.setCategory(category);
			taskService.saveTask(task);
		}
		return "redirect:/tasks";
	}

	@PostMapping("/task/edit/{id}")
	@Secured("ROLE_USER")
	public String editTask(@PathVariable Long id,
						   String title,
						   String description,
						   Long category,
						   @AuthenticationPrincipal AppUser user) throws AccessDeniedException
	{
		// Get the task and verify ownership
		Task task = taskService.getTaskById(id);
		if(task==null||!task.getUser().getId().equals(user.getId()))
		{
			throw new AccessDeniedException("Not authorized to edit this task");
		}

		// Get the category and verify ownership
		Category catById = categoryService.getCategoryById(category);
		if(catById==null||!catById.getUser().getId().equals(user.getId()))
		{
			throw new AccessDeniedException("Not authorized to use this category");
		}

		// Update task properties
		task.setTitle(title);
		task.setDescription(description);
		task.setCategory(catById);

		// Save the updated task
		taskService.saveTask(task);

		return "redirect:/tasks";
	}

	@PostMapping("/task/status/{id}")
	@Secured("ROLE_USER")
	public String updateStatus(@PathVariable Long id, Long status,
							   @AuthenticationPrincipal AppUser user) throws AccessDeniedException
	{
		Status statusObj = statusService.getStatusById(status);
		taskService.updateStatus(id, statusObj, user);
		return "redirect:/tasks";
	}

	@PostMapping("/task/delete/{id}")
	@Secured("ROLE_USER")
	public String deleteTask(@PathVariable Long id,
							 @AuthenticationPrincipal AppUser user) throws AccessDeniedException
	{
		taskService.deleteTask(id, user);
		return "redirect:/tasks";
	}
}