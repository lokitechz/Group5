package com.example.Group5.controller;

import com.example.Group5.entity.Ticket;
import com.example.Group5.repository.AppUserRepo;
import com.example.Group5.repository.BusRouteRepo;
import com.example.Group5.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    BusRouteRepo busRouteRepo;

    @Autowired
    AppUserRepo appUserRepo;

    //  Trả về trang thống kê số liệu
    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String Dashboard(Model model) {
        int total = 0;
        int revenue = 0;
        int order = 0;
        for (int x = 0; x <= 7; x++) {
            for (Ticket ticket : ticketRepo.findAllByBookingDate(Date.from(LocalDate.now().minusDays(x).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                total += ticket.getAmount();
                order++;
                int perBusRoute = ticket.getAmount();
                revenue += busRouteRepo.findById(ticket.getRouteId()).get().getFare() * perBusRoute;
            }
        }
        model.addAttribute("DashboardTicket", order);
        model.addAttribute("DashboardWeeklySale", total);
        model.addAttribute("revenue", Integer.toString(revenue));
        return "Common/Dashboard";
    }

    //  Trả về danh sách những vé đã bán
    @RequestMapping(value = "/manage-ticket", method = RequestMethod.GET)
    public String listTicket(Model model) {
        List<Ticket> tickets = (List<Ticket>) ticketRepo.findAll();
        model.addAttribute("tickets", tickets);
        return "ManageTicket/ListTicket";
    }

    //  Trả về trang danh sách các vé đã bán theo hành trình
    @RequestMapping(value = "manage-route/tickets/{id}", method = RequestMethod.GET)
    public String listTicketByRoute(Model model, @PathVariable int id) {
        List<Ticket> tickets = ticketRepo.findAllByRouteId(id);
        if (tickets.size() > 0) {
            model.addAttribute("tickets", tickets);
        } else {
            model.addAttribute("isEmpty", "Danh sách rỗng");
        }
        return "ManageRoute/ListTicket";
    }
}
