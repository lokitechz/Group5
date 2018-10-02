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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String save(Model model, AppUser appUser, RedirectAttributes red) {
        List<AppUser> listUser = (List<AppUser>) appUserRepo.findAll();
        String b = appUser.getUserName().toLowerCase();
        for (AppUser x : listUser) {
            if (x.getUserName().toLowerCase().equals(b)) {
                red.addFlashAttribute("dulicateUsername", "Tên tài khoản đã tồn tại");
                return "redirect:/register";
            }
        }
        appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(appUser.getEncrytedPassword()));
        appUser.setEnabled(true);
        appUserRepo.save(appUser);
        if (roleRepo.findByAppUser(appUser).size() == 0) {
            long role = 2;
            UserRole employeeRole = new UserRole();
            employeeRole.setAppRole(appRoleRepo.findById(role).get());
            employeeRole.setAppUser(appUser);
            roleRepo.save(employeeRole);
        }
        red.addFlashAttribute("registerSuccess","Đăng kí tài khoản thành công");
        return "redirect:/login";
    }
}
