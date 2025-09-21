document.addEventListener("DOMContentLoaded", () => {
    const savedTheme = localStorage.getItem('theme') || 'light';
    setTheme(savedTheme);

    const token = localStorage.getItem("token");
    const agentId = Number(localStorage.getItem("agentId"));
    if (!token || !agentId) { alert("You must log in first!"); window.location.href = "login.html"; return; }

    const vesselMap = new Map(), berthMap = new Map(), serviceMap = new Map();
    loadVessels(agentId, token, vesselMap);
    loadBerths(token, berthMap);
    loadServices(token, serviceMap);
    loadBookings(agentId, token, vesselMap, berthMap, serviceMap);

    document.getElementById("bookingForm").addEventListener("submit", async e => {
        e.preventDefault();
        const bookingId = document.getElementById("bookingId").value;
        const vesselId = Number(document.getElementById("vesselId").value);
        const berthId = Number(document.getElementById("berthId").value);
        const serviceIds = Array.from(document.getElementById("serviceIds").selectedOptions).map(opt => Number(opt.value));
        const purpose = document.getElementById("purpose").value;
        if (!vesselId || !berthId || !purpose) { alert("Please select Vessel, Berth, and Purpose!"); return; }

        const bookingData = {
            vesselId, berthId, agentId, serviceIds, purpose,
            bookingDate: document.getElementById("bookingDate").value,
            endDate: document.getElementById("endDate").value
        };

        const url = bookingId ? `http://localhost:8080/api/bookings/${bookingId}` : `http://localhost:8080/api/bookings`;
        const method = bookingId ? "PUT" : "POST";

        try {
            const res = await fetch(url, { 
                method, 
                headers: { "Authorization": "Bearer " + token, "Content-Type": "application/json" }, 
                body: JSON.stringify(bookingData) 
            });
            if (!res.ok) throw new Error(`Server error ${res.status}`);
            alert("✅ Booking saved successfully");
            document.getElementById("bookingForm").reset();
            loadBookings(agentId, token, vesselMap, berthMap, serviceMap);
        } catch (err) { console.error("❌ Error saving booking:", err); alert("Failed to save booking."); }
    });
});

function toggleTheme() {
    const newTheme = document.body.getAttribute('data-bs-theme') === 'light' ? 'dark' : 'light';
    setTheme(newTheme);
}

function setTheme(theme) {
    document.body.setAttribute('data-bs-theme', theme);
    document.getElementById('theme-icon').className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
    localStorage.setItem('theme', theme);
}

const currentPage = window.location.pathname.split("/").pop();
document.querySelectorAll('.nav-link').forEach(link => link.classList.toggle('active', link.getAttribute('href') === currentPage));

function loadVessels(agentId, token, vesselMap) {
    fetch(`http://localhost:8080/api/vessels/agent/${agentId}`, { headers: { "Authorization": "Bearer " + token } })
    .then(res => res.json())
    .then(vessels => {
        const sel = document.getElementById("vesselId");
        sel.innerHTML = `<option value="">Select Vessel</option>`;
        vesselMap.clear();
        vessels.forEach(v => { vesselMap.set(v.vesselId, v.name); sel.innerHTML += `<option value="${v.vesselId}">${v.name}</option>`; });
    }).catch(err => console.error(err));
}

function loadBerths(token, berthMap) {
    fetch("http://localhost:8080/api/berths", { headers: { "Authorization": "Bearer " + token } })
    .then(res => res.json())
    .then(berths => {
        const sel = document.getElementById("berthId");
        sel.innerHTML = `<option value="">Select Berth</option>`;
        berthMap.clear();
        berths.forEach(b => { berthMap.set(b.berthId, { number: b.berthNumber, price: Number(b.price) }); sel.innerHTML += `<option value="${b.berthId}">Berth ${b.berthNumber} - ${b.status} (Cap: ${b.capacity}, $${b.price})</option>`; });
    }).catch(err => console.error(err));
}

function loadServices(token, serviceMap) {
    fetch("http://localhost:8080/api/services", { headers: { "Authorization": "Bearer " + token } })
    .then(res => res.json())
    .then(services => {
        if (services.data) services = services.data;
        const sel = document.getElementById("serviceIds");
        sel.innerHTML = "";
        serviceMap.clear();
        services.forEach(s => { serviceMap.set(s.serviceId, s); sel.innerHTML += `<option value="${s.serviceId}">${s.name} - $${s.price}</option>`; });
    }).catch(err => console.error(err));
}


