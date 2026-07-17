// ---------------------------------------------------------
// Tab switching
// ---------------------------------------------------------
document.querySelectorAll(".tab-btn").forEach(btn => {
  btn.addEventListener("click", () => {
    document.querySelectorAll(".tab-btn").forEach(b => b.classList.remove("active"));
    document.querySelectorAll(".tab-panel").forEach(p => p.classList.remove("active"));
    btn.classList.add("active");
    document.getElementById(btn.dataset.tab).classList.add("active");
  });
});

document.getElementById("adminApiUrl").textContent = ADMIN_API_BASE;
document.getElementById("publicApiUrl").textContent = PUBLIC_API_BASE;

// ---------------------------------------------------------
// Helpers
// ---------------------------------------------------------
function formatDate(iso) {
  if (!iso) return "-";
  const d = new Date(iso);
  return d.toLocaleString();
}

function getCourseStatus(course) {
  const now = new Date();
  const start = course.registrationStartTime ? new Date(course.registrationStartTime) : null;
  const end = course.registrationEndTime ? new Date(course.registrationEndTime) : null;

  if (start && now < start) return { label: "Opens later", cls: "not-open" };
  if (end && now > end) return { label: "Closed", cls: "closed" };
  return { label: "Open", cls: "open" };
}

async function apiRequest(url, options = {}) {
  const res = await fetch(url, {
    headers: { "Content-Type": "application/json" },
    ...options
  });

  let body = null;
  try {
    body = await res.json();
  } catch (_) {
    // no JSON body
  }

  if (!res.ok) {
    const message = body?.message || `Request failed with status ${res.status}`;
    throw new Error(message);
  }

  return body;
}

// ---------------------------------------------------------
// PUBLIC VIEW — list courses + enroll
// ---------------------------------------------------------
let selectedCourseId = null;

async function loadPublicCourses() {
  const container = document.getElementById("publicCoursesList");
  container.innerHTML = "<p>Loading courses...</p>";

  try {
    const courses = await apiRequest(`${PUBLIC_API_BASE}/courses`);

    if (!courses.length) {
      container.innerHTML = "<p>No courses available yet.</p>";
      return;
    }

    container.innerHTML = "";
    courses.forEach(course => {
      const status = getCourseStatus(course);

      const card = document.createElement("div");
      card.className = "course-card";
      card.innerHTML = `
        <span class="badge ${status.cls}">${status.label}</span>
        <h3>${escapeHtml(course.title)}</h3>
        <p>${escapeHtml(course.description || "")}</p>
        <p>Instructor: ${escapeHtml(course.instructorName || "N/A")}</p>
        <p>Opens: ${formatDate(course.registrationStartTime)}</p>
        <p>Closes: ${formatDate(course.registrationEndTime)}</p>
        <button class="primary-btn enroll-btn" data-id="${course.id}" data-title="${escapeHtml(course.title)}">
          Enroll
        </button>
      `;
      container.appendChild(card);
    });

    document.querySelectorAll(".enroll-btn").forEach(btn => {
      btn.addEventListener("click", () => openEnrollModal(btn.dataset.id, btn.dataset.title));
    });

  } catch (err) {
    container.innerHTML = `<p class="message error">Failed to load courses: ${escapeHtml(err.message)}</p>`;
  }
}

function openEnrollModal(courseId, courseTitle) {
  selectedCourseId = courseId;
  document.getElementById("enrollCourseTitle").textContent = courseTitle;
  document.getElementById("enrollMessage").textContent = "";
  document.getElementById("enrollMessage").className = "message";
  document.getElementById("studentName").value = "";
  document.getElementById("enrollModalBackdrop").classList.add("active");
}

function closeEnrollModal() {
  document.getElementById("enrollModalBackdrop").classList.remove("active");
  selectedCourseId = null;
}

document.getElementById("cancelEnroll").addEventListener("click", closeEnrollModal);

document.getElementById("enrollForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const studentName = document.getElementById("studentName").value.trim();
  const messageEl = document.getElementById("enrollMessage");

  if (!studentName) return;

  try {
    await apiRequest(`${PUBLIC_API_BASE}/enrollments`, {
      method: "POST",
      body: JSON.stringify({ courseId: Number(selectedCourseId), studentName })
    });

    messageEl.textContent = "Enrolled successfully!";
    messageEl.className = "message success";

    setTimeout(() => {
      closeEnrollModal();
    }, 1200);

  } catch (err) {
    messageEl.textContent = err.message;
    messageEl.className = "message error";
  }
});

document.getElementById("refreshPublicCourses").addEventListener("click", loadPublicCourses);

// ---------------------------------------------------------
// ADMIN VIEW — create course + list all
// ---------------------------------------------------------
async function loadAdminCourses() {
  const tbody = document.getElementById("adminCoursesTableBody");
  tbody.innerHTML = `<tr><td colspan="5">Loading...</td></tr>`;

  try {
    const courses = await apiRequest(`${ADMIN_API_BASE}/courses`);

    if (!courses.length) {
      tbody.innerHTML = `<tr><td colspan="5">No courses yet.</td></tr>`;
      return;
    }

    tbody.innerHTML = "";
    courses.forEach(course => {
      const status = getCourseStatus(course);
      const row = document.createElement("tr");
      row.innerHTML = `
        <td>${course.id}</td>
        <td>${escapeHtml(course.title)}</td>
        <td>${escapeHtml(course.instructorName || "-")}</td>
        <td>${formatDate(course.registrationStartTime)} &rarr; ${formatDate(course.registrationEndTime)}</td>
        <td><span class="badge ${status.cls}">${status.label}</span></td>
      `;
      tbody.appendChild(row);
    });

  } catch (err) {
    tbody.innerHTML = `<tr><td colspan="5" class="message error">Failed to load: ${escapeHtml(err.message)}</td></tr>`;
  }
}

document.getElementById("createCourseForm").addEventListener("submit", async (e) => {
  e.preventDefault();
  const messageEl = document.getElementById("createMessage");

  const payload = {
    title: document.getElementById("title").value.trim(),
    description: document.getElementById("description").value.trim(),
    instructorName: document.getElementById("instructorName").value.trim(),
    registrationStartTime: document.getElementById("registrationStartTime").value,
    registrationEndTime: document.getElementById("registrationEndTime").value
  };

  try {
    await apiRequest(`${ADMIN_API_BASE}/courses`, {
      method: "POST",
      body: JSON.stringify(payload)
    });

    messageEl.textContent = "Course created successfully!";
    messageEl.className = "message success";
    e.target.reset();
    loadAdminCourses();

  } catch (err) {
    messageEl.textContent = err.message;
    messageEl.className = "message error";
  }
});

document.getElementById("refreshAdminCourses").addEventListener("click", loadAdminCourses);

// ---------------------------------------------------------
// Utility
// ---------------------------------------------------------
function escapeHtml(str) {
  const div = document.createElement("div");
  div.textContent = str ?? "";
  return div.innerHTML;
}

// ---------------------------------------------------------
// Initial load
// ---------------------------------------------------------
loadPublicCourses();
loadAdminCourses();
