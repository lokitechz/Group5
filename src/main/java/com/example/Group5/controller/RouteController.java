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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
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
    @RequestMapping(value = "/manage-route", method = RequestMethod.GET)
    public String listRoute(Model model) {
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        model.addAttribute("busRouteList", busRouteList);
        return "ManageRoute/ListBusRoute";
    }

    //  Trả về trang tạo mới tuyến đường
    @RequestMapping(path = "/manage-route/create", method = RequestMethod.GET)
    public String createRoutePage(Model model) {
        model.addAttribute("bus_route", new BusRoute());
        return "ManageRoute/CreateRoute";
    }

    //  Lưu thông tin tuyến đường lên database
    @RequestMapping(path = "/manage-route/create", method = RequestMethod.POST)
    public String saveRoute(Model model, @ModelAttribute BusRoute busRoute) {
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        for (BusRoute b : busRouteList) {
            //  Nếu trùng ngày thì load lại trang create
            if (b.getOrigin().toLowerCase().equals(busRoute.getOrigin().toLowerCase()) && b.getDestination().toLowerCase().equals(busRoute.getDestination().toLowerCase())
                    && b.getDepartureDate().toString().equals(busRoute.getDepartureDate().toString())) {
                model.addAttribute("error", "Hành trình đã tồn tại");
                model.addAttribute("bus_route", new BusRoute());
                return "ManageRoute/CreateRoute";
            }
        }
        busRouteRepo.save(busRoute);
        return "redirect:/manage-route";
    }

    //  Trả về trang sửa thông tin tuyến đường
    @RequestMapping(path = "/manage-route/update/{id}", method = RequestMethod.GET)
    public String editRoutePage(@PathVariable int id, Model model) {
        Optional<BusRoute> busRoute = busRouteRepo.findById(id);
        model.addAttribute("bus_route", busRoute.get());
        return "ManageRoute/UpdateRoute";
    }

    // Lưu thông tin sau khi chỉnh sửa tuyến đường
    @RequestMapping(value = "/manage-route/update", method = RequestMethod.POST)
    public String updateBus(Model model, @ModelAttribute BusRoute busRoute, @RequestParam("olddate") Date oldDate) {
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
        return "redirect:/manage-route";
    }


    //  Xóa tuyến đường
    @RequestMapping(path = "/manage-route/delete/{id}", method = RequestMethod.GET)
    public String delRoute(@PathVariable int id, RedirectAttributes red) {
        BusRoute busRoute = busRouteRepo.findById(id).get();
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        for (Bus b : busList) {
            if (b.getBusRoute().equals(busRoute)) {
                red.addFlashAttribute("deleteError", "Không thể xoá tuyến đường này");
                return "redirect:/manage-route";
            }
        }
        busRouteRepo.delete(busRoute);
        return "redirect:/manage-route";
    }
}
