package com.example.Group5.controller;

import com.example.Group5.entity.*;
import com.example.Group5.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    AppUserRepo appUserRepo;

    @Autowired
    AppRoleRepo appRoleRepo;

    @Autowired
    BusRouteRepo busRouteRepo;

    @Autowired
    BusRepo busRepo;

    @Autowired
    BusTypeRepo busTypeRepo;

    @Autowired
    TicketRepo ticketRepo;

    @Autowired
    public JavaMailSender emailSender;

    // Trang chính khi chạy chương trình
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String homePage(Model model) {
        return "Customer/HomePage";
    }

    // Trả về trang đăng nhập
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "Common/LoginForm";
    }

    // Trả về trang 403 khi không có quyền
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "Common/403Page";
    }

    //  Trả về danh sách kết quả tìm kiếm của khách hàng
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @RequestParam(name = "origin") String origin,
                         @RequestParam(name = "destination") String destination,
                         @RequestParam(name = "date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, RedirectAttributes red) {
        BusRoute busRoute = busRouteRepo.search(origin, destination, date);
        if (busRoute != null) {
            Optional<Bus> bus = busRepo.findById(busRoute.getBusId());
            Optional<BusType> busType = busTypeRepo.findById(bus.get().getBusTypeId());
            int totalSold = 0;
            List<Ticket> tickets = (List<Ticket>) ticketRepo.findAll();
            for (Ticket ticket : tickets) {
                if (ticket.getRouteId() == busRoute.getRouteId()) {
                    totalSold += ticket.getAmount();
                }
            }
            if (totalSold == busType.get().getTotalSeat()) {
                model.addAttribute("soldOut", "Hết vé");
            } else {
                model.addAttribute("totalSold", totalSold);
            }
            model.addAttribute("busInfo", bus.get());
            model.addAttribute("busTypeInfo", busType.get());
            model.addAttribute("result", busRoute);
            return "Customer/HomePage";
        } else {
            red.addFlashAttribute("isEmpty", "Xin lỗi, chúng tôi không thể tìm được kết quả hợp với tìm kiếm của bạn");
            return "redirect:/";
        }
    }

    //  Trả về trang đặt vé cho khách hàng
    @RequestMapping(value = "/customer/booking-ticket/{id}", method = RequestMethod.GET)
    public String bookingPage(@PathVariable int id, Model model) {
        Optional<BusRoute> busRoute = busRouteRepo.findById(id);
        if (busRoute.isPresent()) {
            Optional<Bus> bus = busRepo.findById(busRoute.get().getBusId());
            if (bus.isPresent()) {
                Optional<BusType> busType = busTypeRepo.findById(bus.get().getBusTypeId());
                model.addAttribute("bustype", busType.get());
            }
            model.addAttribute("bus", bus.get());
            model.addAttribute("busroute", busRoute.get());
            model.addAttribute("ticket", new Ticket());
        }
        return "Customer/BookingPageforCustomer";
    }

    //  Lấy thông tin đặt vé và truyển về trang thanh toán
    @RequestMapping(value = "/customer/payment/{id}", method = RequestMethod.POST)
    public String paymentPage(@PathVariable int id, @ModelAttribute Ticket ticket, RedirectAttributes red) {
        Optional<BusRoute> busRoute = busRouteRepo.findById(id);
        Optional<Bus> bus = busRepo.findById(busRoute.get().getBusId());
        Optional<BusType> busType = busTypeRepo.findById(bus.get().getBusTypeId());
        //  Lấy ra danh sách tất cả ticket đã đặt của hành trình đó
        int totalSold = 0;
        List<Ticket> tickets = (List<Ticket>) ticketRepo.findAll();
        for (Ticket item : tickets) {
            if (item.getRouteId() == busRoute.get().getRouteId()) {
                totalSold += item.getAmount();
            }
        }
        if (ticket.getAmount() == 0) {
            red.addFlashAttribute("msg", "Số lượng vé phải lớn hơn 0");
            return "redirect:/customer/booking-ticket/{id}";
        } else if (totalSold + ticket.getAmount() > busType.get().getTotalSeat()) {
            red.addFlashAttribute("notEnough", "Số lượng vé bạn muốn đặt k đủ");
            return "redirect:/customer/booking-ticket/{id}";
        } else {
            red.addFlashAttribute("ticketInfo", ticket);
            return "redirect:/customer/payment/{id}";
        }
    }

    //  Trang thông tin chi tiết vé xe khách đã đặt
    @RequestMapping(value = "/customer/payment/{id}", method = RequestMethod.GET)
    public String detailTicket(Model model, @PathVariable int id, Principal principal) {
        AppUser appUser = appUserRepo.findAppUserByUserName(principal.getName());
        Optional<BusRoute> busRoute = busRouteRepo.findById(id);
        Optional<Bus> bus = busRepo.findById(busRoute.get().getBusId());
        Optional<BusType> busType = busTypeRepo.findById(bus.get().getBusTypeId());
        Ticket ticket = new Ticket();
        model.addAttribute("CustomerInfo", appUser);
        model.addAttribute("TicketInfo", ticket);
        model.addAttribute("BusRouteInfo", busRoute.get());
        model.addAttribute("BusInfo", bus.get());
        model.addAttribute("BusTypeInfo", busType.get());
        return "Customer/InfoTicket";
    }

    //  Trả về trang thanh toán
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.POST)
    public String paymentMethod(@PathVariable int id, Principal principal, @ModelAttribute Ticket ticket,
                                @RequestParam int amount, @RequestParam int paymentOp, RedirectAttributes red) {
        String username = principal.getName();
        AppUser appUser = appUserRepo.findAppUserByUserName(principal.getName());
        ticket.setAmount(amount);
        ticket.setRouteId(id);
        ticket.setUserId(appUserRepo.findAppUserByUserName(username).getUserId());
        ticket.setStatus(false);
        ticketRepo.save(ticket);
        String subject = "Cảm ơn bạn đã sử dụng dịch vụ của chúng tôi";
        String TicketInfo = "<h3>Đây là thông tin chi tiết vé xe của bạn</h3>"
                + "<div>Họ và tên: " + appUserRepo.findAppUserByUserName(username).getFullName() + "</div>"
                + "<div>Email: " + appUserRepo.findAppUserByUserName(username).getEmail() + "</div>"
                + "<div>Số điện thoại: " + appUserRepo.findAppUserByUserName(username).getPhone() + "</div>"
                + "<div>Tuổi: " + appUserRepo.findAppUserByUserName(username).getAge() + "</div>"
                + "<div>Mã vé: TK-" + ticket.getTicketId() + "</div>"
                + "<div>Số xe đã đặt: " + ticket.getAmount() + "</div>"
                + "<h4>Lưu lý: Khi đến quầy quý khách vui lòng xuất trình mã vé để có thể lấy vé</h4>";

        String notification = "Thông báo";
        String notificationContent = "<h3>Có khách hàng vừa đặt vé trên hệ thống</h3>"
                + "<div>Khách hàng: " + appUserRepo.findAppUserByUserName(username).getFullName() + " vừa đặt vé trên hệ thống </div>"
                + "<div>Mã vé: TK-" + ticket.getTicketId() + "</div>";
        try {
            sendHTMLMail("vuachom94@gmail.com", notification, notificationContent);
            sendHTMLMail(appUserRepo.findAppUserByUserName(username).getEmail(), subject, TicketInfo);
        } catch (MessagingException e){
            red.addFlashAttribute("sendMailError", "Server đang gặp sự cố,quý khách vui lòng quay lại sau");
            return "redirect:/customer/booking-ticket/{id}";
        }
        if (paymentOp == 1) {
            return "Customer/PaymentPage";
        } else {
            red.addFlashAttribute("success", "Mời bạn đến địa điểm giao dịch gần nhất của chúng tôi để hoàn tất thủ tục thanh toán");
            return "redirect:/";
        }
    }

    // Use it to send HTML Email
    private void sendHTMLMail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo(to);
        helper.setSubject(subject);
        message.setContent(content, "text/html; charset=UTF-8");
        emailSender.send(message);
    }
}
