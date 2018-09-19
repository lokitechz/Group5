package com.example.Group5.Controller;

import com.example.Group5.Entity.AppUser;
import com.example.Group5.Entity.UserRole;
import com.example.Group5.Repository.AppRoleRepo;
import com.example.Group5.Repository.AppUserRepo;
import com.example.Group5.Repository.RoleRepo;
import com.example.Group5.Utils.EncrytedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {

    @Autowired
    private AppRoleRepo appRoleRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    //  Trả về trang danh sách nhân viên
    @RequestMapping(value = "/manage-employee", method = RequestMethod.GET)
    public String listEmployee(Model model) {
        long num = 2;
        List<UserRole> userRoles = roleRepo.findAllByAppRole(appRoleRepo.findById(num));
        List<AppUser> users = new ArrayList<>();
        for (UserRole userRole : userRoles) {
            if (userRole.getAppUser().isEnabled()) {
                users.add(userRole.getAppUser());
            }
        }
        model.addAttribute("employeeInfo", users);
        return "ManageEmployee/ListEmployee";
    }

    //  Trả về trang tạo mới
    @RequestMapping(value = "/manage-employee/create", method = RequestMethod.GET)
    public String createEmployee(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "ManageEmployee/CreateEmployee";
    }

    //  Lưu dữ liệu nhân viên lên Database
    @RequestMapping(value = "/manage-employee/create", method = RequestMethod.POST)
    public String saveEmployee(@Valid AppUser appUser, BindingResult bindingResult, @RequestParam("status") int status, @RequestParam("role") long role) {
        if (bindingResult.hasFieldErrors()) {
            return "ManageEmployee/CreateEmployee";
        } else {
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
        }
        return "redirect:/manage-employee";
    }

    //  Trả về trang chỉnh sửa thông tin nhân viên
    @RequestMapping(path = "/manage-employee/update/{id}", method = RequestMethod.GET)
    public String pageUpdateEmployee(@PathVariable long id, Model model) {
        Optional<AppUser> appUser = appUserRepo.findById(id);
        model.addAttribute("appUser", appUser.get());
        return "ManageEmployee/UpdateEmployee";
    }

    //  Trả về trang chỉnh sửa thông tin nhân viên
    @RequestMapping(path = "/manage-employee/update", method = RequestMethod.POST)
    public String updateEmployee(AppUser appUser, @RequestParam("role") long role, @RequestParam("status") long status) {
        appUserRepo.save(appUser);
        return "redirect:/manage-employee";
    }

    //  Xóa nhân viên
    @RequestMapping(path = "/manage-employee/delete/{id}", method = RequestMethod.GET)
    public String delProduct(@PathVariable long id) {
        AppUser appUser = appUserRepo.findById(id).get();
        appUserRepo.delete(appUser);
        return "redirect:/manage-employee";
    }

}