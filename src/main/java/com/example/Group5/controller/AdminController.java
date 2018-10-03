package com.example.Group5.controller;

import com.example.Group5.entity.AppUser;
import com.example.Group5.entity.Ticket;
import com.example.Group5.repository.AppUserRepo;
import com.example.Group5.repository.TicketRepo;
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
    private AppUserRepo appUserRepo;

    @Autowired
    private TicketRepo ticketRepo;

    @RequestMapping(value = "/manage-bus/details/{id}", method = RequestMethod.GET)
    public String listTicket(@PathVariable int id, Model model) {
        List<Ticket> tickets = ticketRepo.findAllByBusId(id);
        for (Ticket ticket : tickets) {
            List<AppUser> appUsers = (List<AppUser>) appUserRepo.findAll();
            List<AppUser> customers = new ArrayList<>();
            for (AppUser customer : appUsers) {
                if (ticket.getPassengerId() == customer.getUserId()) {
                    customers.add(customer);
                    model.addAttribute("CustomerInfo", customers);
                }
            }
            model.addAttribute("TicketInfo",ticket);
        }
        return "ManageTicket/ListTicket";
    }
}