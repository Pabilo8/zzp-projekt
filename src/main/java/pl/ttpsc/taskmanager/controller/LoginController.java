package pl.ttpsc.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import pl.ttpsc.taskmanager.service.LoginService;

@Controller
public class LoginController {

    private final LoginService _loginService;

    public LoginController(LoginService loginService) {
        this._loginService = loginService;
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
