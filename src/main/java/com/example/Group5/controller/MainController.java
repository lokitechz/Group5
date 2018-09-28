package com.example.Group5.controller;

import com.example.Group5.entity.Bus;
import com.example.Group5.entity.BusRoute;
import com.example.Group5.entity.Passenger;
import com.example.Group5.entity.Ticket;
import com.example.Group5.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.servlet.http.HttpSession;
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

    @Autowired
    PassengerRepo passengerRepo;

    @Autowired
    TicketRepo ticketRepo;

    private boolean isSearch = false;

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
    public String dashboard(Model model) {
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
    public String saveTicket(@PathVariable int id, @ModelAttribute Ticket ticket, @RequestParam String fullname, @RequestParam int age) {
        Passenger passenger = new Passenger();
        passenger.setPassengerName(fullname);
        passenger.setPassengerAge(age);
        passengerRepo.save(passenger);
        Optional<Passenger> optionalPassenger = passengerRepo.findById(passenger.getPassengerId());
        ticket.setBusId(id);
        ticket.setPassengerId(optionalPassenger.get().getPassengerId());
        ticketRepo.save(ticket);
        return "redirect:/customer/booking-ticket/detail/" + ticket.getTicketId();
    }

    //  Trang thông tin chi tiết vé xe khách đã đặt
    @RequestMapping(value = "/customer/booking-ticket/detail/{id}", method = RequestMethod.GET)
    public String detailTicket(Model model, @PathVariable int id, @ModelAttribute Ticket ticket) {
        Optional<Ticket> ticketOptional = ticketRepo.findById(id);
        if (ticketOptional.isPresent()) {
            Optional<Bus> bus = busRepo.findById(ticketOptional.get().getBusId());
            Optional<Passenger> passenger = passengerRepo.findById(ticketOptional.get().getPassengerId());
            model.addAttribute("ticketInfo", ticketOptional.get());
            model.addAttribute("busInfo", bus.get());
            model.addAttribute("passengerInfo", passenger.get());
            return "Customer/InfoTicket";
        }
        return "Customer/InfoTicket";
    }
}
