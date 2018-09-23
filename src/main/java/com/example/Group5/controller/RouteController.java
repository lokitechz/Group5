package com.example.Group5.controller;

import com.example.Group5.entity.Bus;
import com.example.Group5.entity.BusRoute;
import com.example.Group5.entity.BusType;
import com.example.Group5.repository.BusRepo;
import com.example.Group5.repository.BusRouteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class RouteController {

    @Autowired
    private BusRepo busRepo;

    @Autowired
    private BusRouteRepo busRouteRepo;

    //  Trả về trang danh sách tuyến đường
    @RequestMapping(value = "/manage-bus-route", method = RequestMethod.GET)
    public String listRoute(Model model) {
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        model.addAttribute("busRouteList", busRouteList);
        return "ManageRoute/ListBusRoute";
    }

    //  Trả về trang tạo mới tuyến đường
    @RequestMapping(path = "/manage-bus-route/create", method = RequestMethod.GET)
    public String createRoute(Model model) {
        model.addAttribute("bus_route", new BusRoute());
        return "ManageRoute/CreateRoute";
    }

    // Lưu thông tin tuyến đườnge lên database
    @RequestMapping(path = "/manage-bus-route/create", method = RequestMethod.POST)
    public String addRoute(Model model, @ModelAttribute @Valid BusRoute busRoute, BindingResult bindingResult) {
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        for (BusRoute b : busRouteList) {
            //Nếu trùng ngày thì load lại trang create
            if (b.getOrigin().toLowerCase().equals(busRoute.getOrigin().toLowerCase()) && b.getDestination().toLowerCase().equals(busRoute.getDestination().toLowerCase())
                    && b.getDepartureDate().toString().equals(busRoute.getDepartureDate().toString())) {
                model.addAttribute("error", "Ngày khởi hành đã tồn tại");
                model.addAttribute("bus_route", new BusRoute());
                return "ManageRoute/CreateRoute";
            }
        }
        if (bindingResult.hasErrors()) {
            return "ManageRoute/CreateRoute";
        } else {
            busRouteRepo.save(busRoute);
            return "redirect:/manage-bus-route";
        }
    }

    //  Trả về trang sửa thông tin tuyến đường
    @RequestMapping(path = "/manage-bus-route/update/{id}", method = RequestMethod.GET)
    public String editBus(@PathVariable int id, Model model) {
        Optional<BusRoute> busRoute = busRouteRepo.findById(id);
        model.addAttribute("bus_route", busRoute.get());
        return "ManageRoute/UpdateRoute";
    }

    //update
    @RequestMapping(value = "/manage-bus-route/update", method = RequestMethod.POST)
    public String updateBus(Model model, @ModelAttribute @Valid BusRoute busRoute, BindingResult bindingResult, @RequestParam("olddate") Date oldDate) {
        if (bindingResult.hasErrors()) {
            return "ManageRoute/UpdateRoute";
        } else {
            List<BusRoute> busRoutes = (List<BusRoute>) busRouteRepo.findAll();
            if (!busRoute.getDepartureDate().toString().equals(oldDate.toString())) {
                for (BusRoute b : busRoutes) {
                    //Nếu trùng ngày thì load lại trang update
                    if (b.getOrigin().toLowerCase().equals(busRoute.getOrigin().toLowerCase()) && b.getDestination().toLowerCase().equals(busRoute.getDestination().toLowerCase())
                            && b.getDepartureDate().toString().equals(busRoute.getDepartureDate().toString())) {
                        model.addAttribute("error", "Ngày khởi hành đã tồn tại");
                        model.addAttribute("bus_route", busRoute);
                        return "ManageRoute/UpdateRoute";
                    }
                }
            }
            busRouteRepo.save(busRoute);
            return "redirect:/manage-bus-route";
        }
    }

    //  Xóa tuyến đường
    @RequestMapping(path = "/manage-bus-route/delete/{id}", method = RequestMethod.GET)
    public String delRoute(@PathVariable int id, Model model) {

        BusRoute busRoute = busRouteRepo.findById(id).get();
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        for (Bus b : busList) {
            if (b.getBusRoute().equals(busRoute)) {
                model.addAttribute("delerror", "Không thể xoá tuyến đường này");
                model.addAttribute("busRouteList", busRouteList);
                return "ManageRoute/ListBusRoute";
            }
        }
        busRouteRepo.delete(busRoute);
        return "redirect:/manage-bus-route";
    }
}
