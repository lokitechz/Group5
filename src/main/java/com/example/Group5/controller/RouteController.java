package com.example.Group5.controller;

import com.example.Group5.entity.Bus;
import com.example.Group5.entity.BusRoute;
import com.example.Group5.repository.BusRepo;
import com.example.Group5.repository.BusRouteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Iterator;
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
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        model.addAttribute("busList", busList);
        model.addAttribute("bus_route", new BusRoute());
        return "ManageRoute/CreateRoute";
    }

    //  Lưu thông tin tuyến đường lên database
    @RequestMapping(path = "/manage-route/create", method = RequestMethod.POST)
    public String saveRoute(Model model, @ModelAttribute BusRoute busRoute,@RequestParam float fare) {
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        //  Lấy ra danh sách các tuyến đường trong ngày
        List<BusRoute> busRoutes = busRouteRepo.findBusRouteByDepartureDate(busRoute.getDepartureDate());
        for (BusRoute br : busRoutes) {
            if (busRoute.getOrigin().equalsIgnoreCase(br.getOrigin())
                    && busRoute.getDestination().equalsIgnoreCase(br.getDestination())
                    && busRoute.getDepartureDate().equals(br.getDepartureDate())) {
                model.addAttribute("exist", "Tuyến đường đã tồn tại");
                model.addAttribute("busList", busList);
                model.addAttribute("bus_route", new BusRoute());
                return "ManageRoute/CreateRoute";
            }
        }
        if (fare == 0) {
            model.addAttribute("isError", "Giá vé phải lớn hơn 0");
            model.addAttribute("busList", busList);
            model.addAttribute("bus_route", new BusRoute());
            return "ManageRoute/CreateRoute";
        }
        busRoute.setFare(fare);
        busRouteRepo.save(busRoute);
        return "redirect:/manage-route";
    }
//
//    //  Trả về trang sửa thông tin tuyến đường
//    @RequestMapping(path = "/manage-route/update/{id}", method = RequestMethod.GET)
//    public String editRoutePage(@PathVariable int id, Model model, RedirectAttributes red) {
//        List<Bus> busList = (List<Bus>) busRepo.findAll();
//        Optional<BusRoute> busRoute = busRouteRepo.findById(id);
//        //list cac tuyen duong trong ngay
//        List<BusRoute> busRoutes = busRouteRepo.findBusRouteByDepartureDate(busRoute.get().getDepartureDate());
//        busList.remove(busRoute.get().getBus());
//        Iterator<Bus> busIterator = busList.iterator();
//        while (busIterator.hasNext()){
//            Bus bus = busIterator.next();
//            for (BusRoute br: busRoutes){
//                if(br.getBus().getBusId() == bus.getBusId()){
//                    busIterator.remove();
//                }
//            }
//        }
//        //neu tat ca cac xe deu co tuyen duong roi thi load lai trang list(chua co thong bao)
//        if (busList.size() > 0) {
//            model.addAttribute("presentBus", busRoute.get().getBus());
//            model.addAttribute("busList", busList);
//            model.addAttribute("bus_route", busRoute.get());
//        } else {
//            red.addFlashAttribute("empty", "Tất cả các xe đã có hành trình");
//            return "redirect:/manage-route";
//        }
//        return "ManageRoute/UpdateRoute";
//    }
//
//    // Lưu thông tin sau khi chỉnh sửa tuyến đường
//    @RequestMapping(value = "/manage-route/update", method = RequestMethod.POST)
//    public String updateBus(Model model, @ModelAttribute BusRoute busRoute) {
//        busRouteRepo.save(busRoute);
//        return "redirect:/manage-route";
//    }

}
