package com.example.Group5.controller;

import com.example.Group5.entity.AppUser;
import com.example.Group5.entity.UserRole;
import com.example.Group5.repository.AppRoleRepo;
import com.example.Group5.repository.AppUserRepo;
import com.example.Group5.repository.RoleRepo;
import com.example.Group5.utils.EncrytedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RegisterController {

    @Autowired
    private AppRoleRepo appRoleRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    //  Trả về trang đăng ký
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "Common/Register";
    }

    //  Lưu dữ liệu khàng hách lên Database
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String save(Model model, AppUser appUser, @RequestParam("status") int status, @RequestParam("role") long role) {
        List<AppUser> listUser = (List<AppUser>) appUserRepo.findAll();
        String b = appUser.getUserName().toLowerCase();
        for (AppUser x : listUser) {
            if (x.getUserName().toLowerCase().equals(b)) {
                model.addAttribute("dulicateUsername", "Tên tài khoản đã tồn tại");
                return "Common/Register";
            }
        }
        appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(appUser.getEncrytedPassword()));
        if (status == 1) {
            appUser.setEnabled(true);
        } else {
            appUser.setEnabled(false);
        }
        appUserRepo.save(appUser);
        if (roleRepo.findByAppUser(appUser).size() == 0) {
            long userRole = role;
            UserRole employeeRole = new UserRole();
            employeeRole.setAppRole(appRoleRepo.findById(userRole).get());
            employeeRole.setAppUser(appUser);
            roleRepo.save(employeeRole);
        }
        return "Common/LoginForm";
    }
}
