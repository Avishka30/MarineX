  //fetch code 
     const agentContainer = document.getElementById('agentCardsContainer');
const token = localStorage.getItem('token'); // JWT token

// Fetch all pending agents
async function fetchPendingAgents() {
    try {
        const response = await fetch('http://localhost:8080/api/admin/agents/pending', {
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        const result = await response.json();
        console.log('Fetched result:', result);

        if (result.success && result.data.length > 0) {
            renderAgents(result.data);
        } else {
            agentContainer.innerHTML = '<p class="text-muted">No pending agents found.</p>';
        }
    } catch (error) {
        console.error('Error fetching agents:', error);
        agentContainer.innerHTML = '<p class="text-danger">Failed to load agents.</p>';
    }
}

// Render agent cards dynamically
function renderAgents(agents) {
    agentContainer.innerHTML = ''; // clear previous content

    agents.forEach(agent => {
        const card = document.createElement('div');
        card.className = 'col-md-4 mb-4';
        card.innerHTML = `
            <div class="card shadow-sm">
                <div class="card-body text-center">
                    <img src="${agent.profileImageUrl || 'https://via.placeholder.com/80'}" class="rounded-circle mb-3" width="80" height="80" alt="Profile">
                    <h5 class="card-title">${agent.fullName}</h5>
                    <p class="card-text text-muted">${agent.email}</p>
                    <p class="card-text"><strong>Reg. Number:</strong> ${agent.licenseCode || '-'}</p>
                    <div class="d-flex justify-content-center gap-2">
                        <button class="btn btn-success btn-approve" data-id="${agent.userId}">Approve</button>
                        <button class="btn btn-danger btn-reject" data-id="${agent.userId}">Reject</button>
                    </div>
                </div>
            </div>
        `;
        agentContainer.appendChild(card);
    });

    // Attach button event listeners
    document.querySelectorAll('.btn-approve').forEach(btn => {
        btn.addEventListener('click', () => handleAgentAction(btn.dataset.id, 'approve'));
    });
    document.querySelectorAll('.btn-reject').forEach(btn => {
        btn.addEventListener('click', () => handleAgentAction(btn.dataset.id, 'reject'));
    });
}

// Approve or reject agent
async function handleAgentAction(agentId, action) {
    try {
        const response = await fetch(`http://localhost:8080/api/admin/agents/${agentId}/${action}`, {
            method: 'PUT',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        const result = await response.json();
        if (result.success) {
            alert(`${action === 'approve' ? 'Approved' : 'Rejected'} successfully!`);
            fetchPendingAgents(); // refresh list
        } else {
            alert(`Failed to ${action}: ${result.message}`);
        }
    } catch (error) {
        console.error(`Error on ${action}:`, error);
        alert(`Failed to ${action} agent.`);
    }
}

// Load pending agents on page load
document.addEventListener('DOMContentLoaded', fetchPendingAgents);
        //======

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