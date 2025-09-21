// ================= Sidebar & Navigation =================
document.addEventListener('DOMContentLoaded', () => {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');

    const sectionToFile = {
        "dashboard": "admin-dashboard.html",
        "agent-management": "agent-management.html",
        "berth-management": "berth-management.html",
        "booking-management": "booking-management.html",
        "worker-management": "worker-management.html",
        "view-schedule": "Schedule.html",
        "payment-details": "payment-details.html"
    };

    // Sidebar toggle
    sidebarToggle.addEventListener('click', () => {
        if (window.innerWidth > 1200) {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        } else {
            sidebar.classList.toggle('show');
        }
    });

    // Sidebar nav link clicks
    document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            document.querySelectorAll('.sidebar-nav .nav-link').forEach(l => l.classList.remove('active'));
            link.classList.add('active');

            const section = link.dataset.section;
            const file = sectionToFile[section];
            if (file) window.location.href = `/Frontend/pages/admin/${file}`;
        });
    });

    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', (e) => {
        if (window.innerWidth <= 1200 && !sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
            sidebar.classList.remove('show');
        }
    });

    // Handle window resize
    window.addEventListener('resize', () => {
        if (window.innerWidth > 1200) {
            sidebar.classList.remove('show');
        } else {
            sidebar.classList.remove('collapsed');
            mainContent.classList.remove('expanded');
        }
    });
});
// ======= Fixed JS (paste into your <script> block) =======

/* --- Configuration --- */
const API_BOOKINGS = "http://localhost:8080/api/bookings";
const API_VESSELS = "http://localhost:8080/api/vessels";
const API_BERTHS = "http://localhost:8080/api/berths";
const API_SERVICES = "http://localhost:8080/api/services";
const API_WORKERS = "http://localhost:8080/api/workers";
const API_ASSIGNMENTS = "http://localhost:8080/api/worker-assignments"; // adjust if your controller path differs

/* --- Auth helper --- */
function getAuthHeaders() {
  const token = localStorage.getItem("token");
  if (!token) {
    // Force login if token missing
    alert("You must log in first!");
    window.location.href = "login.html";
    throw new Error("No auth token");
  }
  return {
    "Authorization": `Bearer ${token}`,
    "Content-Type": "application/json"
  };
}

/* --- Data maps & lists --- */
const vesselMap = new Map();
const berthMap = new Map();
const serviceMap = new Map();
let workersList = []; // array of WorkerDTOs

/* --- Load reference data --- */
async function loadVessels() {
  try {
    const res = await fetch(API_VESSELS, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Vessels fetch failed: ${res.status}`);
    const data = await res.json();
    vesselMap.clear();
    (Array.isArray(data) ? data : (data.data || [])).forEach(v => vesselMap.set(v.vesselId ?? v.id, v.name ?? v.fullName ?? v.vesselName));
  } catch (err) {
    console.error("loadVessels:", err);
  }
}

async function loadBerths() {
  try {
    const res = await fetch(API_BERTHS, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Berths fetch failed: ${res.status}`);
    const data = await res.json();
    berthMap.clear();
    (Array.isArray(data) ? data : (data.data || [])).forEach(b => berthMap.set(b.berthId ?? b.id, { number: b.berthNumber, price: Number(b.price || 0) }));
  } catch (err) {
    console.error("loadBerths:", err);
  }
}

async function loadServices() {
  try {
    const res = await fetch(API_SERVICES, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Services fetch failed: ${res.status}`);
    let data = await res.json();
    if (data && data.data && Array.isArray(data.data)) data = data.data;
    (Array.isArray(data) ? data : []).forEach(s => serviceMap.set(s.serviceId ?? s.id, s.name));
  } catch (err) {
    console.error("loadServices:", err);
  }
}

/* --- Load workers --- */
async function loadWorkers() {
  try {
    const res = await fetch(API_WORKERS, { headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Workers fetch failed: ${res.status}`);
    let data = await res.json();
    // backend might wrap list in { data: [...] }
    if (data && data.data && Array.isArray(data.data)) data = data.data;
    if (!Array.isArray(data)) {
      console.warn("Unexpected workers response shape:", data);
      workersList = [];
      return;
    }
    workersList = data;
    console.log("Workers loaded:", workersList);
    // If the assign-worker modal select exists and is visible/populated earlier, update it too
    populateWorkerSelect(document.getElementById('workerSelect'));
  } catch (err) {
    console.error("loadWorkers:", err);
    workersList = [];
  }
}

