# MarineX - Smart Harbor Management & Booking System

MarineX is an advanced web application designed to streamline harbor operations and vessel bookings. The system ensures that only verified agents can manage bookings while giving administrators full control over harbor management, services, workers, and payments.

---

## Project Overview

MarineX simplifies harbor management by combining user-friendly UI/UX with powerful backend functionality. The system ensures:

- Secure agent verification using license codes.
- Real-time berth availability checks during bookings.
- Admin approval workflows for agents and bookings.
- Integration with Stripe for payments and automatic PDF invoice generation.
- Notifications for agents regarding booking status and assigned workers.

---

## Main Features

### Admin Features
- **Agent Management:** Approve or reject agents. Approved agents receive email notifications.
- **Berth Management:** Add, update, and delete berths with per-day pricing.
- **Worker Management:** Add, update, and assign workers with detailed specifications.
- **Service Management:** Add, update, delete services with pricing options.
- **Booking Management:** View all bookings, approve bookings, and assign workers.
- **Payment Overview:** Track all payments made by agents.
- **Schedule Management:** Visualize all bookings and worker assignments using a calendar for better UX.
- **Notifications:** Admin can notify agents about booking approvals or assigned tasks.

### Agent Features
- **Secure Sign-in:** Agents sign in using their license code and wait for admin approval.
- **Vessel Management:** Add, update, and view vessels.
- **Booking System:** 
  - Select available berths based on date and time.
  - Choose services for the booking.
  - Specify purpose and cargo details.
  - Auto-check berth availability.
  - Wait for admin approval after booking.
- **Payment Integration:** Pay 20% of the confirmed booking via Stripe.
- **Invoice Generation:** Download PDF invoices containing booking and payment details.
- **Notifications:** Receive real-time notifications on booking approval and worker assignment.

---

## Tech Stack

| Layer          | Technology / Tool |
|----------------|-----------------|
| Backend        | Java, Spring Boot, MySQL |
| Frontend       | HTML, CSS, Bootstrap, JavaScript (Fetch API) |
| Authentication | JWT (JSON Web Token) |
| Payment        | Stripe Payment Gateway |
| Notifications  | Email Notifications via JavaMail API |

---

## System Architecture

1. **Frontend:** Responsive UI built with Bootstrap and HTML/JS. Fetch API used to communicate with backend endpoints.
2. **Backend:** Spring Boot REST API handles authentication, agent approval, bookings, payments, and notifications.
3. **Database:** MySQL stores users, vessels, berths, bookings, payments, and services.
4. **Authentication:** JWT ensures secure session management.
5. **Payment Processing:** Stripe integration handles agent payments, triggers invoice generation, and updates booking status.
6. **Email Notifications:** Admin actions trigger automated emails for agents.

  Youtube URL - https://youtu.be/MRz4YHdByGg?si=FRhVQxId77bWElTh
