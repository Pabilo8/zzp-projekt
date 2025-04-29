package pl.ttpsc.taskmanager.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Category;
import pl.ttpsc.taskmanager.service.CategoryService;
import pl.ttpsc.taskmanager.service.UserService;

import java.nio.file.AccessDeniedException;
import java.security.Principal;
import java.util.List;

@Controller
public class HomeController
{

	private final CategoryService _categoryService;

	public HomeController(CategoryService categoryService)
	{
		this._categoryService = categoryService;
	}

	@GetMapping({"/", "/index"})
	@Secured("ROLE_USER")
	public String index(Model model, @AuthenticationPrincipal AppUser user)
	{
		model.addAttribute("categories", _categoryService.getCategoriesByUser(user));
		return "index";
	}

	@PostMapping("/addCategory")
	@Secured("ROLE_USER")
	public String addCategory(String name, @AuthenticationPrincipal AppUser user, RedirectAttributes redirectAttributes)
	{
		Category category = new Category();
		category.setName(name);
		category.setUser(user);

		boolean newlyAdded = _categoryService.saveCategory(category);

		if(!newlyAdded){
			redirectAttributes.addFlashAttribute("addCategoryError", true);
		}

		return "redirect:/index";
	}

	@PostMapping("/category/delete/{id}")
	@Secured("ROLE_USER")
	public String deleteCategory(@PathVariable Long id, @AuthenticationPrincipal AppUser user) throws AccessDeniedException
	{
		_categoryService.deleteCategory(id, user);
		return "redirect:/index";
	}

}
