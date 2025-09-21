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

  const API_BOOKINGS = "http://localhost:8080/api/bookings"; // <- change if different
    const token = localStorage.getItem('token') || ""; // include if needed

    function getAuthHeaders() {
      const headers = { "Content-Type": "application/json" };
      if (token) headers["Authorization"] = "Bearer " + token;
      return headers;
    }

    // ---------- HELPERS ----------
    function parseISODate(d) {
      if (!d) return null;
      // if already Date object
      if (d instanceof Date) return d;
      // ensure string is ISO-like
      return new Date(d);
    }
    // FullCalendar treats end as exclusive; for inclusive endDate add 1 day
    function addOneDayIso(isoStr) {
      const d = parseISODate(isoStr);
      if (!d || isNaN(d)) return null;
      d.setDate(d.getDate() + 1);
      return d.toISOString();
    }

    function formatDateHuman(d) {
      if (!d) return "";
      const dt = (d instanceof Date) ? d : new Date(d);
      return dt.toLocaleString([], { year:'numeric', month:'short', day:'numeric' });
    }

    // check if targetDate is >= start && < end (end exclusive)
    function isDateInEvent(dayDate, eventStart, eventEndExclusive) {
      const d = new Date(dayDate.getFullYear(), dayDate.getMonth(), dayDate.getDate()).getTime();
      const s = new Date(eventStart.getFullYear(), eventStart.getMonth(), eventStart.getDate()).getTime();
      const e = eventEndExclusive ? new Date(eventEndExclusive.getFullYear(), eventEndExclusive.getMonth(), eventEndExclusive.getDate()).getTime() : s + 24*3600*1000;
      return d >= s && d < e;
    }

    // ---------- UI ELEMENTS ----------
    const bookingModalEl = document.getElementById('bookingModal');
    const bookingModal = new bootstrap.Modal(bookingModalEl, { backdrop: true });
    const bookingListEl = document.getElementById('bookingList');
    const modalDateEl = document.getElementById('modalDate');

    // ---------- CALENDAR SETUP ----------
    document.addEventListener('DOMContentLoaded', function() {
      const calendarEl = document.getElementById('calendar');
      if (!calendarEl) {
        console.error("Calendar element not found");
        return;
      }

      const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: 'dayGridMonth,timeGridWeek,timeGridDay'
        },
        height: 'auto',
        navLinks: true,
        selectable: true,
        events: async function(fetchInfo, successCallback, failureCallback) {
          // fetch events from API; map backend fields to FullCalendar events
          try {
            console.log("Fetching bookings for calendar...");
            const res = await fetch(API_BOOKINGS, {
              method: "GET",
              headers: getAuthHeaders()
            });
            if (!res.ok) {
              const txt = await res.text().catch(()=>"");
              throw new Error("Bookings fetch failed: " + res.status + " " + txt);
            }
            let data = await res.json();
            // backend may return {data: [...] } — handle both shapes
            if (data && data.data && Array.isArray(data.data)) data = data.data;
            if (!Array.isArray(data)) data = [];

            // map bookings -> events
            const events = data.map(b => {
              const start = b.bookingDate || b.startDate || b.start; // adapt fields
              // for full-day bookings treat them as allDay events
              let end = b.endDate || b.end;
              // convert end to exclusive by adding 1 day if provided
              const endIso = end ? addOneDayIso(end) : null;

              // choose title (vesselName or booking id)
              const title = (b.vesselName || b.vessel?.name || `Booking #${b.bookingId || b.id || ''}`).toString();

              return {
                id: b.bookingId ?? b.id ?? title,
                title: title,
                start: start,
                end: endIso, // if null, FullCalendar treats as single-day event
                allDay: true,
                extendedProps: { booking: b }
              };
            });

            console.log("Calendar events loaded:", events.length);
            successCallback(events);
          } catch (err) {
            console.error("Error loading bookings for calendar:", err);
            failureCallback(err);
          }
        },

        // click on blank day
        dateClick: function(info) {
          const clickedDate = info.date;
          // find events on this date
          const events = calendar.getEvents().filter(ev => {
            const evStart = ev.start;
            const evEnd = ev.end ? ev.end : new Date(ev.start.getTime() + 24*3600*1000); // treat as 1 day if no end
            return isDateInEvent(clickedDate, evStart, evEnd);
          });

          showBookingsModalForDate(clickedDate, events.map(ev => ev.extendedProps.booking || ev));
        },

        // click on an event
        eventClick: function(info) {
          const booking = info.event.extendedProps.booking || info.event;
          showBookingsModalForDate(info.event.start, [booking]);
        },

        loading: function(isLoading) {
          if (isLoading) {
            // optionally show spinner
          } else {
            // loaded
          }
        }

      });

      calendar.render();

      // Expose calendar for debugging
      window._MarineXCalendar = calendar;
    });

    // ---------- Modal rendering ----------
    function showBookingsModalForDate(dateObj, bookingsArray) {
      modalDateEl.textContent = formatDateHuman(dateObj);
      bookingListEl.innerHTML = "";

      if (!bookingsArray || bookingsArray.length === 0) {
        const emptyEl = document.createElement('div');
        emptyEl.className = 'text-center text-muted';
        emptyEl.textContent = 'No bookings for this day';
        bookingListEl.appendChild(emptyEl);
      } else {
        bookingsArray.forEach(b => {
          const li = document.createElement('div');
          li.className = 'list-group-item booking-item';

          const vessel = b.vesselName || (b.vessel && b.vessel.name) || '—';
          const berth = b.berthNumber || b.berth?.berthNumber || (b.berthId ? 'Berth ' + b.berthId : '—');
          const id = b.bookingId ?? b.id ?? '—';
          const start = b.bookingDate ?? b.startDate ?? b.start;
          const end = b.endDate ?? b.end;
          const status = b.status ?? b.bookingStatus ?? '—';
          const services = (b.serviceIds && b.serviceIds.length) ? b.serviceIds.join(', ') : (b.services ? (b.services.map(s=>s.name||s.serviceName).join(', ')) : '—');

          li.innerHTML = `
            <div class="d-flex justify-content-between">
              <div>
                <div class="booking-title">#${id} — ${escapeHtml(vessel)}</div>
                <div class="text-muted-small">${escapeHtml(berth)} • ${escapeHtml(services)}</div>
                <div class="text-muted-small">From: ${formatDateHuman(start)} ${ end ? ' — To: ' + formatDateHuman(end) : '' }</div>
              </div>
              <div class="text-end">
                <div><span class="badge ${status && status.toUpperCase().includes('CONFIR') ? 'bg-success' : 'bg-secondary'}">${escapeHtml(status)}</span></div>
                <div class="mt-2"><button class="btn btn-sm btn-outline-primary view-details-btn">View</button></div>
              </div>
            </div>
            <div class="mt-2 text-muted-small">Agent: ${escapeHtml(b.agent?.fullName || b.agentName || b.agent?.email || '—')}</div>
          `;

          // attach click handler for view button
          li.querySelector('.view-details-btn').addEventListener('click', () => {
            showBookingDetailAlert(b);
          });

          bookingListEl.appendChild(li);
        });
      }

      bookingModal.show();
    }

    function showBookingDetailAlert(b) {
      // Small quick details display — replace with another modal or a details pane if you prefer
      const lines = [
        `Booking ID: ${b.bookingId ?? b.id ?? '—'}`,
        `Vessel: ${b.vesselName || b.vessel?.name || '—'}`,
        `Berth: ${b.berthNumber || b.berth?.berthNumber || b.berthId || '—'}`,
        `Services: ${(b.services && b.services.length) ? b.services.map(s=>s.name||s.serviceName).join(', ') : (b.serviceIds ? b.serviceIds.join(', ') : '—')}`,
        `Status: ${b.status || b.bookingStatus || '—'}`,
        `From: ${formatDateHuman(b.bookingDate || b.startDate || b.start)}`,
        `To: ${formatDateHuman(b.endDate || b.end)}`,
        `Agent: ${b.agent?.fullName || b.agentName || b.agent?.email || '—'}`
      ];
      alert(lines.join('\n'));
    }

    // small helper
    function escapeHtml(str) {
      if (str == null) return '';
      return String(str)
        .replaceAll('&','&amp;')
        .replaceAll('<','&lt;')
        .replaceAll('>','&gt;')
        .replaceAll('"','&quot;')
        .replaceAll("'",'&#039;');
    }

    // ---------- Diagnostics: warn if CSS/JS CDN failed ----------
    window.addEventListener('error', (ev) => {
      // CSS/JS loading errors show up here — log them so you can debug CDN 404s
      console.warn('Resource load error:', ev);
    });
