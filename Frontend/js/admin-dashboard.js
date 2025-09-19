
// admin-dashboard.js

document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");

    if (!token) {
        // If no token, redirect to login page
        alert("You must log in first!");
        window.location.href = "login.html";
        return;
    }

    // Fetch admin-only endpoint
    fetch("http://localhost:8080/api/admin/test", {
        method: "GET",
        headers: {
            "Authorization": "Bearer " + token,
            "Content-Type": "application/json"
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error("Unauthorized or server error");
        }
        return response.text(); // endpoint returns plain String
    })
    .then(data => {
        console.log("✅ API Response:", data);
        // If you later want to display it:
        // document.getElementById("someElement").innerText = data;
    })
    .catch(error => {
        console.error("❌ Error:", error);
        alert("Access denied. Please log in again.");
        window.location.href = "login.html";
    });
});

// Harbor Activity Chart Animation
// Make sure Chart.js is loaded before running this code

// Sample harbor activity data
const harborData = {
    labels: ['6 AM', '9 AM', '12 PM', '3 PM', '6 PM', '9 PM', '12 AM', '3 AM'],
    datasets: [{
        label: 'Ship Arrivals',
        data: [12, 25, 18, 32, 28, 15, 8, 5],
        borderColor: '#007bff',
        backgroundColor: 'rgba(0, 123, 255, 0.1)',
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#007bff',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 8
    }, {
        label: 'Ship Departures',
        data: [8, 20, 22, 28, 35, 18, 12, 7],
        borderColor: '#28a745',
        backgroundColor: 'rgba(40, 167, 69, 0.1)',
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#28a745',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 8
    }, {
        label: 'Cargo Volume (tons)',
        data: [150, 200, 180, 250, 220, 160, 120, 90],
        borderColor: '#ffc107',
        backgroundColor: 'rgba(255, 193, 7, 0.1)',
        fill: true,
        tension: 0.4,
        pointBackgroundColor: '#ffc107',
        pointBorderColor: '#ffffff',
        pointBorderWidth: 2,
        pointRadius: 5,
        pointHoverRadius: 8,
        yAxisID: 'y1'
    }]
};

// Chart configuration with animations
const chartConfig = {
    type: 'line',
    data: harborData,
    options: {
        responsive: true,
        maintainAspectRatio: false,
        interaction: {
            mode: 'index',
            intersect: false,
        },
        plugins: {
            legend: {
                position: 'top',
                labels: {
                    usePointStyle: true,
                    padding: 20,
                    font: {
                        size: 12
                    }
                }
            },
            tooltip: {
                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                titleColor: '#ffffff',
                bodyColor: '#ffffff',
                borderColor: '#007bff',
                borderWidth: 1,
                cornerRadius: 8,
                displayColors: true,
                callbacks: {
                    afterLabel: function(context) {
                        if (context.datasetIndex === 2) {
                            return 'tons';
                        }
                        return 'vessels';
                    }
                }
            }
        },
        scales: {
            x: {
                display: true,
                title: {
                    display: true,
                    text: 'Time of Day',
                    font: {
                        size: 14,
                        weight: 'bold'
                    }
                },
                grid: {
                    color: 'rgba(0, 0, 0, 0.1)'
                }
            },
            y: {
                type: 'linear',
                display: true,
                position: 'left',
                title: {
                    display: true,
                    text: 'Number of Vessels',
                    font: {
                        size: 14,
                        weight: 'bold'
                    }
                },
                grid: {
                    color: 'rgba(0, 0, 0, 0.1)'
                }
            },
            y1: {
                type: 'linear',
                display: true,
                position: 'right',
                title: {
                    display: true,
                    text: 'Cargo Volume (tons)',
                    font: {
                        size: 14,
                        weight: 'bold'
                    }
                },
                grid: {
                    drawOnChartArea: false,
                },
            }
        },
        // Animation configuration
        animation: {
            duration: 2000,
            easing: 'easeInOutQuart',
            animateRotate: true,
            animateScale: true
        },
        animations: {
            tension: {
                duration: 1500,
                easing: 'easeInOutCubic',
                from: 1,
                to: 0.4,
                loop: false
            },
            radius: {
                duration: 1000,
                easing: 'easeInOutElastic',
                from: 0,
                to: 5
            },
            borderWidth: {
                duration: 1200,
                easing: 'easeOutBounce',
                from: 0,
                to: 2
            }
        },
        hover: {
            animationDuration: 300
        }
    }
};

