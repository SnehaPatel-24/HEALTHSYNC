// HEALTHSYNC — app.js | Connects frontend to Spring Boot backend
const API = '';
let token     = localStorage.getItem('hms_token') || '';
let userEmail = localStorage.getItem('hms_email') || '';
let userRole  = localStorage.getItem('hms_role')  || '';

const pagination = { patients: { page: 0, totalPages: 1 } };

// ============================================================
// TOAST
// ============================================================
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast toast-${type} show`;
    setTimeout(() => { toast.className = 'toast'; }, 3500);
}

// ============================================================
// API HELPER
// ============================================================
async function api(endpoint, method = 'GET', body = null) {
    const isAuth = endpoint.includes('/api/auth/');
    const headers = { 'Content-Type': 'application/json' };
    if (token && !isAuth) headers['Authorization'] = `Bearer ${token}`;

    const config = { method, headers };
    if (body) config.body = JSON.stringify(body);

    try {
        const res = await fetch(API + endpoint, config);
        const contentType = res.headers.get('content-type');
        let data;
        if (contentType && contentType.includes('application/json')) {
            data = await res.json();
        } else {
            const text = await res.text();
            data = { message: text };
        }

        if (res.status === 401) { logout(); throw new Error('Session expired. Please login again.'); }
        if (res.status === 403) { throw new Error('You do not have permission to perform this action.'); }
        if (!res.ok) {
            // If backend sends a message field, use it
            if (data.message) throw new Error(data.message);
            // If backend sends a validation map like {mobileNumber: "...", email: "..."}
            const values = Object.values(data);
            if (values.length) throw new Error('Please fill all the details.');
            throw new Error(`Error ${res.status}`);
        }

        return data;
    } catch (err) {
        throw err;
    }
}

// ============================================================
// AUTH
// ============================================================
function showTab(tab) {
    document.getElementById('login-form').style.display           = tab === 'login'    ? 'block' : 'none';
    document.getElementById('register-form').style.display        = tab === 'register' ? 'block' : 'none';
    document.getElementById('forgot-password-form').style.display = tab === 'forgot'   ? 'block' : 'none';

    document.querySelectorAll('.tab-btn').forEach((btn, i) => {
        btn.classList.toggle('active', (i === 0 && tab === 'login') || (i === 1 && tab === 'register'));
    });
    // Clear all error/success messages when switching tabs
    ['login-error','reg-error','reg-success','fp-error','fp-success'].forEach(id => {
        const el = document.getElementById(id);
        if (el) el.textContent = '';
    });
}

async function resetPassword() {
    const email       = document.getElementById('fp-email').value.trim();
    const newPassword = document.getElementById('fp-new-password').value;
    const confirmPass = document.getElementById('fp-confirm-password').value;
    const errorEl     = document.getElementById('fp-error');
    const successEl   = document.getElementById('fp-success');
    errorEl.textContent   = '';
    successEl.textContent = '';

    if (!email || !newPassword || !confirmPass) {
        errorEl.textContent = 'Please fill in all fields.'; return;
    }
    if (newPassword.length < 6) {
        errorEl.textContent = 'Password must be at least 6 characters.'; return;
    }
    if (newPassword !== confirmPass) {
        errorEl.textContent = 'Passwords do not match.'; return;
    }

    try {
        await api('/api/auth/reset-password', 'POST', { email, newPassword });
        successEl.textContent = '✅ Password reset successfully!';
        setTimeout(() => {
            document.getElementById('login-email').value    = email;
            document.getElementById('login-password').value = '';
            showTab('login');
        }, 2000);
    } catch (err) {
        errorEl.textContent = err.message || 'Failed to reset password. Please try again.';
    }
}

async function login() {
    const email    = document.getElementById('login-email').value.trim();
    const password = document.getElementById('login-password').value;
    const errorEl  = document.getElementById('login-error');
    errorEl.textContent = '';

    if (!email || !password) { errorEl.textContent = 'Please enter email and password.'; return; }

    try {
        const data = await api('/api/auth/login', 'POST', { email, password });
        token     = data.token;
        userEmail = email;
        userRole  = data.role || '';
        localStorage.setItem('hms_token', token);
        localStorage.setItem('hms_email', email);
        localStorage.setItem('hms_role',  userRole);
        startApp();
    } catch (err) {
        errorEl.textContent = 'Invalid email or password. Please try again.';
    }
}

async function register() {
    const fullName  = document.getElementById('reg-name').value.trim();
    const email     = document.getElementById('reg-email').value.trim();
    const password  = document.getElementById('reg-password').value;
    const role      = document.getElementById('reg-role').value;
    const errorEl   = document.getElementById('reg-error');
    const successEl = document.getElementById('reg-success');
    errorEl.textContent   = '';
    successEl.textContent = '';

    if (!fullName || !email || !password) { errorEl.textContent = 'Please fill in all fields.'; return; }
    if (password.length < 6) { errorEl.textContent = 'Password must be at least 6 characters.'; return; }

    try {
        await api('/api/auth/register', 'POST', { fullName, email, password, role });
        successEl.textContent = '✅ Registered! Redirecting to login...';
        setTimeout(() => {
            document.getElementById('login-email').value    = email;
            document.getElementById('login-password').value = password;
            showTab('login');
        }, 1500);
    } catch (err) {
        if (err.message.toLowerCase().includes('already') || err.message.toLowerCase().includes('duplicate')) {
            errorEl.textContent = 'Email already registered. Please login instead.';
            setTimeout(() => showTab('login'), 2000);
        } else {
            errorEl.textContent = err.message || 'Registration failed. Try again.';
        }
    }
}

function logout() {
    token = ''; userEmail = ''; userRole = '';
    localStorage.removeItem('hms_token');
    localStorage.removeItem('hms_email');
    localStorage.removeItem('hms_role');
    document.getElementById('app').style.display         = 'none';
    document.getElementById('auth-screen').style.display = 'flex';
    showTab('login');
}

// ============================================================
// APP STARTUP
// ============================================================
function startApp() {
    document.getElementById('auth-screen').style.display = 'none';
    document.getElementById('app').style.display         = 'flex';
    document.getElementById('sidebar-user').textContent  = '👤 ' + userEmail;
    showSection('dashboard');
}

window.onload = () => {
    if (token) { startApp(); }
    else { showTab('login'); }
};

// ============================================================
// NAVIGATION
// ============================================================
function showSection(name) {
    document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
    document.querySelectorAll('.nav-link').forEach(l => l.classList.remove('active'));
    document.getElementById(name).classList.add('active');
    document.querySelectorAll('.nav-link').forEach(l => {
        if (l.getAttribute('onclick') && l.getAttribute('onclick').includes(`'${name}'`)) {
            l.classList.add('active');
        }
    });
    if (name === 'dashboard')     loadDashboard();
    if (name === 'patients')      loadPatients();
    if (name === 'doctors')       loadDoctors();
    if (name === 'appointments')  loadAppointments();
    if (name === 'prescriptions') loadPrescriptions();
}

// ============================================================
// MODAL HELPERS
// ============================================================
function openModal(id) { document.getElementById(id).style.display = 'flex'; }
function closeModal(id) {
    document.getElementById(id).style.display = 'none';
    const errEl = document.querySelector(`#${id} .error-msg`);
    if (errEl) errEl.textContent = '';
}
document.addEventListener('click', (e) => {
    if (e.target.classList.contains('modal-overlay')) e.target.style.display = 'none';
});

