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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class BusController {

    @Autowired
    BusRepo busRepo;

    @Autowired
    BusTypeRepo busTypeRepo;

    @Autowired
    BusRouteRepo busRouteRepo;

    //  Trả về trang danh sách xe xe
    @RequestMapping(value = "/manage-bus", method = RequestMethod.GET)
    public String listBus(Model model) {
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        model.addAttribute("busList", busList);
        return "ManageBus/ListBus";
    }

    //  Trả về trang tạo mới xe
    @RequestMapping(path = "/manage-bus/create", method = RequestMethod.GET)
    public String createBusPage(Model model) {
        List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
        model.addAttribute("bus", new Bus());
        model.addAttribute("listBusType", typeList);
        return "ManageBus/CreateBus";
    }

    //  Lưu thông tin xe lên database
    @RequestMapping(path = "/manage-bus/create", method = RequestMethod.POST)
    public String saveBus(Model model, @ModelAttribute Bus bus, @RequestParam("type") int type, RedirectAttributes red) {
        for (Bus b : busRepo.findAll()) {
            if (b.getBusNo() == bus.getBusNo()) {
                List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
                model.addAttribute("listBusType", typeList);
                model.addAttribute("msgerror", "Số xe đã bị trùng");
            } else if (bus.getBusNo() == 0 || bus.getBusNo() < 10000) {
                List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
                model.addAttribute("listBusType", typeList);
                model.addAttribute("msgerror1", "Số xe phải là số có 5 chữ số");
            } else {
                BusType busType = busTypeRepo.findById(type).get();
                bus.setBusType(busType);
                busRepo.save(bus);
                red.addFlashAttribute("msgsuccess", "Tạo mới thành công");
                return "redirect:/manage-bus";
            }
        }
        return "ManageBus/CreateBus";
    }

    //  Xoá xe
    @RequestMapping(value = "/manage-bus/delete/{id}", method = RequestMethod.GET)
    public String deleteBus(@PathVariable int id, Model model, RedirectAttributes red) {
        Bus bus = busRepo.findById(id).get();
        List<BusRoute> listBusRoutes = (List<BusRoute>) busRouteRepo.findAll();
        for (BusRoute busRoute : listBusRoutes) {
            if (busRoute.getBus().getBusId() == bus.getBusId()) {
                red.addFlashAttribute("msgDelete", "Xe đang thuộc 1 tuyến đường không thể xóa");
                return "redirect:/manage-bus";
            } else {
                busRepo.delete(bus);
                red.addFlashAttribute("msgSuccess1", "Xóa thành công");
            }
        }
        return "redirect:/manage-bus";
    }
}


