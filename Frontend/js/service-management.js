 //crud operation for service management
  // ======= Service Management Script =======
const serviceForm = document.getElementById('serviceForm');
const servicesTableBody = document.getElementById('servicesTableBody');
const apiBase = 'http://localhost:8080/api/services';

// Token from localStorage
const token = localStorage.getItem('token');
const fetchOptions = {
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    }
};

// ===== Fetch All Services =====
async function fetchServices() {
    try {
        const res = await fetch(apiBase, fetchOptions);
        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
        const { data } = await res.json();
        renderTable(data);
    } catch (err) {
        console.error('Error fetching services:', err);
        servicesTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">Failed to load services</td></tr>`;
    }
}

// ===== Render Table =====
function renderTable(services) {
    servicesTableBody.innerHTML = '';
    if (!services.length) {
        servicesTableBody.innerHTML = `<tr><td colspan="5" class="text-center text-muted">No services available</td></tr>`;
        return;
    }

    services.forEach(service => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${service.serviceId}</td>
            <td>${service.name}</td>
            <td>${service.description}</td>
            <td>${service.price?.toFixed(2) || '0.00'}</td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="editService(${service.serviceId})">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteService(${service.serviceId})">Delete</button>
            </td>
        `;
        servicesTableBody.appendChild(row);
    });
}

// ===== Add / Update Service =====
serviceForm.addEventListener('submit', async (e) => {
    e.preventDefault();

    const serviceId = document.getElementById('serviceId').value;
    const serviceDTO = {
        name: document.getElementById('serviceName').value,
        description: document.getElementById('serviceDescription').value,
        price: parseFloat(document.getElementById('servicePrice').value)
    };

    try {
        let method = 'POST';
        let url = apiBase;

        if (serviceId) {
            method = 'PUT';
            url = `${apiBase}/${serviceId}`;
        }

        const res = await fetch(url, {
            method: method,
            headers: fetchOptions.headers,
            body: JSON.stringify(serviceDTO)
        });

        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);

        // Reset form
        serviceForm.reset();
        document.getElementById('serviceId').value = '';
        fetchServices();

    } catch (err) {
        console.error('Error saving service:', err);
        alert('Failed to save service: ' + err.message);
    }
});

// ===== Edit Service =====
async function editService(id) {
    try {
        const res = await fetch(`${apiBase}/${id}`, fetchOptions);
        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
        const { data } = await res.json();

        // Populate form with existing data
        document.getElementById('serviceId').value = data.serviceId; // ✅ Correct property
        document.getElementById('serviceName').value = data.name;
        document.getElementById('serviceDescription').value = data.description;
        document.getElementById('servicePrice').value = data.price;

        // Scroll form into view
        serviceForm.scrollIntoView({ behavior: 'smooth' });

    } catch (err) {
        console.error('Error fetching service:', err);
        alert('Failed to fetch service: ' + err.message);
    }
}

// ===== Delete Service =====
async function deleteService(id) {
    if (!confirm('Are you sure you want to delete this service?')) return;

    try {
        const res = await fetch(`${apiBase}/${id}`, {
            method: 'DELETE',
            headers: fetchOptions.headers
        });

        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
        fetchServices();

    } catch (err) {
        console.error('Error deleting service:', err);
        alert('Failed to delete service: ' + err.message);
    }
}

// ===== Filters =====
const searchNameInput = document.getElementById('filterName');
const minPriceInput = document.getElementById('filterMinPrice');
const maxPriceInput = document.getElementById('filterMaxPrice');
const clearFiltersBtn = document.getElementById('clearFilters');

async function applyFilters() {
    try {
        let services = [];

        const name = searchNameInput.value.trim();
        const minPrice = parseFloat(minPriceInput.value);
        const maxPrice = parseFloat(maxPriceInput.value);

        // Fetch filtered by name from backend if name exists
        if (name) {
            const res = await fetch(`${apiBase}/search?name=${encodeURIComponent(name)}`, fetchOptions);
            if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
            const { data } = await res.json();
            services = data;
        } else {
            const res = await fetch(apiBase, fetchOptions);
            if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
            const { data } = await res.json();
            services = data;
        }

        // Apply price filters client-side
        if (!isNaN(minPrice)) services = services.filter(s => s.price >= minPrice);
        if (!isNaN(maxPrice)) services = services.filter(s => s.price <= maxPrice);

        renderTable(services);

    } catch (err) {
        console.error('Error applying filters:', err);
        alert('Failed to apply filters: ' + err.message);
    }
}

searchNameInput.addEventListener('input', applyFilters);
minPriceInput.addEventListener('input', applyFilters);
maxPriceInput.addEventListener('input', applyFilters);

clearFiltersBtn.addEventListener('click', () => {
    searchNameInput.value = '';
    minPriceInput.value = '';
    maxPriceInput.value = '';
    fetchServices();
});

// ===== Initial Load =====
fetchServices();


        ///////////
        // Sidebar Toggle Functionality
        document.addEventListener('DOMContentLoaded', function() {
            const sidebarToggle = document.getElementById('sidebarToggle');
            const sidebar = document.getElementById('sidebar');
            const mainContent = document.getElementById('mainContent');

            sidebarToggle.addEventListener('click', function() {
                if (window.innerWidth > 1200) {
                    // Desktop: Collapse sidebar
                    sidebar.classList.toggle('collapsed');
                    mainContent.classList.toggle('expanded');
                } else {
                    // Mobile: Show/hide sidebar
                    sidebar.classList.toggle('show');
                }
            });

            // Handle window resize
            window.addEventListener('resize', function() {
                if (window.innerWidth > 1200) {
                    sidebar.classList.remove('show');
                } else {
                    sidebar.classList.remove('collapsed');
                    mainContent.classList.remove('expanded');
                }
            });

            // Navigation Link Active State
            const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
            navLinks.forEach(link => {
                link.addEventListener('click', function(e) {
                    e.preventDefault();
                    
                    // Remove active class from all links
                    navLinks.forEach(l => l.classList.remove('active'));
                    
                    // Add active class to clicked link
                    this.classList.add('active');
                    
                    // You can add navigation logic here
                    const section = this.dataset.section;
                    console.log('Navigating to:', section);
                    
                    // Example: You could redirect to different pages
                    // window.location.href = section + '.html';
                });
            });

            // Notification Badge Animation
            const notificationBell = document.querySelector('.notification-bell');
            if (notificationBell) {
                notificationBell.addEventListener('click', function() {
                    const badge = this.querySelector('.notification-badge');
                    if (badge) {
                        badge.style.animation = 'pulse 0.6s ease-in-out';
                        setTimeout(() => {
                            badge.style.animation = '';
                        }, 600);
                    }
                });
            }

            // Close sidebar when clicking outside on mobile
            document.addEventListener('click', function(e) {
                if (window.innerWidth <= 1200) {
                    if (!sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                        sidebar.classList.remove('show');
                    }
                }
            });
        });

        // Export navigation functions for use in other pages
        window.MarineXNav = {
            setActiveNavigation: function(sectionName) {
                const navLinks = document.querySelectorAll('.sidebar-nav .nav-link');
                navLinks.forEach(link => {
                    link.classList.remove('active');
                    if (link.dataset.section === sectionName) {
                        link.classList.add('active');
                    }
                });
            },
            
            updateNotificationCount: function(count) {
                const badge = document.querySelector('.notification-badge');
                if (badge) {
                    badge.textContent = count;
                    if (count === 0) {
                        badge.style.display = 'none';
                    } else {
                        badge.style.display = 'flex';
                    }
                }
            },
            
            toggleSidebar: function() {
                const sidebar = document.getElementById('sidebar');
                const mainContent = document.getElementById('mainContent');
                
                if (window.innerWidth > 1200) {
                    sidebar.classList.toggle('collapsed');
                    mainContent.classList.toggle('expanded');
                } else {
                    sidebar.classList.toggle('show');
                }
            }
        };

              //=========nav bar =======

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

    // Map data-section to actual HTML files
    const sectionToFile = {
        "dashboard": "admin-dashboard.html",
        "agent-management": "agent-management.html",
        "berth-management": "berth-management.html",
        "booking-management": "admin-booking.html",
        "worker-management": "workers-management.html",
        "view-schedule": "Schedule.html",
        "service-management": "service-management.html"
    };

    // Sidebar nav link clicks
    document.querySelectorAll('.sidebar-nav .nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();

            // Remove active class from all links
            document.querySelectorAll('.sidebar-nav .nav-link').forEach(l => l.classList.remove('active'));

            // Add active class to clicked link
            link.classList.add('active');

            // Redirect to the proper page
            const section = link.dataset.section;
            const file = sectionToFile[section];
            if (file) {
                window.location.href = `/Frontend/pages/admin/${file}`;
            }
        });
    });

    // Close sidebar when clicking outside on mobile
    document.addEventListener('click', function(e) {
        if (window.innerWidth <= 1200) {
            if (!sidebar.contains(e.target) && !sidebarToggle.contains(e.target)) {
                sidebar.classList.remove('show');
            }
        }
    });

    // Handle window resize
    window.addEventListener('resize', function() {
        if (window.innerWidth > 1200) {
            sidebar.classList.remove('show');
        } else {
            sidebar.classList.remove('collapsed');
            mainContent.classList.remove('expanded');
        }
    });
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