function loadBookings(agentId, token, vesselMap, berthMap, serviceMap) {
    fetch(`http://localhost:8080/api/bookings/agent/${agentId}`, { headers: { "Authorization": "Bearer " + token } })
    .then(res => res.json())
    .then(bookings => {
        const tbody = document.querySelector("#bookingsTable tbody");
        tbody.innerHTML = "";

        bookings.forEach((b, i) => {
            const vesselName = vesselMap.get(b.vesselId) || b.vesselId;
            const berthData = berthMap.get(b.berthId) || {};
            const serviceNames = (b.serviceIds || []).map(id => serviceMap.get(id)?.name || id).join(", ");

            let actionButtons = '';
            if (b.status === "APPROVED") {
                actionButtons = `<button class="btn btn-sm btn-success" onclick="payBooking(${b.bookingId})">Pay Now</button>`;
            } else if (b.status === "CONFIRMED") {
                actionButtons = `
                    <button class="btn btn-sm btn-warning" disabled>Edit</button>
                    <button class="btn btn-sm btn-danger" disabled>Delete</button>
                `;
            } else if (b.status === "COMPLETED") {
                actionButtons = `<span class="badge bg-danger fw-bold">Paid</span>`;
            } else {
                actionButtons = `
                    <button class="btn btn-sm btn-warning" onclick="editBooking(${b.bookingId})">Edit</button>
                    <button class="btn btn-sm btn-danger" onclick="deleteBooking(${b.bookingId})">Delete</button>
                `;
            }

            // Optional: add color for status
            let statusLabel = '';
            switch(b.status) {
                case "PENDING": statusLabel = `<span class="badge bg-secondary">PENDING</span>`; break;
                case "APPROVED": statusLabel = `<span class="badge bg-primary">APPROVED</span>`; break;
                case "CONFIRMED": statusLabel = `<span class="badge bg-success">CONFIRMED</span>`; break;
                case "COMPLETED": statusLabel = `<span class="badge bg-danger">COMPLETED</span>`; break;
                default: statusLabel = b.status;
            }

            tbody.innerHTML += `<tr>
                <td>${i+1}</td>
                <td>${vesselName}</td>
                <td>${berthData.number || b.berthId}</td>
                <td>${serviceNames}</td>
                <td>${b.purpose || ''}</td>
                <td>${b.bookingDate || ""}</td>
                <td>${b.endDate || ""}</td>
                <td>${b.totalPrice || 0}</td>
                <td>${statusLabel}</td>
                <td class="text-center">${actionButtons}</td>
            </tr>`;
        });
    }).catch(err => console.error(err));
}

function payBooking(bookingId) {
      const row = document.querySelector(`#bookingsTable tbody tr td button[onclick="payBooking(${bookingId})"]`).closest('tr');
    const totalPrice = Number(row.cells[7].innerText); // totalPrice is in the 8th column
    const paymentAmount = (totalPrice * 0.2).toFixed(2);

    if (confirm(`Pay 20% of total price: $${paymentAmount}?`)) {
        // Call backend to mark payment (we'll implement this later)
        alert(`Payment of $${paymentAmount} for booking ID ${bookingId} is recorded.`);
    }
}

//stripe payment integration (to be implemented)

// Replace this with your Stripe publishable key
const stripe = Stripe("pk_test_51S8gBiRtg8p4PLbs2XzTqEoEplHgHaLpigo2B5g6zZcW6G9BkIWyObTH0VMYJR1mkkjklAAiQfP4fzohdwh4UhHB00wooHGPor"); 
// Create Stripe elements
const elements = stripe.elements();
const card = elements.create("card", { hidePostalCode: true });
card.mount("#card-element");

// Show error messages
card.addEventListener("change", function(event) {
    const displayError = document.getElementById("card-errors");
    displayError.textContent = event.error ? event.error.message : "";
});


function payBooking(bookingId) {
    const row = document.querySelector(`#bookingsTable tbody tr td button[onclick="payBooking(${bookingId})"]`).closest('tr');
    const totalPrice = Number(row.cells[7].innerText);
    const paymentAmount = (totalPrice * 0.2).toFixed(2);

    document.getElementById("paymentBookingId").value = bookingId;

    // Show modal
    const stripeModal = new bootstrap.Modal(document.getElementById("stripePaymentModal"));
    stripeModal.show();
}
document.getElementById("stripePaymentForm").addEventListener("submit", async function(e) {
    e.preventDefault();
    const bookingId = Number(document.getElementById("paymentBookingId").value);
    const row = document.querySelector(`#bookingsTable tbody tr td button[onclick="payBooking(${bookingId})"]`).closest('tr');
    const totalPrice = Number(row.cells[7].innerText);
    const paymentAmount = (totalPrice * 0.2).toFixed(2);

    try {
        const token = localStorage.getItem("token");

        // Call backend to save payment
        const res = await fetch("http://localhost:8080/api/payments", {
            method: "POST",
            headers: { "Content-Type": "application/json", "Authorization": "Bearer " + token },
            body: JSON.stringify({
                bookingId,
                amount: Number(paymentAmount),
                method: "CARD"
            })
        });

        if (!res.ok) throw new Error("Payment failed");

        alert(`✅ Payment of $${paymentAmount} successful! Invoice sent via email.`);

        // Close modal
        const stripeModalEl = document.getElementById("stripePaymentModal");
        const stripeModal = bootstrap.Modal.getInstance(stripeModalEl);
        stripeModal.hide();

        // Reload bookings table to reflect CONFIRMED
        const agentId = Number(localStorage.getItem("agentId"));
        const vesselMap = new Map(), berthMap = new Map(), serviceMap = new Map();
        loadBookings(agentId, token, vesselMap, berthMap, serviceMap);

    } catch (err) {
        console.error(err);
        document.getElementById("card-errors").textContent = "Payment failed. Try again.";
    }
});