/* --- Utility to populate worker <select> --- */
function populateWorkerSelect(selectEl) {
  if (!selectEl) return;
  selectEl.innerHTML = ""; // clear
  if (!workersList || workersList.length === 0) {
    const opt = document.createElement("option");
    opt.value = "";
    opt.textContent = "No workers available";
    selectEl.appendChild(opt);
    return;
  }

  workersList.forEach(w => {
    const opt = document.createElement("option");
    opt.value = w.workerId ?? "";
    // Show "FullName (Specialization)"
    const workerName = w.fullName ?? `Worker ${opt.value}`;
    const specialization = w.specialization ?? "";
    opt.textContent = specialization ? `${workerName} (${specialization})` : workerName;
    selectEl.appendChild(opt);
  });
}

/* --- Load bookings and render table --- */
async function loadBookings() {
  try {
    // Ensure reference data loaded so we can show names
    await Promise.all([loadVessels(), loadBerths(), loadServices()]);
    const res = await fetch(API_BOOKINGS, { method: "GET", headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Bookings fetch failed: ${res.status}`);
    const bookings = await res.json();
    renderBookings(Array.isArray(bookings) ? bookings : (bookings.data || []));
  } catch (err) {
    console.error("loadBookings:", err);
    alert("Failed to load bookings");
  }
}

function renderBookings(bookings) {
  const tbody = document.getElementById("bookingsTableBody");
  if (!tbody) return;
  tbody.innerHTML = "";

  bookings.forEach((b, i) => {
    const vesselName = vesselMap.get(b.vesselId) || b.vesselName || b.vesselId || "-";
    const berthInfo = berthMap.get(b.berthId);
    const berthName = berthInfo ? `Berth ${berthInfo.number}` : (b.berthNumber || b.berthId || "-");
    const serviceNames = (b.serviceIds && b.serviceIds.length)
      ? b.serviceIds.map(id => serviceMap.get(id) || id).join(", ")
      : (b.services ? (b.services.map(s => s.name || s.serviceName).join(", ")) : "-");
    const totalPrice = b.totalPrice ?? (berthInfo ? berthInfo.price + (b.services?.reduce((s, it) => s + (it.price || 0), 0) || 0) : 0);

    // Status label
    const statusLabel = `<span class="badge ${
      b.status === "CONFIRMED" || b.status === "CONFIRM" ? "bg-success" :
      b.status === "PENDING" ? "bg-warning text-dark" :
      b.status === "COMPLETED" ? "bg-primary" : "bg-secondary"
    }">${b.status}</span>`;

    // Buttons: show ONLY Approve for PENDING, ONLY Complete for CONFIRMED
    let actionButtons = "";
    if (String(b.status).toUpperCase() === "PENDING") {
      actionButtons = `<button class="btn btn-sm btn-success me-1" onclick="approveBooking(${b.bookingId})"><i class="fas fa-check-circle me-1"></i> Approve</button>`;
    } else if (String(b.status).toUpperCase() === "CONFIRMED" || String(b.status).toUpperCase() === "CONFIRM") {
      actionButtons = `<button class="btn btn-sm btn-primary" onclick="completeBooking(${b.bookingId})"><i class="fas fa-flag-checkered me-1"></i> Complete</button>`;
    }

    const assignWorkerBtn = `<button class="btn btn-sm btn-info" onclick="assignWorkers(${b.bookingId})"><i class="fas fa-hard-hat me-1"></i> Assign</button>`;


    tbody.insertAdjacentHTML("beforeend", `
      <tr>
        <td>${i + 1}</td>
        <td>${escapeHtml(vesselName)}</td>
        <td>${escapeHtml(berthName)}</td>
        <td>${escapeHtml(serviceNames)}</td>
        <td>${escapeHtml(b.purpose || "")}</td>
        <td>${escapeHtml(b.bookingDate || "")}</td>
        <td>${escapeHtml(b.endDate || "")}</td>
        <td>${escapeHtml(totalPrice)}</td>
        <td>${statusLabel}</td>
        <td class="text-center">${assignWorkerBtn}</td>
        <td class="text-center">${actionButtons}</td>
      </tr>
    `);
  });
}

/* very small helper to avoid injecting raw HTML from data */
function escapeHtml(v) {
  if (v == null) return "";
  return String(v)
    .replaceAll("&", "&amp;")
    .replaceAll("<", "&lt;")
    .replaceAll(">", "&gt;")
    .replaceAll('"', "&quot;")
    .replaceAll("'", "&#039;");
}

