package pl.ttpsc.taskmanager.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Status;
import pl.ttpsc.taskmanager.service.StatusService;

import java.nio.file.AccessDeniedException;

@Controller
public class StatusController
{
	private final StatusService statusService;

	public StatusController(StatusService statusService)
	{
		this.statusService = statusService;
	}

	@GetMapping("/statuses")
	@Secured("ROLE_USER")
	public String statuses(Model model, @AuthenticationPrincipal AppUser user)
	{
		model.addAttribute("statuses", statusService.getStatusesByUser(user));
		return "statuses";
	}

	@PostMapping("/addStatus")
	@Secured("ROLE_USER")
	public String addStatus(String status_name_add, @AuthenticationPrincipal AppUser user, RedirectAttributes redirectAttributes)
	{
		Status status = new Status();
		status.setName(status_name_add);
		status.setUser(user);

		boolean completed = statusService.saveStatus(status);

		if(!completed)
		{
			redirectAttributes.addFlashAttribute("addStatusError", true);
		}

		return "redirect:/statuses";
	}

	@PostMapping("/status/delete/{id}")
	@Secured("ROLE_USER")
	public String deleteStatus(@PathVariable Long id, @AuthenticationPrincipal AppUser user, RedirectAttributes redirectAttributes) throws AccessDeniedException
	{
		boolean completed = statusService.deleteStatus(id, user);

		if(!completed)
		{
			redirectAttributes.addFlashAttribute("deleteStatusError", true);
		}

		return "redirect:/statuses";
	}

	@PostMapping("/status/edit/{id}")
	@Secured("ROLE_USER")
	public String editStatus(@PathVariable Long id, @RequestParam String status_name_edit, @AuthenticationPrincipal AppUser user, RedirectAttributes redirectAttributes) throws AccessDeniedException
	{
		boolean completed = statusService.editStatus(id, status_name_edit, user);

		if(!completed)
		{
			redirectAttributes.addFlashAttribute("editStatusError", true);
		}

		return "redirect:/statuses";
	}
}