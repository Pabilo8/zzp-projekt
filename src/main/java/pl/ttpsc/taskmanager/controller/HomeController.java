package pl.ttpsc.taskmanager.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.model.Category;
import pl.ttpsc.taskmanager.service.CategoryService;

import java.nio.file.AccessDeniedException;

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
	public String addCategory(String category_name_add, @AuthenticationPrincipal AppUser user, RedirectAttributes redirectAttributes)
	{
		Category category = new Category();
		category.setName(category_name_add);
		category.setUser(user);

		boolean completed = _categoryService.saveCategory(category);

		if(!completed){
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

	@PostMapping("/category/edit/{id}")
	@Secured("ROLE_USER")
	public String editCategory(@PathVariable Long id, @RequestParam String category_name_edit, @AuthenticationPrincipal AppUser user) throws AccessDeniedException
	{
		_categoryService.editCategory(id, category_name_edit, user);
		return "redirect:/index";
	}

}
