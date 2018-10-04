//package com.example.Group5.controller;
//
//import com.example.Group5.entity.AppUser;
//import com.example.Group5.repository.AppUserRepo;
//import com.example.Group5.repository.TicketRepo;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Controller
//public class AdminController {
//
//    @Autowired
//    private AppUserRepo appUserRepo;
//
//    @Autowired
//    private TicketRepo ticketRepo;
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