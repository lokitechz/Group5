package com.example.Group5.Controller;

import com.example.Group5.Utils.WebUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class MainController {
    // Trang main khi chương trình chạy lên
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcomePage() {
        return "LayoutPage";
    }

    // Trả về trang đăng nhập
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(Model model) {
        return "LoginForm";
    }

    // Sau khi đăng nhập thành công sẽ trả về trang main
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal) {
        // Sau khi user login thanh cong se co principal
        String userName = principal.getName();

        User loginedUser = (User) ((Authentication) principal).getPrincipal();

        String userInfo = WebUtils.toString(loginedUser);
        model.addAttribute("userInfo", userInfo);

        return "LayoutPage";
    }

    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied(Model model, Principal principal) {
        if (principal != null) {
            User loginedUser = (User) ((Authentication) principal).getPrincipal();
        }
        return "403Page";
    }

}
