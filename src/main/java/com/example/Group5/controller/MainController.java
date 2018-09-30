package com.example.Group5.controller;

import com.example.Group5.dao.AppUserDAO;
import com.example.Group5.entity.*;
import com.example.Group5.repository.*;
import com.example.Group5.utils.EncrytedPasswordUtils;
import com.example.Group5.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    private JavaMailSender javaMailSender;

    // Trang chính khi chạy chương trình
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model) {
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
    public String Dashboard(Model model) {
        int total = 0;
        long num = 2;
        int revenue = 0;
        int order = 0;
        model.addAttribute("DashBoardUser", roleRepo.findAllByAppRole(appRoleRepo.findById(num)).size());
        for (int x = 0; x < 7; x++) {
            for (Ticket ticket : ticketRepo.findAllByBookingDate(Date.from(LocalDate.now().plusDays(-x).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                total += ticket.getAmount();
                order++;
                int amount = ticket.getAmount();
                for (Bus bus : busRepo.findAllByBusId(ticket.getBusId())) {
                    for (BusRoute route : busRouteRepo.findAllByBusRouteId(bus.getId())) {
                        revenue += route.getFare() * amount;
                    }
                }
            }
        }
        model.addAttribute("DashboardTicket", order);
        model.addAttribute("DashboardWeeklySale", total);
        model.addAttribute("revenue", Integer.toString(revenue));
        return "Common/Dashboard";
    }

    //  Trả về danh sách kết quả tìm kiếm của khách hàng
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(@RequestParam(name = "origin") String origin, @RequestParam(name = "destination") String destination, @RequestParam(name = "date") String date, Model model, HttpSession session) {
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

    //  Lưu thông tin đặt vé của khách hàng
    @RequestMapping(value = "/customer/booking-ticket/{id}", method = RequestMethod.POST)
    public String saveTicket(@PathVariable int id, @ModelAttribute Ticket ticket,@RequestParam String username) {
        ticket.setBusId(id);
        ticket.setPassengerId(appUserRepo.findAppUserByUserName(username).getUserId());
        ticketRepo.save(ticket);
        return "redirect:/customer/booking-ticket/detail/" + ticket.getTicketId();
    }

//    //  Trang thông tin chi tiết vé xe khách đã đặt
//    @RequestMapping(value = "/customer/booking-ticket/detail/{id}", method = RequestMethod.GET)
//    public String detailTicket(Model model, @PathVariable int id, @ModelAttribute Ticket ticket) {
//    }


    //  Chuyển sang trang đổi mật khẩu
    @RequestMapping(path = "/change-password/{name}", method = RequestMethod.GET)
    public String changePassword(Model model, @PathVariable String name) {
        AppUser appUser = appUserRepo.findAppUserByUserName(name);
        model.addAttribute("appUser", appUser);
        return "Common/ChangePassword";
    }

    // Đổi mật khẩu mà lưu lên database
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String updatePassword(Model model, AppUser appUser, @RequestParam("oldpass") String oldPass, @RequestParam("newpass") String newPass, @RequestParam("renewpass") String reNewPass) {
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

    private static <E> List<E> toList(Iterable<E> iterable) {
        if (iterable instanceof List) {
            return (List<E>) iterable;
        }
        ArrayList<E> list = new ArrayList<E>();
        if (iterable != null) {
            for (E e : iterable) {
                list.add(e);
            }
        }
        return list;
    }

}
