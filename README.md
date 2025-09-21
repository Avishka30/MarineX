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
<img width="1366" height="768" alt="Screenshot (113)" src="https://github.com/user-attachments/assets/6f07b621-b538-4e14-b357-9ac83d1d0c39" />
- **Agent Management:** Approve or reject agents. Approved agents receive email notifications.
- **Berth Management:** Add, update, and delete berths with per-day pricing.
 <img width="1366" height="768" alt="Screenshot (114)" src="https://github.com/user-attachments/assets/d62bce24-4149-45d6-a3db-619fc18d607b" />
- **Worker Management:** Add, update, and assign workers with detailed specifications.
 <img width="1366" height="768" alt="Screenshot (115)" src="https://github.com/user-attachments/assets/cdcdd4e9-8331-4757-9801-4ab32c0d6cae" />
- **Service Management:** Add, update, delete services with pricing options.
  <img width="1366" height="768" alt="Screenshot (116)" src="https://github.com/user-attachments/assets/1529fe06-f65c-4dba-a424-bc7f357fb39c" />
- **Booking Management:** View all bookings, approve bookings, and assign workers.
  <img width="1366" height="768" alt="Screenshot (117)" src="https://github.com/user-attachments/assets/f68b455f-b136-4f3c-97ea-65ebc229a676" />
  <img width="1366" height="768" alt="Screenshot (118)" src="https://github.com/user-attachments/assets/1c92f66b-f51d-49a8-817e-2156573154c2" />
- **Payment Overview:** Track all payments made by agents.
- **Schedule Management:** Visualize all bookings and worker assignments using a calendar for better UX.
  <img width="1366" height="768" alt="image" src="https://github.com/user-attachments/assets/dbe46ecc-43e6-4fed-8ac2-4174e4733d32" />
- **Notifications:** Admin can notify agents about booking approvals or assigned tasks.
<img width="1366" height="768" alt="Screenshot (120)" src="https://github.com/user-attachments/assets/bd3c225d-47b5-4fbf-847b-6ef49a3c8ed6" />

### Agent Features
- **Secure Sign-in:** Agents sign in using their license code and wait for admin approval.
- **Vessel Management:** Add, update, and view vessels.
 <img width="1366" height="768" alt="Screenshot (124)" src="https://github.com/user-attachments/assets/1473fcfa-c073-4142-b42f-b33f16c7b07d" />

- **Booking System:** 
  - Select available berths based on date and time.
  - Choose services for the booking.
  - Specify purpose and cargo details.
  - Auto-check berth availability.
  - Wait for admin approval after booking.
    <img width="1366" height="768" alt="Screenshot (122)" src="https://github.com/user-attachments/assets/01f3a662-836f-4560-9c98-562059d98ec7" />
    <img width="1366" height="768" alt="Screenshot (123)" src="https://github.com/user-attachments/assets/a19aba75-1523-4491-8886-e058dccad960" />
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

## Setup Instructions
Follow these steps to run the project locally.

# Full Project
Clone the repository:
   git clone (https://github.com/Avishka30/MarineX.git)
Configure the database in application.properties:

  spring.datasource.url=jdbc:mysql://localhost:3306/hms
  spring.datasource.username=your_username
  spring.datasource.password=your_password
  spring.jpa.hibernate.ddl-auto=update
  
Run the backend application:

  mvn spring-boot:run
  Run the frontend: Open the index.html file (Use Live Server)
  
Additional Configurations -
  Stripe API Key → Add your API key in the Stripe Payment.
    Youtube URL -
    https://youtu.be/MRz4YHdByGg?si=FRhVQxId77bWElTh
