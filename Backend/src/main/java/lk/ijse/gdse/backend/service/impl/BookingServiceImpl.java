
package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.BookingDTO;
import lk.ijse.gdse.backend.entity.*;
import lk.ijse.gdse.backend.repository.*;
import lk.ijse.gdse.backend.service.BookingService;
import lk.ijse.gdse.backend.service.EmailService;
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
    private final EmailService emailService;

    public BookingServiceImpl(BookingRepository bookingRepository,
                              VesselRepository vesselRepository,
                              BerthRepository berthRepository,
                              UserRepository agentRepository,
                              ServicesRepository serviceRepository, EmailService emailService) {
        this.bookingRepository = bookingRepository;
        this.vesselRepository = vesselRepository;
        this.berthRepository = berthRepository;
        this.agentRepository = agentRepository;
        this.serviceRepository = serviceRepository;
        this.emailService = emailService;
    }

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        Vessel vessel = vesselRepository.findById(bookingDTO.getVesselId())
                .orElseThrow(() -> new RuntimeException("Vessel not found"));
        Berth berth = berthRepository.findById(bookingDTO.getBerthId())
                .orElseThrow(() -> new RuntimeException("Berth not found"));
        User agent = agentRepository.findById(bookingDTO.getAgentId())
                .orElseThrow(() -> new RuntimeException("Agent not found"));

        // Check vessel ownership
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

        // Create Booking entity
        Booking booking = new Booking();
        booking.setVessel(vessel);
        booking.setBerth(berth);
        booking.setAgent(agent);
        booking.setBookingDate(bookingDTO.getBookingDate());
        booking.setEndDate(bookingDTO.getEndDate());
        booking.setPurpose(bookingDTO.getPurpose());
        booking.setCargoCategory(bookingDTO.getCargoCategory());
        booking.setCargoQuantity(bookingDTO.getCargoQuantity());
        booking.setStatus(BookingStatus.PENDING);
        booking.setTotalPrice(totalPrice);

        // Map services to BookingService entity using full package
        List<lk.ijse.gdse.backend.entity.BookingService> bookingServices = services.stream().map(s -> {
            lk.ijse.gdse.backend.entity.BookingService bs = new lk.ijse.gdse.backend.entity.BookingService();
            bs.setBooking(booking);
            bs.setService(s);
            return bs;
        }).toList();
        booking.setBookingServices(bookingServices);

        bookingRepository.save(booking);

        // Map back to DTO
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
        booking.setPurpose(bookingDTO.getPurpose());
        booking.setCargoCategory(bookingDTO.getCargoCategory());
        booking.setCargoQuantity(bookingDTO.getCargoQuantity());

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
                booking.getStatus(),
                booking.getPurpose(),
                booking.getCargoCategory(),
                booking.getCargoQuantity(),
                null // createdAt not used
        );
    }

    @Override
    public BookingDTO approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Approve booking
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);

        // Send email
        String agentEmail = booking.getAgent().getEmail();
        double payAmount = booking.getTotalPrice() * 0.2; // 20% payment
        String subject = "Booking Approved - MarineX";
        String text = String.format("Dear %s,\n\nYour booking has been approved. Please pay 20%% (%.2f USD) to confirm your booking.\n\nThank you,\nMarineX Team",
                booking.getAgent().getFullName(), payAmount);

        emailService.sendEmail(agentEmail, subject, text);

        // Map to DTO
        BookingDTO dto = new BookingDTO();
        dto.setBookingId(booking.getBookingId());
        dto.setStatus(booking.getStatus());
        dto.setTotalPrice(booking.getTotalPrice());
        dto.setBookingDate(booking.getBookingDate());
        dto.setEndDate(booking.getEndDate());

        return dto;
    }
}
