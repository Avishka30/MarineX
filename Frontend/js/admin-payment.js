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
        "payment-details": "admin-payment.html"
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

        // === Payments CRUD ===
const paymentsTableBody = document.querySelector('#paymentsTable tbody');
const paymentApiBase = 'http://localhost:8080/api/payments'; // adjust if needed

const token = localStorage.getItem('token');

const fetchOptions = {
    headers: {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
    }
};

// Fetch all payments
async function fetchPayments() {
    try {
        const res = await fetch(paymentApiBase, fetchOptions);
        if (!res.ok) throw new Error(res.status + ' ' + res.statusText);
        const data = await res.json();
        renderPaymentsTable(data);
    } catch (err) {
        console.error('Error fetching payments:', err);
        alert('Failed to fetch payments: ' + err.message);
    }
}

// Fetch payment by ID
async function fetchPaymentById(id) {
    try {
        const res = await fetch(`${paymentApiBase}/${id}`, fetchOptions);
        if (!res.ok) {
            if (res.status === 404) {
                renderPaymentsTable([]); // no results
                alert('No payment found with this ID');
                return;
            }
            throw new Error(res.status + ' ' + res.statusText);
        }
        const data = await res.json();
        renderPaymentsTable([data]); // wrap single object in array
    } catch (err) {
        console.error('Error fetching payment:', err);
        alert('Failed to fetch payment: ' + err.message);
    }
}

// Render table rows
function renderPaymentsTable(payments) {
    paymentsTableBody.innerHTML = '';
    if (!payments || payments.length === 0) {
        paymentsTableBody.innerHTML = `<tr><td colspan="5" class="text-center">No payments found</td></tr>`;
        return;
    }

    payments.forEach(payment => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${payment.id}</td>
            <td>${payment.bookingId}</td>
            <td>${payment.amount.toFixed(2)}</td>
            <td>
                <span class="badge ${payment.status === 'PAID' ? 'bg-success' : 'bg-warning'}">
                    ${payment.status}
                </span>
            </td>
            <td>${new Date(payment.paymentDate).toLocaleDateString()}</td>
        `;
        paymentsTableBody.appendChild(row);
    });
}

// Handle filter form submit
document.getElementById('filterPaymentForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const paymentId = document.getElementById('paymentId').value.trim();
    if (paymentId) {
        fetchPaymentById(paymentId);
    } else {
        alert('Please enter a Payment ID');
    }
});

// Auto load all payments when page loads
document.addEventListener("DOMContentLoaded", fetchPayments);

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

