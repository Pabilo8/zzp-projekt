package pl.ttpsc.taskmanager.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.ttpsc.taskmanager.model.AppUser;
import pl.ttpsc.taskmanager.service.UserService;

@Controller
public class LoginController
{
	private final UserService _userService;
	private final PasswordEncoder _passwordEncoder;

	public LoginController(UserService userService, PasswordEncoder passwordEncoder)
	{
		this._userService = userService;
		this._passwordEncoder = passwordEncoder;
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

	@GetMapping("/register")
	public String register()
	{
		return "register";
	}

	@PostMapping("/register")
	public String registerForm(@RequestParam String username, @RequestParam String password, @RequestParam String password_repeat, Model model)
	{
		if(username.isEmpty() || password.isEmpty())
		{
			model.addAttribute("registerEmpty", true);
			return "register";
		}

		if(!password.equals(password_repeat))
		{
			model.addAttribute("passwordNotEqual", true);
			return "register";
		}

		boolean completed = _userService.saveUser(username, _passwordEncoder.encode(password));

		if(!completed)
		{
			model.addAttribute("registerError", true);
			return "register";
		}

		model.addAttribute("registerSuccess", true);
		return "login";
	}
}