// ============================================================
// DASHBOARD
// ============================================================
async function loadDashboard() {
    try {
        const data = await api('/api/dashboard/stats');
        document.getElementById('stat-patients').textContent      = data.totalPatients          ?? '—';
        document.getElementById('stat-doctors').textContent       = data.totalDoctors           ?? '—';
        document.getElementById('stat-appointments').textContent  = data.totalAppointments      ?? '—';
        document.getElementById('stat-completed').textContent     = data.completedAppointments  ?? '—';
        document.getElementById('stat-cancelled').textContent     = data.cancelledAppointments  ?? '—';
        document.getElementById('stat-prescriptions').textContent = data.totalPrescriptions     ?? '—';
    } catch (err) {
        console.warn('Dashboard stats not available:', err.message);
    }
}

// ============================================================
// PATIENTS
// ============================================================
async function loadPatients(page = 0) {
    pagination.patients.page = page;
    try {
        const data  = await api(`/api/patients?page=${page}&size=10&sort=id`);
        const rows  = data.content ?? data;
        const total = data.totalPages ?? 1;
        pagination.patients.totalPages = total;
        const tbody = document.getElementById('patients-table-body');
        tbody.innerHTML = '';
        if (!rows.length) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#6b7280;padding:30px">No patients found. Add one!</td></tr>';
            return;
        }
        rows.forEach(p => {
            tbody.innerHTML += `
            <tr>
                <td><b>${p.id ?? '—'}</b></td>
                <td>${p.patientCode ?? '—'}</td>
                <td>${p.firstName ?? ''} ${p.lastName ?? ''}</td>
                <td>${p.gender ?? '—'}</td>
                <td>${p.bloodGroup ?? '—'}</td>
                <td>${p.mobileNumber ?? '—'}</td>
                <td>${p.disease ?? '—'}</td>
                <td class="action-btns">
                    <button class="btn btn-sm" onclick="editPatient(${p.id})">✏️ Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete('patient', ${p.id})">🗑️</button>
                </td>
            </tr>`;
        });
        document.getElementById('patients-page-info').textContent = `Page ${page + 1} of ${total}`;
    } catch (err) { showToast(err.message, 'error'); }
}

