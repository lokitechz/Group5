package com.example.Group5.controller;

import com.example.Group5.entity.Ticket;
import com.example.Group5.repository.BusRepo;
import com.example.Group5.repository.BusRouteRepo;
import com.example.Group5.repository.TicketRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class AdminController {

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    BusRouteRepo busRouteRepo;

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


}
//
//    // Tìm tất danh sách những khách hàng đã đặt vé xe theo mã xe
//    @RequestMapping(value = "/manage-bus/details/{id}", method = RequestMethod.GET)
//    public String listTicket(@PathVariable int id, Model model) {
//        List<Ticket> tickets = ticketRepo.findAllByBusId(id);
//        for (Ticket ticket : tickets) {
//            List<AppUser> appUsers = (List<AppUser>) appUserRepo.findAll();
//            List<AppUser> customers = new ArrayList<>();
//            for (AppUser customer : appUsers) {
//                if (ticket.getPassengerId() == customer.getUserId()) {
////                    customers.add(customer);
//                    model.addAttribute("CustomerInfo", customers);
//                }
//            }
//            model.addAttribute("TicketInfo", ticket);
//        }
//        return "ManageTicket/ListTicket";
//    }
//
//    //  Trả về trang sửa trạng thái của vé
//    @RequestMapping(path = "/manage-bus/updateTicket/{id}", method = RequestMethod.GET)
//    public String editTicketPage(@PathVariable int id, Model model) {
//        Optional<Ticket> ticket = ticketRepo.findById(id);
//        if (ticket.isPresent()) {
//            model.addAttribute("detailTicket", ticket.get());
//        }
//        return "ManageTicket/UpdateTicket";
//    }
//
//    // Lưu trạng thái của vé lên DB
//    @RequestMapping(value = "/manage-bus/updateTicket/{id}", method = RequestMethod.POST)
//    public String updateTicket(@PathVariable int id, @ModelAttribute Ticket ticket, RedirectAttributes red) {
//        Ticket item = ticketRepo.findById(id).get();
//        item.setStatus(ticket.isStatus());
//        ticketRepo.save(item);
//        red.addFlashAttribute("msg", "Cập nhật thành công");
//        return "redirect:/manage-bus";
//    }
//}