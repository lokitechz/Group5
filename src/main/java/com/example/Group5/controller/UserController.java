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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

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
        red.addFlashAttribute("registerSuccess", "Đăng kí tài khoản thành công");
        return "redirect:/login";
    }

    //  Trả về trang thông tin cá nhân của tài khoản
    @RequestMapping(value = "/{username}")
    public String userInfo(@PathVariable String username, Model model) {
        AppUser appUser = appUserRepo.findAppUserByUserName(username);
        model.addAttribute("appUser", appUser);
        return "Common/UserInfoPage";
    }

    //  Luư thông tin nhân viên sau khi chỉnh sửa
    @RequestMapping(value = "/updateInfo", method = RequestMethod.POST)
    public String updateInfo(RedirectAttributes red, Principal principal, @ModelAttribute AppUser appUser) {
        AppUser userInfo = appUserRepo.findAppUserByUserName(principal.getName());
        userInfo.setFullName(appUser.getFullName());
        userInfo.setAge(appUser.getAge());
        userInfo.setEmail(appUser.getEmail());
        userInfo.setPhone(appUser.getPhone());
        userInfo.setAddress(appUser.getAddress());
        appUser.setEnabled(true);
        appUserRepo.save(userInfo);
        red.addFlashAttribute("updateSucess", "Cập nhật thông tin thành công");
        return "redirect:/";
    }

    //  Chuyển sang trang đổi mật khẩu
    @RequestMapping(path = "/change-password/{name}", method = RequestMethod.GET)
    public String changePassword(Model model, @PathVariable String name) {
        AppUser appUser = appUserRepo.findAppUserByUserName(name);
        model.addAttribute("appUser", appUser);
        return "Common/ChangePassword";
    }

    // Đổi mật khẩu mà lưu lên database
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String updatePassword(Model model, AppUser appUser, @RequestParam("oldpass") String oldPass,
                                 @RequestParam("newpass") String newPass, @RequestParam("renewpass") String reNewPass) {
        if (!EncrytedPasswordUtils.comparePassword(oldPass, appUser.getEncrytedPassword())) {
            model.addAttribute("error", "Mật khẩu không đúng!");
            model.addAttribute("appUser", appUser);
            return "Common/ChangePassword";
        } else {
            if (!newPass.equals(reNewPass)) {
                model.addAttribute("error", "Mật khẩu mới không giống nhau");
                model.addAttribute("appUser", appUser);
                return "Common/ChangePassword";
            } else {
                appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(newPass));
                appUserRepo.save(appUser);
                return "Common/LoginForm";
            }
        }
    }
}
