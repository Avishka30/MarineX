  document.addEventListener("DOMContentLoaded", () => {

            // Load saved theme
            const savedTheme = localStorage.getItem('theme') || 'light';
            setTheme(savedTheme);
        });

        // Theme toggle functionality
        function toggleTheme() {
            const currentTheme = document.body.getAttribute('data-bs-theme');
            const newTheme = currentTheme === 'light' ? 'dark' : 'light';
            setTheme(newTheme);
        }

        function setTheme(theme) {
            document.body.setAttribute('data-bs-theme', theme);
            const themeIcon = document.getElementById('theme-icon');
            
            if (theme === 'dark') {
                themeIcon.className = 'fas fa-sun';
            } else {
                themeIcon.className = 'fas fa-moon';
            }
            
            // Save theme preference
            localStorage.setItem('theme', theme);
        }

const currentPage = window.location.pathname.split("/").pop(); // e.g., "dashboard.html"
document.querySelectorAll('.nav-link').forEach(link => {
    if (link.getAttribute('href') === currentPage) {
        link.classList.add('active');
    } else {
        link.classList.remove('active');
    }
});
    document.addEventListener("DOMContentLoaded", () => {
    const token = localStorage.getItem("token");
    const agentId = localStorage.getItem("agentId");

    if (!token || !agentId) {
        alert("You must log in first!");
        window.location.href = "login.html";
        return;
    }

    loadAgentPayments(agentId, token);
});

function loadAgentPayments(agentId, token) {
    fetch(`http://localhost:8080/api/payments/agent/${agentId}`, {
        method: "GET",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${token}` // if your backend uses JWT
        }
    })
    .then(res => res.json())
    .then(payments => {
        const tableBody = document.getElementById("paymentsTableBody");
        tableBody.innerHTML = "";

        payments.forEach(p => {
            const row = `
                <tr>
                    <td>${p.id}</td>
                    <td>${p.amount}</td>
                    <td>${p.paymentDate}</td>
                    <td>${p.method}</td>
                    <td>${p.status}</td>
                </tr>
            `;
            tableBody.innerHTML += row;
        });
    })
    .catch(err => console.error(err));
}
