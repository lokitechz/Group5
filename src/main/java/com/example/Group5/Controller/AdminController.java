package com.example.Group5.Controller;

import com.example.Group5.Entity.AppUser;
import com.example.Group5.Entity.UserRole;
import com.example.Group5.Repository.AppRoleRepo;
import com.example.Group5.Repository.AppUserRepo;
import com.example.Group5.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

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
            if (userRole.getAppUser().isEnabled(true)) {
                users.add(userRole.getAppUser());
            }
        }
        model.addAttribute("employeeInfo", users);
        return "ManageEmployee/ListEmployee";
    }

    //  Trả về trang tạo mới
    @RequestMapping(value = "/manage-employee/create", method = RequestMethod.GET)
    public String createEmployee() {
        return "ManageEmployee/CreateEmployee";
    }

    //  Lưu dữ liệu nhân viên lên Database
    @RequestMapping(value = "/manage-employee/create", method = RequestMethod.POST)
    public String saveEmployee() {

        return "redirect:/manage-employee";
    }


    //  Xóa nhân viên (enable bằng false)
    @RequestMapping(path = "/emp/delete/{id}", method = RequestMethod.GET)
    public String delProduct(@PathVariable long id) {
        AppUser appUser = appUserRepo.findById(id).get();
        appUser.setEnabled(false);
        appUserRepo.save(appUser);
        return "redirect:/manage-employee";
    }

}