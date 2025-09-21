      //====crud====
            const berthForm = document.getElementById('berthForm');
const berthsTableBody = document.querySelector('#berthsTable tbody');
const apiBase = 'http://localhost:8080/api/berths';

// Get token from wherever you store it (e.g., localStorage)
const token = localStorage.getItem('token');

const fetchOptions = {
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    }
};

// Fetch all berths
async function fetchBerths() {
    try {
        const res = await fetch(apiBase, fetchOptions);
        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
        const data = await res.json();
        renderTable(data);
    } catch (err) {
        console.error('Error fetching berths:', err);
        alert('Failed to fetch berths: ' + err.message);
    }
}

// Render table rows
function renderTable(berths) {
    berthsTableBody.innerHTML = '';
    berths.forEach(berth => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${berth.berthId}</td>
            <td>${berth.berthNumber}</td>
            <td>${berth.capacity}</td>
            <td>${berth.status}</td>
            <td>${berth.price.toFixed(2)}</td>
            <td>
                <button class="btn btn-sm btn-info me-1" onclick="editBerth(${berth.berthId})">Edit</button>
                <button class="btn btn-sm btn-danger" onclick="deleteBerth(${berth.berthId})">Delete</button>
            </td>
        `;
        berthsTableBody.appendChild(row);
    });
}

// Add / Update Berth
berthForm.addEventListener('submit', async function(e) {
    e.preventDefault();
    const berthId = document.getElementById('berthId').value;
    const berthDTO = {
        berthNumber: document.getElementById('berthNumber').value,
        capacity: document.getElementById('berthCapacity').value,
        status: document.getElementById('berthStatus').value,
        price: parseFloat(document.getElementById('berthPrice').value)
    };

    try {
        if (berthId) {
            await fetch(`${apiBase}/${berthId}`, {
                method: 'PUT',
                headers: fetchOptions.headers,
                body: JSON.stringify(berthDTO)
            });
        } else {
            await fetch(apiBase, {
                method: 'POST',
                headers: fetchOptions.headers,
                body: JSON.stringify(berthDTO)
            });
        }
        berthForm.reset();
        document.getElementById('berthId').value = '';
        fetchBerths();
    } catch (err) {
        console.error('Error saving berth:', err);
        alert('Failed to save berth: ' + err.message);
    }
});

// Edit Berth
async function editBerth(id) {
    try {
        const res = await fetch(`${apiBase}/${id}`, fetchOptions);
        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
        const berth = await res.json();
        document.getElementById('berthId').value = berth.berthId;
        document.getElementById('berthNumber').value = berth.berthNumber;
        document.getElementById('berthCapacity').value = berth.capacity;
        document.getElementById('berthStatus').value = berth.status;
        document.getElementById('berthPrice').value = berth.price;
    } catch (err) {
        console.error('Error fetching berth:', err);
        alert('Failed to fetch berth: ' + err.message);
    }
}

// Delete Berth
async function deleteBerth(id) {
    if (!confirm('Are you sure you want to delete this berth?')) return;
    try {
        await fetch(`${apiBase}/${id}`, { method: 'DELETE', headers: fetchOptions.headers });
        fetchBerths();
    } catch (err) {
        console.error('Error deleting berth:', err);
        alert('Failed to delete berth: ' + err.message);
    }
}

// Initial load
fetchBerths();



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
        "payment-details": "admin-payment.html",
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