function prevPage(module) {
    if (module === 'patients' && pagination.patients.page > 0) loadPatients(pagination.patients.page - 1);
}
function nextPage(module) {
    if (module === 'patients' && pagination.patients.page < pagination.patients.totalPages - 1) loadPatients(pagination.patients.page + 1);
}

async function searchPatients() {
    const query = document.getElementById('patient-search').value.trim();
    if (!query) { loadPatients(0); return; }
    try {
        const data = await api(`/api/patients/search?keyword=${encodeURIComponent(query)}`);
        const rows = Array.isArray(data) ? data : (data.content ?? []);
        const tbody = document.getElementById('patients-table-body');
        tbody.innerHTML = '';
        if (!rows.length) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;color:#6b7280;padding:30px">No results found</td></tr>';
            return;
        }
        rows.forEach(p => {
            tbody.innerHTML += `
            <tr>
                <td>${p.patientCode ?? '—'}</td>
                <td>${p.firstName ?? ''} ${p.lastName ?? ''}</td>
                <td>${p.gender ?? '—'}</td>
                <td>${p.bloodGroup ?? '—'}</td>
                <td>${p.mobileNumber ?? '—'}</td>
                <td>${p.disease ?? '—'}</td>
                <td class="action-btns">
                    <button class="btn btn-sm" onclick="editPatient(${p.id})">✏️ Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete('patient', ${p.id})">🗑️</button>
                </td>
            </tr>`;
        });
    } catch (err) { loadPatients(0); }
}

async function editPatient(id) {
    try {
        const p = await api(`/api/patients/${id}`);
        document.getElementById('patient-modal-title').textContent = 'Edit Patient';
        document.getElementById('patient-id').value      = p.id;
        document.getElementById('p-firstName').value     = p.firstName    ?? '';
        document.getElementById('p-lastName').value      = p.lastName     ?? '';
        document.getElementById('p-gender').value        = p.gender       ?? '';
        document.getElementById('p-bloodGroup').value    = p.bloodGroup   ?? '';
        document.getElementById('p-dob').value           = p.dateOfBirth  ?? '';
        document.getElementById('p-mobile').value        = p.mobileNumber ?? '';
        document.getElementById('p-email').value         = p.email        ?? '';
        document.getElementById('p-address').value       = p.address      ?? '';
        document.getElementById('p-disease').value       = p.disease      ?? '';
        document.getElementById('p-allergies').value     = p.allergies    ?? '';
        openModal('patient-modal');
    } catch (err) { showToast('Failed to load patient: ' + err.message, 'error'); }
}

