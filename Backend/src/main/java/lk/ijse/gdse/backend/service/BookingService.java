package lk.ijse.gdse.backend.service;

import lk.ijse.gdse.backend.dto.BookingDTO;
import java.util.List;

public interface BookingService {

    BookingDTO createBooking(BookingDTO bookingDTO);

    BookingDTO updateBooking(Long id, BookingDTO bookingDTO);

    String deleteBooking(Long id);

    BookingDTO getBookingById(Long id);

    List<BookingDTO> getAllBookings();

    List<BookingDTO> getBookingsByAgent(Long agentId);
}
