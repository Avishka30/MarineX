package lk.ijse.gdse.backend.controller;

import lk.ijse.gdse.backend.dto.BookingDTO;
import lk.ijse.gdse.backend.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    // Create a new booking
    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@RequestBody BookingDTO bookingDTO) {
        BookingDTO created = bookingService.createBooking(bookingDTO);
        return ResponseEntity.ok(created);
    }

    // Update booking by ID
    @PutMapping("/{id}")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        BookingDTO updated = bookingService.updateBooking(id, bookingDTO);
        return ResponseEntity.ok(updated);
    }

    // Delete booking by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        String message = bookingService.deleteBooking(id);
        return ResponseEntity.ok(message);
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        BookingDTO booking = bookingService.getBookingById(id);
        return ResponseEntity.ok(booking);
    }

    // Get all bookings
    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        List<BookingDTO> bookings = bookingService.getAllBookings();
        return ResponseEntity.ok(bookings);
    }

    // Get bookings by agent
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<BookingDTO>> getBookingsByAgent(@PathVariable Long agentId) {
        List<BookingDTO> bookings = bookingService.getBookingsByAgent(agentId);
        return ResponseEntity.ok(bookings);
    }
}
