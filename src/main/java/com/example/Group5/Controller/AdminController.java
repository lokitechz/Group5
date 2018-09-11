package com.example.Group5.Controller;

import com.example.Group5.Repository.AppRoleRepo;
import com.example.Group5.Repository.AppUserRepo;
import com.example.Group5.Repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class AdminController {

    @Autowired
    private AppRoleRepo appRoleRepo;

    @Autowired
    private AppUserRepo appUserRepo;

    @Autowired
    private RoleRepo roleRepo;

    //    Controller trả về trang danh sách nhân viên
    @RequestMapping(value = "/manage-employee", method = RequestMethod.GET)
    public String adminPage(Model model, Principal principal) {
        //        Code here
        return "";
    }
}