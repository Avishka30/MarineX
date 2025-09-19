package lk.ijse.gdse.backend.service.impl;

import lk.ijse.gdse.backend.dto.WorkerAssignmentDTO;
import lk.ijse.gdse.backend.entity.BookingService;
import lk.ijse.gdse.backend.entity.Worker;
import lk.ijse.gdse.backend.entity.WorkerAssignment;
import lk.ijse.gdse.backend.repository.BookingServiceRepository;
import lk.ijse.gdse.backend.repository.WorkerAssignmentRepository;
import lk.ijse.gdse.backend.repository.WorkerRepository;
import lk.ijse.gdse.backend.service.WorkerAssignmentService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class WorkerAssignmentServiceImpl implements WorkerAssignmentService {

    private final WorkerAssignmentRepository assignmentRepo;
    private final BookingServiceRepository bookingServiceRepo;
    private final WorkerRepository workerRepo;
    private final JavaMailSender mailSender;

    public WorkerAssignmentServiceImpl(WorkerAssignmentRepository assignmentRepo,
                                       BookingServiceRepository bookingServiceRepo,
                                       WorkerRepository workerRepo,
                                       JavaMailSender mailSender) {
        this.assignmentRepo = assignmentRepo;
        this.bookingServiceRepo = bookingServiceRepo;
        this.workerRepo = workerRepo;
        this.mailSender = mailSender;
    }

    @Override
    public WorkerAssignmentDTO assignWorker(WorkerAssignmentDTO dto) {
        BookingService bookingService = bookingServiceRepo.findById(dto.getBookingServiceId())
                .orElseThrow(() -> new RuntimeException("Booking Service not found"));

        Worker worker = workerRepo.findById(dto.getWorkerId())
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        WorkerAssignment assignment = new WorkerAssignment();
        assignment.setBookingService(bookingService);
        assignment.setWorker(worker);
        assignment.setAssignedDate(dto.getAssignedDate() != null ? dto.getAssignedDate() : java.time.LocalDate.now());

        WorkerAssignment saved = assignmentRepo.save(assignment);

        // Send mail
        sendAssignmentEmail(worker, bookingService);

        return mapToDTO(saved);
    }

    private void sendAssignmentEmail(Worker worker, BookingService bookingService) {
        String subject = "New Work Assignment - Booking ID: " + bookingService.getBooking().getBookingId();
        String body = "Hello " + worker.getFullName() + ",\n\n" +
                "You have been assigned a new task.\n\n" +
                "Booking Details:\n" +
                "- Booking ID: " + bookingService.getBooking().getBookingId() + "\n" +
                "- Vessel: " + bookingService.getBooking().getVessel().getName() + "\n" +
                "- Berth: " + bookingService.getBooking().getBerth().getBerthNumber() + "\n" +
                "- Service: " + bookingService.getService().getName() + "\n" +
                "- Start Date: " + bookingService.getBooking().getBookingDate() + "\n" +
                "- End Date: " + bookingService.getBooking().getEndDate() + "\n\n" +
                "Please be prepared on time.\n\n" +
                "Regards,\nMarineX Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(worker.getEmail());
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    @Override
    public List<WorkerAssignmentDTO> getAssignmentsByBookingService(Long bookingServiceId) {
        return assignmentRepo.findByBookingServiceBookingServiceId(bookingServiceId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public List<WorkerAssignmentDTO> getAllAssignments() {
        return assignmentRepo.findAll()
                .stream().map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private WorkerAssignmentDTO mapToDTO(WorkerAssignment assignment) {
        return new WorkerAssignmentDTO(
                assignment.getAssignmentId(),
                assignment.getBookingService().getBookingServiceId(),
                assignment.getWorker().getWorkerId(),
                assignment.getAssignedDate()
        );
    }
}