// Initialize the chart
function initHarborChart() {
    const ctx = document.getElementById('activityChart');
    if (!ctx) {
        console.error('Canvas element with id "activityChart" not found');
        return;
    }

    const harborChart = new Chart(ctx, chartConfig);

    // Add periodic data updates with smooth animations
    setInterval(() => {
        updateChartData(harborChart);
    }, 10000); // Update every 10 seconds

    return harborChart;
}

// Function to update chart data with new values
function updateChartData(chart) {
    // Generate new random data to simulate real-time updates
    const newArrivals = harborData.datasets[0].data.map(val => 
        Math.max(0, val + Math.floor(Math.random() * 10 - 5))
    );
    const newDepartures = harborData.datasets[1].data.map(val => 
        Math.max(0, val + Math.floor(Math.random() * 10 - 5))
    );
    const newCargo = harborData.datasets[2].data.map(val => 
        Math.max(50, val + Math.floor(Math.random() * 50 - 25))
    );

    // Update datasets with smooth transition
    chart.data.datasets[0].data = newArrivals;
    chart.data.datasets[1].data = newDepartures;
    chart.data.datasets[2].data = newCargo;

    // Apply update animation
    chart.update('active');
    
    // Add a subtle pulse effect to the points
    addPulseEffect(chart);
}

// Add pulse effect to chart points
function addPulseEffect(chart) {
    chart.data.datasets.forEach((dataset, index) => {
        const originalRadius = dataset.pointRadius;
        
        // Animate radius increase
        setTimeout(() => {
            dataset.pointRadius = originalRadius + 2;
            chart.update('none');
            
            // Animate radius back to original
            setTimeout(() => {
                dataset.pointRadius = originalRadius;
                chart.update('none');
            }, 200);
        }, index * 100);
    });
}

// Add hover animations for the View Details button
function initButtonAnimations() {
    const viewDetailsBtn = document.querySelector('.card-actions .btn');
    if (viewDetailsBtn) {
        viewDetailsBtn.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-2px)';
            this.style.boxShadow = '0 4px 8px rgba(0, 123, 255, 0.3)';
            this.style.transition = 'all 0.3s ease';
        });

        viewDetailsBtn.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = 'none';
        });

        viewDetailsBtn.addEventListener('click', function() {
            // Add click animation
            this.style.transform = 'scale(0.95)';
            setTimeout(() => {
                this.style.transform = 'translateY(-2px)';
            }, 100);
        });
    }
}

// Add card hover effect
function initCardAnimations() {
    const dashboardCard = document.querySelector('.dashboard-card');
    if (dashboardCard) {
        dashboardCard.addEventListener('mouseenter', function() {
            this.style.transform = 'translateY(-5px)';
            this.style.boxShadow = '0 8px 25px rgba(0, 0, 0, 0.15)';
            this.style.transition = 'all 0.3s ease';
        });

        dashboardCard.addEventListener('mouseleave', function() {
            this.style.transform = 'translateY(0)';
            this.style.boxShadow = '0 2px 10px rgba(0, 0, 0, 0.1)';
        });
    }
}

// Initialize all animations when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    // Wait a bit for Chart.js to be available
    setTimeout(() => {
        if (typeof Chart !== 'undefined') {
            initHarborChart();
            initButtonAnimations();
            initCardAnimations();
            console.log('Harbor Activity Chart initialized with animations');
        } else {
            console.error('Chart.js library not found. Please include Chart.js before running this script.');
        }
    }, 500);
});

// Export functions for external use
window.HarborChart = {
    init: initHarborChart,
    updateData: updateChartData,
    initAnimations: function() {
        initButtonAnimations();
        initCardAnimations();
    }
};

// Highlight current sidebar link
document.addEventListener('DOMContentLoaded', () => {
    const currentPath = window.location.pathname;
    const sidebarLinks = document.querySelectorAll('.sidebar-nav .nav-link');

    sidebarLinks.forEach(link => {
        const linkPath = new URL(link.href).pathname;
        if (linkPath === currentPath) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
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

