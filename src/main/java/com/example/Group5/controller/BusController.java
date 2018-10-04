package com.example.Group5.controller;

import com.example.Group5.entity.Bus;
import com.example.Group5.repository.BusRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller
public class BusController {

    @Autowired
    private BusRepo busRepo;

    //  Trả về trang danh sách xe xe
    @RequestMapping(value = "/manage-bus", method = RequestMethod.GET)
    public String listBus(Model model) {
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        model.addAttribute("busList", busList);
        return "ManageBus/ListBus";
    }
}
//
//    //  Trả về trang tạo mới xe
//    @RequestMapping(path = "/manage-bus/create", method = RequestMethod.GET)
//    public String createBusPage(Model model) {
//        List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
//        List<BusRoute> routeList = (List<BusRoute>) busRouteRepo.findAll();
//        model.addAttribute("bus", new Bus());
//        model.addAttribute("listBusType", typeList);
//        model.addAttribute("listBusRoute", routeList);
//        return "ManageBus/CreateBus";
//    }
//
//    // Lưu thông tin xe lên database
//    @RequestMapping(path = "/manage-bus/create", method = RequestMethod.POST)
//    public String saveBus(Model model, @ModelAttribute Bus bus, @RequestParam("type") int type, @RequestParam("route") int route) {
//        BusRoute busRoute = busRouteRepo.findById(route).get();
//        BusType busType = busTypeRepo.findById(type).get();
//        bus.setBusRoute(busRoute);
//        bus.setBusType(busType);
//        List<Bus> busList = busRepo.findAllByBusNo(bus.getBusNo());
//        for (Bus b : busList) {
//            if (b.getBusRoute().getBusRouteId() == bus.getBusRoute().getBusRouteId()) {
//                List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
//                List<BusRoute> routeList = (List<BusRoute>) busRouteRepo.findAll();
//                model.addAttribute("bus", new Bus());
//                model.addAttribute("listBusType", typeList);
//                model.addAttribute("listBusRoute", routeList);
//                model.addAttribute("duplicateRoute", "Hành trình đã tồn tại");
//                return "ManageBus/CreateBus";
//            }
//        }
//        busRepo.save(bus);
//        return "redirect:/manage-bus";
//    }
//
//    //  Trả về trang sửa thông tin xe
//    @RequestMapping(path = "/manage-bus/update/{id}", method = RequestMethod.GET)
//    public String editBusPage(@PathVariable int id, Model model) {
//        List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
//        List<BusRoute> routeList = (List<BusRoute>) busRouteRepo.findAll();
//        Optional<Bus> optionalBus = busRepo.findById(id);
//        typeList.remove(optionalBus.get().getBusType());
//        routeList.remove(optionalBus.get().getBusRoute());
//        model.addAttribute("listBusType", typeList);
//        model.addAttribute("listBusRoute", routeList);
//        model.addAttribute("bus", optionalBus.get());
//        return "ManageBus/UpdateBus";
//    }
//
//    // Lưu thông tin chỉnh sửa lên DB
//    @RequestMapping(value = "/manage-bus/update", method = RequestMethod.POST)
//    public String updateBus(Model model, @ModelAttribute Bus bus, @RequestParam("type") int type, @RequestParam("route") int route) {
//        bus.setBusRoute(busRouteRepo.findById(route).get());
//        bus.setBusType(busTypeRepo.findById(type).get());
//        List<Bus> busList = busRepo.findAllByBusNo(bus.getBusNo());
//        for (Bus b : busList) {
//            if (b.getBusRoute().getBusRouteId() == bus.getBusRoute().getBusRouteId()) {
//                List<BusType> typeList = (List<BusType>) busTypeRepo.findAll();
//                List<BusRoute> routeList = (List<BusRoute>) busRouteRepo.findAll();
//                model.addAttribute("listBusType", typeList);
//                model.addAttribute("listBusRoute", routeList);
//                model.addAttribute("duplicateRoute", "Hành trình đã tồn tại");
//                return "ManageBus/UpdateBus";
//            }
//        }
//        busRepo.save(bus);
//        return "redirect:/manage-bus";
//    }
//
//    //  Trả về trang đặt vé
//    @RequestMapping(value = "/booking-ticket/{id}", method = RequestMethod.GET)
//    public String bookingPage(@PathVariable int id, Model model) {
//        Optional<Bus> optionalBus = busRepo.findById(id);
//        model.addAttribute("bus", optionalBus.get());
//        model.addAttribute("ticket", new Ticket());
//        return "Booking/BookingPage";
//    }
//}