async function savePatient() {
    const id        = document.getElementById('patient-id').value;
    const firstName = document.getElementById('p-firstName').value.trim();
    const lastName  = document.getElementById('p-lastName').value.trim();
    const gender    = document.getElementById('p-gender').value;
    const errorEl   = document.getElementById('patient-modal-error');
    errorEl.textContent = '';
    if (!firstName || !lastName || !gender) { errorEl.textContent = 'Please fill all the details.'; return; }
    const body = {
        firstName, lastName, gender,
        bloodGroup:   document.getElementById('p-bloodGroup').value,
        dateOfBirth:  document.getElementById('p-dob').value      || null,
        mobileNumber: document.getElementById('p-mobile').value.trim(),
        email:        document.getElementById('p-email').value.trim(),
        address:      document.getElementById('p-address').value.trim(),
        disease:      document.getElementById('p-disease').value.trim(),
        allergies:    document.getElementById('p-allergies').value.trim(),
    };
    try {
        if (id) { await api(`/api/patients/${id}`, 'PUT', body); showToast('Patient updated!'); }
        else    { await api('/api/patients', 'POST', body);      showToast('Patient added!'); }
        closeModal('patient-modal');
        loadPatients(pagination.patients.page);
    } catch (err) { document.getElementById('patient-modal-error').textContent = err.message; }
}

// ============================================================
// DOCTORS
// ============================================================
async function loadDoctors() {
    try {
        const data  = await api('/api/doctors');
        const rows  = Array.isArray(data) ? data : (data.content ?? []);
        const tbody = document.getElementById('doctors-table-body');
        tbody.innerHTML = '';
        if (!rows.length) {
            tbody.innerHTML = '<tr><td colspan="7" style="text-align:center;color:#6b7280;padding:30px">No doctors found. Add one!</td></tr>';
            return;
        }
        rows.forEach(d => {
            tbody.innerHTML += `
            <tr>
                <td>${d.doctorCode ?? '—'}</td>
                <td>${d.fullName ?? '—'}</td>
                <td>${d.specialization ?? '—'}</td>
                <td>${d.email ?? '—'}</td>
                <td>${d.phoneNumber ?? '—'}</td>
                <td>${d.experienceYears ?? '—'} yrs</td>
                <td class="action-btns">
                    <button class="btn btn-sm" onclick="editDoctor(${d.id})">✏️ Edit</button>
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete('doctor', ${d.id})">🗑️</button>
                </td>
            </tr>`;
        });
    } catch (err) { showToast(err.message, 'error'); }
}

async function editDoctor(id) {
    try {
        const d = await api(`/api/doctors/${id}`);
        document.getElementById('doctor-modal-title').textContent  = 'Edit Doctor';
        document.getElementById('doctor-id').value                 = d.id;
        document.getElementById('d-fullName').value                = d.fullName       ?? '';
        document.getElementById('d-specialization').value          = d.specialization ?? '';
        document.getElementById('d-email').value                   = d.email          ?? '';
        document.getElementById('d-phone').value                   = d.phoneNumber    ?? '';
        document.getElementById('d-experience').value              = d.experienceYears ?? '';
        openModal('doctor-modal');
    } catch (err) { showToast('Failed to load doctor: ' + err.message, 'error'); }
}

async function saveDoctor() {
    const id       = document.getElementById('doctor-id').value;
    const fullName = document.getElementById('d-fullName').value.trim();
    const spec     = document.getElementById('d-specialization').value.trim();
    const errorEl  = document.getElementById('doctor-modal-error');
    errorEl.textContent = '';
    if (!fullName || !spec) { errorEl.textContent = 'Full name and specialization are required.'; return; }
    const body = {
        fullName, specialization: spec,
        email:          document.getElementById('d-email').value.trim(),
        phoneNumber:    document.getElementById('d-phone').value.trim(),
        experienceYears: parseInt(document.getElementById('d-experience').value) || 0,
    };
    try {
        if (id) { await api(`/api/doctors/${id}`, 'PUT', body); showToast('Doctor updated!'); }
        else    { await api('/api/doctors', 'POST', body);      showToast('Doctor added!'); }
        closeModal('doctor-modal');
        loadDoctors();
    } catch (err) { document.getElementById('doctor-modal-error').textContent = err.message; }
}

