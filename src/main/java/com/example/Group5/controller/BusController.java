package com.example.Group5.controller;

import com.example.Group5.entity.Bus;
import com.example.Group5.entity.BusRoute;
import com.example.Group5.entity.BusType;
import com.example.Group5.repository.BusRepo;
import com.example.Group5.repository.BusRouteRepo;
import com.example.Group5.repository.BusTypeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class BusController {

    @Autowired
    private BusRepo busRepo;

    @Autowired
    private BusTypeRepo busTypeRepo;

    @Autowired
    private BusRouteRepo busRouteRepo;


    //  Trả về trang danh sách xe xe
    @RequestMapping(value = "/manage-bus", method = RequestMethod.GET)
    public String listEmployee(Model model) {
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        model.addAttribute("busList", busList);
        return "ManageBus/ListBus";
    }

    //  Trả về trang tạo mới xe
    @RequestMapping(path = "/manage-bus/create", method = RequestMethod.GET)
    public String createUser(Model model) {
        model.addAttribute("bus", new Bus());
        return "ManageBus/CreateBus";
    }

    // Lưu thông tin xe lên database
    @RequestMapping(path = "/manage-bus/create", method = RequestMethod.POST)
    public String addUser(@ModelAttribute @Valid Bus bus, BindingResult bindingResult, @RequestParam("type") int type, @RequestParam("route") int route) {
        List<Bus> listBus = (List<Bus>) busRepo.findAll();
        BusRoute busRoute = busRouteRepo.findById(route).get();
        BusType busType = busTypeRepo.findById(type).get();
        bus.setBusRoute(busRoute);
        bus.setBusType(busType);
        for (Bus b : listBus) {
            if (b.getBusNo() == bus.getBusNo()) {
                return "ManageBus/CreateBus";
            }
        }
        if (bindingResult.hasErrors()) {
            return "ManageBus/CreateBus";
        } else {
            busRepo.save(bus);
            return "redirect:/manage-bus";
        }
    }

    //  Trả về trang sửa thông tin xe
    @RequestMapping(path = "/manage-bus/update/{id}", method = RequestMethod.GET)
    public String editBus(@PathVariable int id, Model model) {
        Optional<Bus> optionalBus = busRepo.findById(id);
        model.addAttribute("bus", optionalBus.get());
        return "ManageBus/UpdateBus";
    }

    //update
    @RequestMapping(value = "/manage-bus/update", method = RequestMethod.POST)
    public String updateBus(@ModelAttribute @Valid Bus bus, BindingResult bindingResult, @RequestParam("type") int type, @RequestParam("route") int route) {
        if (bindingResult.hasErrors()) {
            return "ManageBus/pdateBus";
        } else {
            bus.setBusRoute(busRouteRepo.findById(route).get());
            bus.setBusType(busTypeRepo.findById(type).get());
            busRepo.save(bus);
            return "redirect:/manage-bus";
        }
    }
}
