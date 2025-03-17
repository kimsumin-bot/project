
package com.example.project.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.project.entity.Screening;
import com.example.project.service.BookingService;

@Controller
public class BookingController {

    @Autowired
    BookingService service;

    @GetMapping("/booking")
    public String bookingPage(Model model) {
        model.addAttribute("movies", service.getAllMovies());
        model.addAttribute("theaters", service.getAllTheaters());
        return "/main/booking";
    }

    @GetMapping("/screenings")
    public List<Screening> getScreenings(
            @RequestParam Long movieId,
            @RequestParam Long theaterId,
            @RequestParam String date) {
        
        return service.getScreeningsByMovieAndTheater(movieId, theaterId, date);
    }

    public static class ScreeningDTO {
        private Long id;
        private String screenTime;

        public ScreeningDTO(Long id, String screenTime) {
            this.id = id;
            this.screenTime = screenTime;
        }

        public Long getId() { return id; }
        public String getScreenTime() { return screenTime; }
    }

    @GetMapping("/select-seat")
    public String selectSeat(@RequestParam("screeningId") Long screeningId, Model model) {
        Screening screening = service.getScreeningById(screeningId);
        model.addAttribute("screening", screening);
        return "/seat/select-seat";
    }

    @GetMapping("/payments")
    public String paymentsPage(@RequestParam("screeningId") Long screeningId,
                               @RequestParam("seats") String seats,
                               Model model) {
        model.addAttribute("screeningId", screeningId);
        model.addAttribute("seats", seats);
        return "/pay/payments";
    }

    @PostMapping("/confirm")
    @ResponseBody
    public Map<String, Object> confirmPayment(@RequestBody Map<String, Object> requestData) {
        String paymentKey = requestData.get("paymentKey").toString();
        String orderId = requestData.get("orderId").toString();
        int amount = Integer.parseInt(requestData.get("amount").toString());

        Map<String, Object> response = new HashMap<>();
        response.put("paymentKey", paymentKey);
        response.put("orderId", orderId);
        response.put("amount", amount);
        response.put("status", "DONE");

        return response;
    }

    @GetMapping("/fail")
    public ModelAndView paymentFail(@RequestParam(value = "message", required = false, defaultValue = "결제를 실패했습니다.") String message,
                                    @RequestParam(value = "code", required = false, defaultValue = "ERROR") String code) {
        ModelAndView modelAndView = new ModelAndView("redirect:/fail.html");
        modelAndView.addObject("message", message);
        modelAndView.addObject("code", code);
        return modelAndView;
    }

    @GetMapping("/price")
    public ResponseEntity<Map<String, Integer>> getMoviePrice() {
        Map<String, Integer> response = new HashMap<>();
        response.put("price", 12000);
        return ResponseEntity.ok(response);
    }
}
