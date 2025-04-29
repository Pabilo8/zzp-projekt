package pl.ttpsc.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.ttpsc.taskmanager.service.UserService;

@Controller
public class LoginController
{
	private final UserService _userService;

	public LoginController(UserService userService)
	{
		this._userService = userService;
	}

	@GetMapping("/login")
	public String login()
	{
		return "login";
	}

	@GetMapping("/loginError")
	public String loginError(Model model)
	{
		model.addAttribute("loginError", true);
		return "login";
	}
}