/* --- Booking action helpers --- */
async function approveBooking(id) {
  if (!confirm("Approve this booking?")) return;
  try {
    const res = await fetch(`${API_BOOKINGS}/approve/${id}`, { method: "PUT", headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Approve failed: ${res.status}`);
    alert("Booking approved!");
    await loadBookings();
  } catch (err) {
    console.error("approveBooking:", err);
    alert("Could not approve booking");
  }
}

async function completeBooking(id) {
  if (!confirm("Mark this booking as completed?")) return;
  try {
    const res = await fetch(`${API_BOOKINGS}/complete/${id}`, { method: "PUT", headers: getAuthHeaders() });
    if (!res.ok) throw new Error(`Complete failed: ${res.status}`);
    alert("Booking completed!");
    await loadBookings();
  } catch (err) {
    console.error("completeBooking:", err);
    alert("Could not complete booking");
  }
}

/* --- Assign worker: open modal, populate select, submit --- */
function openAssignModal(bookingId) {
  const modalEl = document.getElementById('assignWorkerModal');
  if (!modalEl) {
    alert("Assign modal element not found in DOM.");
    return;
  }
  const modal = new bootstrap.Modal(modalEl);

  // populate booking id hidden
  document.getElementById('modalBookingId').value = bookingId;

  // ensure workers loaded (if not, load now)
  if (!workersList || workersList.length === 0) {
    loadWorkers().then(() => {
      populateWorkerSelect(document.getElementById('workerSelect'));
    });
  } else {
    populateWorkerSelect(document.getElementById('workerSelect'));
  }

  // default date to today
  const assignedDateInput = document.getElementById('assignedDate');
  if (assignedDateInput) assignedDateInput.value = new Date().toISOString().slice(0, 10);

  modal.show();
}

// wrapper called from button in table
function assignWorkers(bookingId) {
  // If your backend expects bookingServiceId (not bookingId), you must map bookingId => bookingServiceId here.
  // For now we pass bookingId as bookingServiceId (adjust if necessary)
  openAssignModal(bookingId);
}

// handle assignment form submission
document.addEventListener("submit", async (evt) => {
  const form = evt.target;
  if (form && form.id === "assignWorkerForm") {
    evt.preventDefault();

    const bookingId = document.getElementById('modalBookingId').value;
    const workerId = document.getElementById('workerSelect').value;
    const assignedDate = document.getElementById('assignedDate').value;

    if (!workerId) {
      alert("Please select a worker");
      return;
    }

    // payload: backend expects booking_service_id (FK) — adapt if needed
    const payload = {
      bookingServiceId: Number(bookingId), // <-- change this mapping if you have a BookingService entity to reference
      workerId: Number(workerId),
      assignedDate: assignedDate || new Date().toISOString().slice(0,10)
    };

    try {
      const res = await fetch(API_ASSIGNMENTS, {
        method: "POST",
        headers: getAuthHeaders(),
        body: JSON.stringify(payload)
      });
      if (!res.ok) {
        const text = await res.text().catch(()=>"");
        throw new Error(`Assign failed: ${res.status} ${text}`);
      }
      alert("Worker assigned successfully!");
      bootstrap.Modal.getInstance(document.getElementById('assignWorkerModal')).hide();
      await loadBookings();
    } catch (err) {
      console.error("assignWorkerForm submit:", err);
      alert("Could not assign worker. See console for details.");
    }
  }
});

/* --- Initialization --- */
document.addEventListener("DOMContentLoaded", async () => {
  try {
    // load workers first (so dropdowns are ready), then other refs and bookings
    await loadWorkers();
    await Promise.all([loadVessels(), loadBerths(), loadServices()]);
    await loadBookings();
  } catch (err) {
    console.error("init error:", err);
  }
});

//logout

document.addEventListener("DOMContentLoaded", () => {
    // Select the Logout link in the user dropdown
    const logoutLink = [...document.querySelectorAll(".dropdown-item")].find(
        el => el.textContent.trim() === "Logout"
    );

    if (logoutLink) {
        logoutLink.addEventListener("click", async (e) => {
            e.preventDefault();

            try {
                const token = localStorage.getItem("token"); // get token if needed

                // Use full backend URL
                const response = await fetch("http://localhost:8080/api/auth/logout", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": token ? `Bearer ${token}` : ""
                    }
                });

                // Handle different response types
                let resData;
                const contentType = response.headers.get("content-type");
                if (contentType && contentType.includes("application/json")) {
                    resData = await response.json();
                } else {
                    resData = { message: await response.text() };
                }

                if (response.ok) {
                    // Clear storage
                    localStorage.clear();
                    sessionStorage.clear();

                    // Optional success message
                    alert(resData?.message || "Logged out successfully!");

                    // Redirect to login page (adjust path if needed)
                    window.location.href = "/Frontend/pages/login.html";
                } else {
                    alert(resData?.message || "Logout failed! Please try again.");
                }

            } catch (error) {
                console.error("Logout error:", error);
                alert("An error occurred during logout: " + error.message);
            }
        });
    }
});