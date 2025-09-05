package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.BookingDTO;
import lk.ijse.gdse.backend.entity.*;
import lk.ijse.gdse.backend.repository.*;
import lk.ijse.gdse.backend.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final VesselRepository vesselRepository;
    private final BerthRepository berthRepository;
    private final UserRepository agentRepository;
    private final ServicesRepository serviceRepository;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              VesselRepository vesselRepository,
                              BerthRepository berthRepository,
                              UserRepository agentRepository,
                              ServicesRepository serviceRepository) {
        this.bookingRepository = bookingRepository;
        this.vesselRepository = vesselRepository;
        this.berthRepository = berthRepository;
        this.agentRepository = agentRepository;
        this.serviceRepository = serviceRepository;
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Vessel vessel = vesselRepository.findById(bookingDTO.getVesselId())
                .orElseThrow(() -> new RuntimeException("Vessel not found"));
        Berth berth = berthRepository.findById(bookingDTO.getBerthId())
                .orElseThrow(() -> new RuntimeException("Berth not found"));
        User agent = agentRepository.findById(bookingDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        //  Check vessel ownership
        if (!vessel.getAgent().getUserId().equals(agent.getUserId())) {
            throw new RuntimeException("Vessel does not belong to this agent!");
        }

        // Check berth availability
        boolean berthTaken = bookingRepository.existsByBerthAndDateRange(
                berth.getBerthId(),
                bookingDTO.getBookingDate(),
                bookingDTO.getEndDate()
        );

        if (berthTaken) {
            throw new RuntimeException("This berth is already booked for the selected date range!");
        }

        List<Services> services = serviceRepository.findAllById(bookingDTO.getServiceIds());

        // Total price calculation
        double serviceCost = services.stream().mapToDouble(Services::getPrice).sum();
        long days = java.time.temporal.ChronoUnit.DAYS.between(
                bookingDTO.getBookingDate(), bookingDTO.getEndDate()
        );
        if (days <= 0) days = 1;
        double totalPrice = serviceCost + berth.getPrice().doubleValue() * days;

        Booking booking = new Booking();
        booking.setVessel(vessel);
        booking.setBerth(berth);
        booking.setAgent(agent);
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setEndDate(bookingDTO.getEndDate());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);

        // Map services to BookingService entity
        List<lk.ijse.gdse.backend.entity.BookingService> bookingServices = services.stream().map(s -> {
            lk.ijse.gdse.backend.entity.BookingService bs = new lk.ijse.gdse.backend.entity.BookingService();
            bs.setBooking(booking);
            bs.setService(s);
            return bs;
        }).toList();
        booking.setBookingServices(bookingServices);

        bookingRepository.save(booking);

        bookingDTO.setTotalPrice(totalPrice);
        bookingDTO.setStatus(BookingStatus.PENDING);

        return bookingDTO;
    }


    @Override
    public BookingDTO updateBooking(Long id, BookingDTO bookingDTO) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setEndDate(bookingDTO.getEndDate());

        long days = java.time.temporal.ChronoUnit.DAYS.between(
                bookingDTO.getBookingDate(), bookingDTO.getEndDate()
        );
        if (days <= 0) days = 1;

        double serviceCost = booking.getBookingServices().stream()
                .mapToDouble(bs -> bs.getService().getPrice())
                .sum();
        double berthCost = booking.getBerth().getPrice().doubleValue() * days;

        booking.setTotalPrice(serviceCost + berthCost);

        bookingRepository.save(booking);

        bookingDTO.setTotalPrice(booking.getTotalPrice());
        bookingDTO.setStatus(booking.getStatus());

        return bookingDTO;
    }

    @Override
    public String deleteBooking(Long id) {
        if (!bookingRepository.existsById(id)) {
            throw new RuntimeException("Booking not found");
        }
        bookingRepository.deleteById(id);
        return "Booking deleted successfully";
    }

    @Override
    public BookingDTO getBookingById(Long id) {
        return bookingRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("Booking not found"));
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDTO> getBookingsByAgent(Long agentId) {
        // Use the correct repository method for agent's userId
        return bookingRepository.findByAgentUserId(agentId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private BookingDTO toDTO(Booking booking) {
        return new BookingDTO(
                booking.getBookingId(),
                booking.getVessel().getVesselId(),
                booking.getBerth().getBerthId(),
                booking.getAgent().getUserId(),
                booking.getBookingServices().stream()
                        .map(bs -> bs.getService().getServiceId())
                        .toList(),
                booking.getBookingDate(),
                booking.getEndDate(),
                booking.getTotalPrice(),
                booking.getStatus()
        );
    }
}
