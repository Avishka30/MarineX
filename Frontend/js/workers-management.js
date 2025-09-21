
document.addEventListener('DOMContentLoaded', () => {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');

    sidebarToggle.addEventListener('click', () => {
        if(window.innerWidth>1200){ sidebar.classList.toggle('collapsed'); mainContent.classList.toggle('expanded'); }
        else sidebar.classList.toggle('show');
    });

    document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
        link.addEventListener('click', e => {
            e.preventDefault();
            document.querySelectorAll('.sidebar-nav .nav-link').forEach(l=>l.classList.remove('active'));
            link.classList.add('active');
            const sectionToFile = {
                "dashboard":"admin-dashboard.html","agent-management":"agent-management.html",
                "berth-management":"berth-management.html","booking-management":"booking-management.html",
                "worker-management":"worker-management.html","view-schedule":"view-schedule.html",
                "payment-details":"payment-details.html"
            };
            window.location.href = `/Frontend/pages/admin/${sectionToFile[link.dataset.section]}`;
        });
    });

    document.addEventListener('click', e => {
        if(window.innerWidth<=1200 && !sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) sidebar.classList.remove('show');
    });
    window.addEventListener('resize', ()=>{ if(window.innerWidth>1200){ sidebar.classList.remove('show'); } else { sidebar.classList.remove('collapsed'); mainContent.classList.remove('expanded'); } });
});


// CRUD Operations
const apiBase = "http://localhost:8080/api/workers";
const token = localStorage.getItem("token");
const headers = { "Authorization": `Bearer ${token}`, "Content-Type": "application/json" };

async function fetchWorkers() {
    try {
        const res = await fetch(apiBase, { headers });
        if(!res.ok) throw new Error(res.statusText);
        const data = await res.json();
        renderTable(data);
    } catch(err) { alert("Error fetching workers: "+err.message); }
}

function renderTable(workers){
    const tbody = document.getElementById("workersTableBody");
    tbody.innerHTML="";
    workers.forEach(w=>{
        tbody.innerHTML+=`<tr>
            <td>${w.workerId}</td>
            <td>${w.fullName}</td>
            <td>${w.email}</td>
            <td>${w.phone}</td>
            <td>${w.specialization}</td>
            <td>${w.status}</td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="editWorker(${w.workerId})">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteWorker(${w.workerId})">Delete</button>
            </td>
        </tr>`;
    });
}

// Add / Update Worker
document.getElementById("workerForm").addEventListener("submit", async e=>{
    e.preventDefault();
    const workerId=document.getElementById("workerId").value;
    const dto={
        fullName:document.getElementById("fullName").value,
        email:document.getElementById("email").value,
        phone:document.getElementById("phone").value,
        specialization:document.getElementById("specialization").value,
        status:document.getElementById("status").value
    };
    await fetch(workerId?`${apiBase}/${workerId}`:apiBase,{
        method:workerId?"PUT":"POST", headers, body:JSON.stringify(dto)
    });
    e.target.reset(); document.getElementById("workerId").value="";
    fetchWorkers();
});

// Edit Worker
async function editWorker(id){
    const res = await fetch(`${apiBase}/${id}`, { headers });
    const w = await res.json();
    document.getElementById("workerId").value = w.workerId;
    document.getElementById("fullName").value = w.fullName;
    document.getElementById("email").value = w.email;
    document.getElementById("phone").value = w.phone;
    document.getElementById("specialization").value = w.specialization;
    document.getElementById("status").value = w.status;
}

// Delete Worker
async function deleteWorker(id){
    if(!confirm("Delete this worker?")) return;
    await fetch(`${apiBase}/${id}`, { method:"DELETE", headers });
    fetchWorkers();
}

// Search
document.getElementById("searchInput").addEventListener("keyup", async e=>{
    const q=e.target.value.trim();
    if(!q){ fetchWorkers(); return; }
    const res = await fetch(`${apiBase}/search?query=${q}`, { headers });
    const data = await res.json();
    renderTable(data);
});

// Initial Load
fetchWorkers();

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

// ===== MarineX Navbar & Sidebar Script =====
document.addEventListener('DOMContentLoaded', () => {
    const sidebarToggle = document.getElementById('sidebarToggle');
    const sidebar = document.getElementById('sidebar');
    const mainContent = document.getElementById('mainContent');

    // Sidebar toggle
    sidebarToggle.addEventListener('click', () => {
        if (window.innerWidth > 1200) {
            sidebar.classList.toggle('collapsed');
            mainContent.classList.toggle('expanded');
        } else {
            sidebar.classList.toggle('show');
        }
    });

    // Map section names to actual HTML files
    const sectionToFile = {
        "dashboard": "admin-dashboard.html",
        "agent-management": "agent-management.html",
        "berth-management": "berth-management.html",
        "service-management": "service-management.html",
        "booking-management": "admin-booking.html",
        "worker-management": "workers-management.html",
        "view-schedule": "Schedule.html",
        "payment-details": "admin-payment.html"
    };

    // Sidebar navigation
    const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
    navLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();

            // Remove active class
            navLinks.forEach(l => l.classList.remove('active'));
            this.classList.add('active');

            // Navigate to file
            const section = this.dataset.section;
            if (sectionToFile[section]) {
                window.location.href = `/Frontend/pages/admin/${sectionToFile[section]}`;
            }
        });
    });

    // Close sidebar on outside click (mobile)
    document.addEventListener('click', (e) => {
        if (window.innerWidth <= 1200 &&
            !sidebar.contains(e.target) &&
            !sidebarToggle.contains(e.target)) {
            sidebar.classList.remove('show');
        }
    });

    // ===== Expose utility functions globally =====
    window.MarineXNav = {
        setActiveNavigation(sectionName) {
            navLinks.forEach(link => {
                link.classList.remove('active');
                if (link.dataset.section === sectionName) {
                    link.classList.add('active');
                }
            });
        },

        updateNotificationCount(count) {
            const badge = document.querySelector('.notification-badge');
            if (badge) {
                badge.textContent = count;
                badge.style.display = count > 0 ? 'flex' : 'none';
            }
        },

        toggleSidebar() {
            if (window.innerWidth > 1200) {
                sidebar.classList.toggle('collapsed');
                mainContent.classList.toggle('expanded');
            } else {
                sidebar.classList.toggle('show');
            }
        }
    };
});

