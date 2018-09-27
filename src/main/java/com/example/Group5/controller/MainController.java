package com.example.Group5.controller;

import com.example.Group5.entity.*;
import com.example.Group5.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    AppRoleRepo appRoleRepo;

    @Autowired
    BusRouteRepo busRouteRepo;

    @Autowired
    BusRepo busRepo;

    // Trang chính khi chạy chương trình
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage() {
        return "Customer/HomePage";
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

    //  Trả về danh sách kết quả tìm kiếm của khách hàng
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam String origin, @RequestParam String destination, @RequestParam String date, Model model) {
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        List<BusRoute> busRoutes = new ArrayList<>();
        List<Bus> buses = new ArrayList<>();
        for (BusRoute busRoute : busRouteList) {
            if (busRoute.getOrigin().equalsIgnoreCase(origin) && busRoute.getDestination().equalsIgnoreCase(destination) && busRoute.getDepartureDate().toString().equals(date)) {
                busRoutes.add(busRoute);
                for (Bus bus : busList) {
                    if (bus.getBusRoute().getBusRouteId() == busRoute.getBusRouteId()) {
                        buses.add(bus);
                        model.addAttribute("listSearch", buses);
                    }
                }
            }
        }
        return "Customer/HomePage";
    }

    //  Trả về trang đặt vé cho khách hàng
    @RequestMapping(value = "/customer/booking-ticket/{id}", method = RequestMethod.GET)
    public String bookingPage(@PathVariable int id, Model model) {
        Optional<Bus> optionalBus = busRepo.findById(id);
        model.addAttribute("bus", optionalBus.get());
        model.addAttribute("ticket", new Ticket());
        return "Customer/BookingPageforCustomer";
    }
}
