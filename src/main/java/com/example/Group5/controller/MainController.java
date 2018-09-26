package com.example.Group5.controller;

import com.example.Group5.entity.AppUser;
import com.example.Group5.entity.UserRole;
import com.example.Group5.repository.AppRoleRepo;
import com.example.Group5.repository.AppUserRepo;
import com.example.Group5.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    AppRoleRepo appRoleRepo;

    // Trang main khi chương trình chạy lên
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcomePage() {
        return "LayoutPage";
    }

    // Trả về trang đăng nhập
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "Common/LoginForm";
    }

    // Trả về trang 403 khi không có quyền
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "Common/403Page";
    }

    //  Trả về trang thống kê số liệu
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard(Model model) {
//        long roleId = 2;
//        List<UserRole> userRoles = roleRepo.findAllByAppRole(appRoleRepo.findById(roleId));
//        List<AppUser> users = new ArrayList<>();
//        for (UserRole userRole : userRoles) {
//            users.add(userRole.getAppUser());
//        }
//
//        model.addAttribute("countUser", users);
        return "Common/Dashboard";
    }
}
