package com.example.Group5.controller;

import com.example.Group5.entity.BusRoute;
import com.example.Group5.entity.Ticket;
import com.example.Group5.repository.AppUserRepo;
import com.example.Group5.repository.BusRouteRepo;
import com.example.Group5.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        for (int x = 0; x <= 7; x++) {
            for (Ticket ticket : ticketRepo.findAllByBookingDate(Date.from(LocalDate.now().minusDays(x).atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
                int perBusRoute = ticket.getAmount();
                revenue += busRouteRepo.findById(ticket.getRouteId()).get().getFare() * perBusRoute;
            }
        }
        for (Ticket ticket : ticketRepo.findAllByBookingDate(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()))) {
            total += ticket.getAmount();
        }
        List<BusRoute> busRouteList = new ArrayList<>();
        for(BusRoute route :  busRouteRepo.findAll()){
            int amountPerRoute = 0;
            for (Ticket routeTicket : ticketRepo.findAllByRouteId(route.getRouteId())) {
                amountPerRoute+=routeTicket.getAmount();
            }
            route.setBreakPoint(Integer.toString(amountPerRoute));
            busRouteList.add(route);
        }
        model.addAttribute("revenue", Integer.toString(revenue));
        model.addAttribute("dailysale", total);
        model.addAttribute("busRouteList", busRouteList);
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

    //  Trả về trang sửa trạng thái của vé
    @RequestMapping(path = "ticket/update/{id}", method = RequestMethod.GET)
    public String editTicketPage(@PathVariable int id, Model model) {
        Optional<Ticket> ticket = ticketRepo.findById(id);
        ticket.ifPresent(ticket1 -> model.addAttribute("detailTicket", ticket1));
        return "ManageTicket/UpdateTicket";
    }

    // Lưu trạng thái của vé lên DB
    @RequestMapping(value = "ticket/update/{id}", method = RequestMethod.POST)
    public String updateTicket(@PathVariable int id, @ModelAttribute Ticket ticket, RedirectAttributes red) {
        Ticket item = ticketRepo.findById(id).get();
        item.setStatus(ticket.getStatus());
        ticketRepo.save(item);
        red.addFlashAttribute("msg", "Cập nhật trạng thái thành công");
        return "redirect:/manage-ticket";
    }

}
