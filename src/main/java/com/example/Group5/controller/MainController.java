package com.example.Group5.controller;

import com.example.Group5.entity.AppUser;
import com.example.Group5.entity.Bus;
import com.example.Group5.entity.BusRoute;
import com.example.Group5.entity.Ticket;
import com.example.Group5.repository.*;
import com.example.Group5.utils.EncrytedPasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.Principal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
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

    //  Chuyển sang trang đổi mật khẩu
    @RequestMapping(path = "/change-password/{name}", method = RequestMethod.GET)
    public String changePassword(Model model, @PathVariable String name) {
        AppUser appUser = appUserRepo.findAppUserByUserName(name);
        model.addAttribute("appUser", appUser);
        return "Common/ChangePassword";
    }

    // Đổi mật khẩu mà lưu lên database
    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public String updatePassword(Model model, AppUser appUser, @RequestParam("oldpass") String oldPass, @RequestParam("newpass") String newPass, @RequestParam("renewpass") String reNewPass) {
        if (!EncrytedPasswordUtils.comparePassword(oldPass, appUser.getEncrytedPassword())) {
            model.addAttribute("error", "Mật khẩu không đúng!");
            model.addAttribute("appUser", appUser);
            return "Common/ChangePassword";
        } else {
            if (!newPass.equals(reNewPass)) {
                model.addAttribute("error", "Mật khẩu mới không giống nhau");
                model.addAttribute("appUser", appUser);
                return "Common/ChangePassword";
            } else {
                appUser.setEncrytedPassword(EncrytedPasswordUtils.encrytePassword(newPass));
                appUserRepo.save(appUser);
                return "Common/LoginForm";
            }
        }
    }

    // Trả về trang 403 khi không có quyền
    @RequestMapping(value = "/403", method = RequestMethod.GET)
    public String accessDenied() {
        return "Common/403Page";
    }

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
                for (Bus bus : busRepo.findAllByBusId(ticket.getBusId())) {
                    revenue += bus.getBusRoute().getFare() * perBusRoute;
                }
            }
        }
        model.addAttribute("DashboardTicket", order);
        model.addAttribute("DashboardWeeklySale", total);
        model.addAttribute("revenue", Integer.toString(revenue));
        return "Common/Dashboard";
    }

    //  Trả về danh sách kết quả tìm kiếm của khách hàng
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String search(Model model, @RequestParam(name = "origin") String origin,
                         @RequestParam(name = "destination") String destination,
                         @RequestParam(name = "date") String date, RedirectAttributes red) {
        int totalSold = 0;
        List<BusRoute> busRouteList = (List<BusRoute>) busRouteRepo.findAll();
        List<Bus> busList = (List<Bus>) busRepo.findAll();
        List<Bus> buses = new ArrayList<>();
        for (BusRoute busRoute : busRouteList) {
            if (busRoute.getOrigin().equalsIgnoreCase(origin)
                    && busRoute.getDestination().equalsIgnoreCase(destination)
                    && busRoute.getDepartureDate().toString().equals(date)) {
                for (Bus bus : busList) {
                    if (bus.getBusRoute().getBusRouteId() == busRoute.getBusRouteId()) {
                        buses.add(bus);
                        model.addAttribute("listSearch", buses);
                        List<Ticket> tickets = ticketRepo.findAllByBusId(bus.getId());
                        for (Ticket item : tickets) {
                            totalSold += item.getAmount();
                            if (totalSold == busRepo.findById(bus.getId()).get().getBusType().getTotalSeat()) {
                                model.addAttribute("TicketInfo", item.getBusId());
                                model.addAttribute("totalSold", totalSold);
                            } else {
                                model.addAttribute("TicketInfo", item.getBusId());
                                model.addAttribute("totalSold", totalSold);
                            }
                        }
                    }
                }
            }
        }
        if (buses.isEmpty()) {
            red.addFlashAttribute("isEmpty", "Xin lỗi, chúng tôi không thể tìm được kết quả hợp với tìm kiếm của bạn");
            return "redirect:/";
        }
        return "Customer/HomePage";
    }

    //  Trả về trang đặt vé cho khách hàng
    @RequestMapping(value = "/customer/booking-ticket/{id}", method = RequestMethod.GET)
    public String bookingPage(@PathVariable int id, Model model) {
        Optional<Bus> optionalBus = busRepo.findById(id);
        model.addAttribute("bus", optionalBus.get());
        model.addAttribute("ticket", new Ticket());
        return "Customer/BookingPageforCustomer";
    }

    //  Lưu thông tin đặt vé của khách hàng
    @RequestMapping(value = "/customer/booking-ticket/{id}", method = RequestMethod.POST)
    public String saveTicket(@PathVariable int id, @ModelAttribute Ticket ticket, Principal principal, RedirectAttributes red) {
        String username = principal.getName();
        //  Lấy ra danh sách tất cả ticket đã đặt của xe đó
        List<Ticket> tickets = ticketRepo.findAllByBusId(id);
        int totalSold = 0;
        for (Ticket item : tickets) {
            totalSold += item.getAmount();
        }
        if (ticket.getAmount() == 0) {
            red.addFlashAttribute("msg", "Số lượng vé phải lớn hơn 0");
        } else if (totalSold + ticket.getAmount() > busRepo.findById(id).get().getBusType().getTotalSeat()) {
            red.addFlashAttribute("soldout", "Số lượng vé bạn muốn đặt không đủ");
        } else {
            ticket.setBusId(id);
            ticket.setPassengerId(appUserRepo.findAppUserByUserName(username).getUserId());
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
                return "redirect:/customer/booking-ticket/detail/" + ticket.getTicketId();
            } catch (MessagingException e) {
                red.addFlashAttribute("sendMailError", "Server đang gặp sự cố,quý khách vui lòng quay lại sau");
                return "redirect:/customer/booking-ticket/{id}";
            }
        }
        return "redirect:/customer/booking-ticket/{id}";
    }

    //  Trang thông tin chi tiết vé xe khách đã đặt
    @RequestMapping(value = "/customer/booking-ticket/detail/{id}", method = RequestMethod.GET)
    public String detailTicket(Model model, @PathVariable int id, Principal principal) {
        AppUser appUser = appUserRepo.findAppUserByUserName(principal.getName());
        Optional<Ticket> ticket = ticketRepo.findById(id);
        Optional<Bus> bus = busRepo.findById(ticket.get().getBusId());
        model.addAttribute("CustomerInfo", appUser);
        model.addAttribute("TicketInfo", ticket.get());
        model.addAttribute("BusInfo", bus.get());
        return "Customer/InfoTicket";
    }

    //  Trả về trang thanh toán
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.GET)
    public String paymentPage(@PathVariable int id) {
        return "Customer/PaymentPage";
    }

    //  Thanh toán
    @RequestMapping(value = "/payment/{id}", method = RequestMethod.POST)
    public String paymentOffline(@PathVariable int id, RedirectAttributes red) {
        red.addFlashAttribute("success", "Mời bạn đến địa điểm giao dịch gần nhất của chúng tôi để hoàn tất thủ tục thanh toán");
        return "redirect:/";
    }

    //  Trả về trang thông tin cá nhân của tài khoản
    @RequestMapping(value = "/{username}")
    public String userInfo(@PathVariable String username) {
        return "Common/UserInfoPage";
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
