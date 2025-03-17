package com.example.project.service;

import com.example.project.entity.Booking;
import com.example.project.entity.Movie;
import com.example.project.entity.Screening;
import com.example.project.entity.Theater;
import com.example.project.repository.BookingRepository;
import com.example.project.repository.MovieRepository;
import com.example.project.repository.ScreeningRepository;
import com.example.project.repository.TheaterRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    ScreeningRepository screeningRepository;

    @Autowired
    BookingRepository bookingRepository;
    
    @Autowired
    MovieRepository movieRepository;
    
    @Autowired
    TheaterRepository theaterRepository;
    
    public List<Theater> getAllTheaters() {
        return theaterRepository.findAll();
    }
    
    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Screening getScreeningById(Long screeningId) {
        return screeningRepository.findById(screeningId).orElse(null);
    }

    public List<Booking> getBookedSeats(Long screeningId) {
        return bookingRepository.findByScreeningId(screeningId);
    }

    public Booking reserveSeat(Long screeningId, String seatNumber, Long userId, int price) {
        Screening screening = getScreeningById(screeningId);
        if (screening == null) {
            throw new IllegalArgumentException("상영 정보를 찾을 수 없습니다.");
        }

        // @AllArgsConstructor가 있으므로 생성자 호출 가능
        Booking booking = new Booking(null, screening, seatNumber, userId, "PENDING", price);
        return bookingRepository.save(booking);
    }

    public void updateBookingStatus(Long bookingId, String status) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setPaymentStatus(status);
            bookingRepository.save(booking);
        } else {
            throw new IllegalArgumentException("예약 정보를 찾을 수 없습니다.");
        }
    }
    
    public List<Screening> getScreeningsByMovieAndTheater(Long movieId, Long theaterId, String date) {
        LocalDate parsedDate = LocalDate.parse(date);
        LocalDateTime startOfDay = parsedDate.atStartOfDay(); // 00:00:00
        LocalDateTime endOfDay = parsedDate.atTime(LocalTime.MAX); // 23:59:59

        System.out.println("Searching for screenings between: " + startOfDay + " - " + endOfDay);
        
        return screeningRepository.findByMovieIdAndTheaterIdAndScreenTimeBetween(
                movieId, theaterId, startOfDay, endOfDay);
    }
}