// ============================================================
// APPOINTMENTS
// ============================================================
async function loadAppointments() {
    try {
        const data  = await api('/api/appointments');
        const rows  = Array.isArray(data) ? data : (data.content ?? []);
        const tbody = document.getElementById('appointments-table-body');
        tbody.innerHTML = '';
        if (!rows.length) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#6b7280;padding:30px">No appointments found. Book one!</td></tr>';
            return;
        }
        rows.forEach(a => {
            const badge = `<span class="badge badge-${(a.status||'').toLowerCase()}">${a.status ?? '—'}</span>`;
            tbody.innerHTML += `
            <tr>
                <td>${a.id ?? '—'}</td>
                <td>${a.appointmentCode ?? '—'}</td>
                <td>${a.patientId ?? '—'}</td>
                <td>${a.doctorName ?? '—'}</td>
                <td>${a.appointmentDate ?? '—'}</td>
                <td>${a.appointmentTime ?? '—'}</td>
                <td>${badge}</td>
                <td class="action-btns">
                    ${a.status === 'SCHEDULED' ? `
                        <button class="btn btn-success" onclick="completeAppointment(${a.id})">✅</button>
                        <button class="btn btn-warning" onclick="cancelAppointment(${a.id})">❌</button>
                    ` : ''}
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete('appointment', ${a.id})">🗑️</button>
                </td>
            </tr>`;
        });
    } catch (err) { showToast(err.message, 'error'); }
}

async function saveAppointment() {
    const id        = document.getElementById('appointment-id').value;
    const patientId = document.getElementById('a-patientId').value;
    const doctor    = document.getElementById('a-doctorName').value.trim();
    const date      = document.getElementById('a-date').value;
    const time      = document.getElementById('a-time').value;
    const errorEl   = document.getElementById('appointment-modal-error');
    errorEl.textContent = '';
    if (!patientId || !doctor || !date || !time) { errorEl.textContent = 'All fields are required.'; return; }
    const body = {
        patientId: parseInt(patientId), doctorName: doctor,
        appointmentDate: date, appointmentTime: time,
        remarks: document.getElementById('a-remarks').value.trim(),
    };
    try {
        if (id) { await api(`/api/appointments/${id}`, 'PUT', body); showToast('Appointment updated!'); }
        else    { await api('/api/appointments', 'POST', body);      showToast('Appointment booked!'); }
        closeModal('appointment-modal');
        loadAppointments();
    } catch (err) { document.getElementById('appointment-modal-error').textContent = err.message; }
}

async function completeAppointment(id) {
    try { await api(`/api/appointments/${id}/complete`, 'PUT'); showToast('Appointment completed!'); loadAppointments(); }
    catch (err) { showToast(err.message, 'error'); }
}
async function cancelAppointment(id) {
    try { await api(`/api/appointments/${id}/cancel`, 'PUT'); showToast('Appointment cancelled.', 'info'); loadAppointments(); }
    catch (err) { showToast(err.message, 'error'); }
}

// ============================================================
// PRESCRIPTIONS
// ============================================================
async function loadPrescriptions() {
    try {
        const data  = await api('/api/prescriptions');
        const rows  = Array.isArray(data) ? data : (data.content ?? []);
        const tbody = document.getElementById('prescriptions-table-body');
        tbody.innerHTML = '';
        if (!rows.length) {
            tbody.innerHTML = '<tr><td colspan="6" style="text-align:center;color:#6b7280;padding:30px">No prescriptions found.</td></tr>';
            return;
        }
        rows.forEach(pr => {
            tbody.innerHTML += `
            <tr>
                <td>${pr.prescriptionCode ?? '—'}</td>
                <td>${pr.diagnosis ?? '—'}</td>
                <td>${pr.medicines ?? '—'}</td>
                <td>${pr.dosage ?? '—'}</td>
                <td>${pr.instructions ?? '—'}</td>
                <td class="action-btns">
                    <button class="btn btn-danger btn-sm" onclick="confirmDelete('prescription', ${pr.id})">🗑️</button>
                </td>
            </tr>`;
        });
    } catch (err) { showToast(err.message, 'error'); }
}

