  document.addEventListener("DOMContentLoaded", () => {
            const token = localStorage.getItem("token");
            const agentId = localStorage.getItem("agentId");

            if (!token || !agentId) {
                alert("Session expired. Please log in again.");
                window.location.href = "login.html";
                return;
            }

            setTheme(localStorage.getItem('theme') || 'light');
            highlightActiveNav();
            fetchVessels(agentId);

            document.querySelectorAll('.dropdown-item.text-danger').forEach(btn => {
                btn.addEventListener('click', logout);
            });

            // Real-time search
            document.getElementById('filterName').addEventListener('input', applyFilters);
            document.getElementById('filterCategory').addEventListener('change', applyFilters);
        });

        const apiBase = 'http://localhost:8080/api/vessels';
        const vesselForm = document.getElementById('vesselForm');
        const vesselCards = document.getElementById('vesselCards');
        let allVessels = [];

        function getFetchOptions(method = 'GET', body = null) {
            const token = localStorage.getItem("token");
            const options = {
                method,
                headers: { 
                    'Authorization': `Bearer ${token}`, 
                    'Content-Type': 'application/json' 
                }
            };
            if (body) options.body = JSON.stringify(body);
            return options;
        }

        // Fetch only vessels of the logged-in agent
        async function fetchVessels(agentId) {
            try {
                showLoading();
                const res = await fetch(`${apiBase}/agent/${agentId}`, getFetchOptions());
                if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
                const vessels = await res.json();
                allVessels = vessels;
                renderCards(vessels);
                hideLoading();
            } catch(err) {
                console.error(err);
                alert("Failed to fetch vessels: " + err.message);
                hideLoading();
            }
        }

        // Show loading state
        function showLoading() {
            vesselCards.innerHTML = `
                <div class="col-12 text-center p-5">
                    <div class="loading-spinner"></div>
                    <p class="mt-3">Loading vessels...</p>
                </div>
            `;
        }

        // Hide loading state
        function hideLoading() {
            // Loading will be replaced by renderCards
        }

        // Render vessel cards with enhanced design
        function renderCards(vessels) {
            if (vessels.length === 0) {
                vesselCards.innerHTML = `
                    <div class="col-12">
                        <div class="empty-state">
                            <i class="fas fa-ship"></i>
                            <h4>No vessels found</h4>
                            <p>No vessels match your current filters. Try adjusting your search criteria or add a new vessel.</p>
                        </div>
                    </div>
                `;
                return;
            }

            vesselCards.innerHTML = '';
            vessels.forEach(v => {
                const col = document.createElement('div');
                col.className = 'col-lg-6 col-xl-4';
                col.innerHTML = `
                    <div class="vessel-card">
                        <div class="card-body p-3">
                            <h5 class="vessel-name">${v.name}</h5>
                            <span class="vessel-category category-${v.category}">${v.category}</span>
                            
                            <div class="vessel-info">
                                <div class="vessel-info-item">
                                    <i class="fas fa-ruler-combined"></i>
                                    <span><strong>Size:</strong> ${v.size}m</span>
                                </div>
                                <div class="vessel-info-item">
                                    <i class="fas fa-building"></i>
                                    <span><strong>Company:</strong> ${v.companyName}</span>
                                </div>
                                <div class="vessel-info-item">
                                    <i class="fas fa-tag"></i>
                                    <span><strong>ID:</strong> ${v.vesselId}</span>
                                </div>
                            </div>
                            
                            <div class="vessel-actions">
                                <button class="btn btn-info btn-sm flex-fill" onclick="editVessel(${v.vesselId})">
                                    <i class="fas fa-edit"></i>
                                    Edit
                                </button>
                                <button class="btn btn-danger btn-sm flex-fill" onclick="deleteVessel(${v.vesselId})">
                                    <i class="fas fa-trash"></i>
                                    Delete
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                vesselCards.appendChild(col);
            });
        }

        // Apply filters with real-time search
        function applyFilters() {
            const nameFilter = document.getElementById('filterName').value.toLowerCase();
            const categoryFilter = document.getElementById('filterCategory').value;

            let filtered = allVessels.filter(vessel => {
                const matchesName = vessel.name.toLowerCase().includes(nameFilter);
                const matchesCategory = !categoryFilter || vessel.category === categoryFilter;
                return matchesName && matchesCategory;
            });

            renderCards(filtered);
        }

        // Clear filters
        function clearFilters() {
            document.getElementById('filterName').value = '';
            document.getElementById('filterCategory').value = '';
            renderCards(allVessels);
        }

        // Vessel CRUD
        vesselForm.addEventListener('submit', async e => {
            e.preventDefault();
            const vesselId = document.getElementById('vesselId').value;
            const payload = {
                name: document.getElementById('vesselName').value,
                category: document.getElementById('vesselCategory').value,
                size: document.getElementById('vesselSize').value,
                companyName: document.getElementById('companyName').value,
                agentId: localStorage.getItem("agentId")
            };

            try {
                // Show loading on save button
                const submitBtn = vesselForm.querySelector('button[type="submit"]');
                const originalContent = submitBtn.innerHTML;
                submitBtn.innerHTML = '<div class="loading-spinner"></div> Saving...';
                submitBtn.disabled = true;

                const url = vesselId ? `${apiBase}/${vesselId}` : apiBase;
                const method = vesselId ? 'PUT' : 'POST';
                const res = await fetch(url, getFetchOptions(method, payload));
                
                if (!res.ok) {
                    const errData = await res.json().catch(() => ({}));
                    throw new Error(errData.message || `${res.status} ${res.statusText}`);
                }

                vesselForm.reset();
                document.getElementById('vesselId').value = '';
                
                // Reset form header
                document.querySelector('.form-card .filter-header').innerHTML = '<i class="fas fa-plus-circle"></i> Add New Vessel';
                
                fetchVessels(localStorage.getItem("agentId"));

                // Reset button
                submitBtn.innerHTML = originalContent;
                submitBtn.disabled = false;
                
            } catch(err) {
                console.error(err);
                alert("Failed to save vessel: " + err.message);
                
                // Reset button on error
                const submitBtn = vesselForm.querySelector('button[type="submit"]');
                submitBtn.innerHTML = '<i class="fas fa-save"></i> Save';
                submitBtn.disabled = false;
            }
        });

        // Edit vessel
        async function editVessel(id) {
            try {
                const res = await fetch(`${apiBase}/${id}`, getFetchOptions());
                if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
                const v = await res.json();
                
                document.getElementById('vesselId').value = v.vesselId;
                document.getElementById('vesselName').value = v.name;
                document.getElementById('vesselCategory').value = v.category;
                document.getElementById('vesselSize').value = v.size;
                document.getElementById('companyName').value = v.companyName;
                
                // Update form header to show editing mode
                document.querySelector('.form-card .filter-header').innerHTML = '<i class="fas fa-edit"></i> Edit Vessel';
                document.querySelector('.btn-success').innerHTML = '<i class="fas fa-save"></i> Update';
                
                // Scroll to form
                document.querySelector('.form-card').scrollIntoView({ behavior: 'smooth' });
                
            } catch(err) {
                console.error(err);
                alert("Failed to fetch vessel: " + err.message);
            }
        }

        // Delete vessel
        async function deleteVessel(id) {
            if (!confirm("Are you sure you want to delete this vessel? This action cannot be undone.")) return;
            
            try {
                const res = await fetch(`${apiBase}/${id}`, getFetchOptions('DELETE'));
                if (!res.ok) throw new Error(`${res.status} ${res.statusText}`);
                
                // Show success message
                showSuccessMessage("Vessel deleted successfully!");
                
                fetchVessels(localStorage.getItem("agentId"));
            } catch(err) {
                console.error(err);
                alert("Failed to delete vessel: " + err.message);
            }
        }

        // Show success message
        function showSuccessMessage(message) {
            const alertDiv = document.createElement('div');
            alertDiv.className = 'alert alert-success alert-dismissible fade show position-fixed';
            alertDiv.style.cssText = 'top: 100px; right: 20px; z-index: 1050; min-width: 300px;';
            alertDiv.innerHTML = `
                <i class="fas fa-check-circle me-2"></i>
                ${message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            `;
            document.body.appendChild(alertDiv);
            
            // Auto-remove after 3 seconds
            setTimeout(() => {
                if (alertDiv.parentNode) {
                    alertDiv.remove();
                }
            }, 3000);
        }

        // Logout
        function logout() {
            localStorage.removeItem("token");
            localStorage.removeItem("agentId");
            window.location.href = "login.html";
        }

        // Theme helpers
        function toggleTheme() {
            const theme = document.body.getAttribute('data-bs-theme') === 'light' ? 'dark' : 'light';
            setTheme(theme);
        }

        function setTheme(theme) {
            document.body.setAttribute('data-bs-theme', theme);
            document.getElementById('theme-icon').className = theme === 'dark' ? 'fas fa-sun' : 'fas fa-moon';
            localStorage.setItem('theme', theme);
        }

        function highlightActiveNav() {
            const currentPage = window.location.pathname.split("/").pop();
            document.querySelectorAll('.nav-link').forEach(link => {
                link.classList.toggle('active', link.getAttribute('href') === currentPage);
            });
        }