async function openPrescriptionModal() {
    // Populate patient and doctor dropdowns
    try {
        const pData = await api('/api/patients?page=0&size=100&sort=id');
        const patients = pData.content ?? pData;
        const pSelect = document.getElementById('pr-patientCode');
        pSelect.innerHTML = '<option value="">-- Select Patient --</option>';
        patients.forEach(p => {
            const option = document.createElement('option');
            option.value = p.id;
            option.textContent = `${p.patientCode} — ${p.firstName} ${p.lastName}`;
            pSelect.appendChild(option);
        });
    } catch (err) { console.warn('Could not load patients:', err.message); }

    try {
        const dData = await api('/api/doctors');
        const doctors = Array.isArray(dData) ? dData : (dData.content ?? []);
        const dSelect = document.getElementById('pr-doctorCode');
        dSelect.innerHTML = '<option value="">-- Select Doctor --</option>';
        doctors.forEach(d => {
            const option = document.createElement('option');
            option.value = d.id;
            option.textContent = `${d.doctorCode ?? 'DOC'} — ${d.fullName} (${d.specialization})`;
            dSelect.appendChild(option);
        });
    } catch (err) { console.warn('Could not load doctors:', err.message); }

    openModal('prescription-modal');
}

async function savePrescription() {
    const id        = document.getElementById('prescription-id').value;
    const patientId = document.getElementById('pr-patientCode').value;
    const doctorId  = document.getElementById('pr-doctorCode').value;
    const diagnosis = document.getElementById('pr-diagnosis').value.trim();
    const medicines = document.getElementById('pr-medicines').value.trim();
    const errorEl   = document.getElementById('prescription-modal-error');
    errorEl.textContent = '';
    if (!patientId || !doctorId || !diagnosis || !medicines) { errorEl.textContent = 'Please fill all the details.'; return; }
    const body = {
        patientId: parseInt(patientId), doctorId: parseInt(doctorId),
        appointmentId: document.getElementById('pr-appointmentId').value ? parseInt(document.getElementById('pr-appointmentId').value) : null,
        diagnosis, medicines,
        dosage:       document.getElementById('pr-dosage').value.trim(),
        instructions: document.getElementById('pr-instructions').value.trim(),
    };
    try {
        if (id) { await api(`/api/prescriptions/${id}`, 'PUT', body); showToast('Prescription updated!'); }
        else    { await api('/api/prescriptions', 'POST', body);      showToast('Prescription saved!'); }
        closeModal('prescription-modal');
        loadPrescriptions();
    } catch (err) { document.getElementById('prescription-modal-error').textContent = err.message; }
}

// ============================================================
// DELETE
// ============================================================
function confirmDelete(type, id) {
    openModal('confirm-modal');
    document.getElementById('confirm-delete-btn').onclick = () => deleteRecord(type, id);
}

async function deleteRecord(type, id) {
    const endpoints = {
        patient: `/api/patients/${id}`, doctor: `/api/doctors/${id}`,
        appointment: `/api/appointments/${id}`, prescription: `/api/prescriptions/${id}`,
    };
    try {
        await api(endpoints[type], 'DELETE');
        closeModal('confirm-modal');
        showToast('Deleted successfully!');
        if (type === 'patient')      loadPatients(pagination.patients.page);
        if (type === 'doctor')       loadDoctors();
        if (type === 'appointment')  loadAppointments();
        if (type === 'prescription') loadPrescriptions();
    } catch (err) {
        closeModal('confirm-modal');
        showToast('Delete failed: ' + err.message, 'error');
    }
}
