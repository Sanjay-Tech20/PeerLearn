function getCurrentUser(){
    const data = localStorage.getItem('currentUser');
    return data ? JSON.parse(data) : null;
}
function setCurrentUser(user){
    localStorage.setItem('currentUser', JSON.stringify(user)); 
    renderAccountChip();
}
function logoutUser(){
    localStorage.removeItem('currentUser');
    renderAccountChip();
    alert("Logged out successfully!");
    window.location.href = 'index.html';
}
function renderAccountChip() {
    const chip = document.getElementById('account-chip');
    const postAsName = document.getElementById('post-as-name');
    const signupBtn = document.getElementById('nav-signup-btn');
    const logoutBtn = document.getElementById('nav-logout-btn');
    const user = getCurrentUser();

    if (chip) {
        chip.textContent = user ? `${user.name} (${user.role})` : 'Not signed in';
    }
    if (postAsName) {
        postAsName.textContent = user ? `${user.name} (${user.role})` : 'Not signed in';
    }
    if (signupBtn) {
        signupBtn.style.display = user ? 'none' : 'inline-block';
    }
    if (logoutBtn) {
        logoutBtn.style.display = user ? 'inline-block' : 'none';
    }
}

document.addEventListener('DOMContentLoaded', () => {
    renderAccountChip();

    if (document.getElementById('skills-list')) {
        loadSkills();
    }

    if (document.getElementById('skill-title')) {
        checkEditMode();
    }
});

const API_URL = "https://peerlearn-4y3k.onrender.com";


async function useExistingId() {
    const id = document.getElementById('quick-user-id').value;
    const response = await fetch(`${API_URL}/users/${id}`);

      if (response.ok) {
        const user = await response.json();
        setCurrentUser(user);
        alert(`Welcome Back ${user.name}`);
    } else {
        alert(`No user found with ID ${id}. Please check and try again.`);
    }
}

 async function registerUser() {
    const name = document.getElementById('register-name').value;
    const email = document.getElementById('register-email').value;
    const role = document.getElementById('register-role').value;

    const response = await fetch(`${API_URL}/users`,{
        method : 'POST',
        headers : {'Content-type' : 'application/json'},
        body : JSON.stringify({ name: name, email: email, role: role })
    });

    if(response.ok){
        const newUser = await response.json();
        setCurrentUser(newUser);
        alert(`Account created! Welcome, ${name}`);
        window.location.href = 'index.html';
    }
    else{
        const errordata = await response.json();
         alert(`Error: ${errordata.error}`);
    }
}

async function postSkillForm() {
      const currentUser = getCurrentUser();
    if (!currentUser) {
        alert("Please sign in first!");
        window.location.href = 'index.html';
        return;
    }
    const title = document.getElementById('skill-title').value;
    const category = document.getElementById('skill-category').value;
    const description = document.getElementById('skill-description').value;
    const mentorId = currentUser.id;

    let url = `${API_URL}/skills`;
    let method = 'POST';

    if (editingSkillId) {
        url = `${API_URL}/${editingSkillId}`;
        method = 'PUT';
    }

    const response = await fetch(url,{
        method : method,
        headers : {'Content-Type' : 'application/json'},
        body : JSON.stringify({ title: title, category: category, description: description, mentorId : mentorId })
    });
    if(response.ok){
        alert(editingSkillId ? "Skill updated successfully!" : "Skill posted successfully!");
        window.location.href = 'index.html';
    }
    else{
         const errordata = await response.json();
         alert(`Error: ${errordata.error}`);
    }
}

async function loadSkills() {
    const response = await fetch(`${API_URL}/skills`);
    const data = await response.json();
    renderSkills(data);
     updateStats(data);
}

function updateStats(data) {
    const statSkills = document.getElementById('stat-skills');
    const statMentors = document.getElementById('stat-mentors');

    if (statSkills) statSkills.textContent = data.length;

    if (statMentors) {
        const uniqueMentors = new Set(data.map(skill => skill.mentorId));
        statMentors.textContent = uniqueMentors.size;
    }
}


async function searchSkills() {
    const title = document.getElementById('search-input').value;
    const response = await fetch(`${API_URL}/skills/search?title=${title}`);
    const data = await response.json();
    renderSkills(data);
}

function renderSkills(data) {
    if (data.length === 0) {
        document.getElementById('skills-list').innerHTML = `<p class="empty-state">No skills found.</p>`;
        return;
    }

    let htmlCards = data.map((skill) => `
        <article class="skill-card">
            <div class="card-top">
                <span class="mono-tag">SKL-${skill.id}</span>
                <span class="pill">${skill.category}</span>
            </div>
            <h3>${skill.title}</h3>
            <p>${skill.description}</p>
            <div class="card-footer">
                <span>Mentor ID: ${skill.mentorId}</span>
                <span>Mentor: ${skill.mentorName}</span>
            </div>
            <button class="btn btn-outline btn-full" onclick="enrollInSkill(${skill.id})">Enroll</button>
        </article>
    `);

    document.getElementById('skills-list').innerHTML = htmlCards.join("");
}

async function enrollInSkill(skillId) {
     const currentUser = getCurrentUser();
    if (!currentUser) {
        alert("Please sign in first!");
        window.location.href = 'index.html';
        return;
    }
    const learner_id = currentUser.id;
    const skill_id = skillId;
    const response = await fetch(`${API_URL}/enrollments`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ learnerId: learner_id, skillId: skill_id })
    });
    if (response.ok) {
        alert(`Enrolled Successfully`);
        window.location.href = 'index.html';
    } else {
        const errordata = await response.json();
        alert(`Error: ${errordata.error}`);
    }
}

async function loadMySkills() {
    const currentUser = getCurrentUser();

    if (!currentUser) {
        document.getElementById('my-skills-list').innerHTML = `<p class="empty-state">Please sign in from the Home page to view your skills.</p>`;
        return;
    }
    if (currentUser.role !== 'MENTOR') {
        document.getElementById('my-skills-list').innerHTML = `<p class="empty-state">Only Mentors can post skills. Sign in with a Mentor account.</p>`;
        return;
    }

    const response = await fetch(`${API_URL}/skills`);
    const allSkills = await response.json();
    const mySkills = allSkills.filter((skill) => skill.mentorId === currentUser.id);
    renderMySkills(mySkills);
}

function renderMySkills(data) {
    if (data.length === 0) {
        document.getElementById('my-skills-list').innerHTML = `<p class="empty-state">You haven't posted any skills yet.</p>`;
        return;
    }

    let htmlCards = data.map((skill) => `
        <article class="skill-card">
            <div class="card-top">
                <span class="mono-tag">SKL-${skill.id}</span>
                <span class="pill">${skill.category}</span>
            </div>
            <h3>${skill.title}</h3>
            <p>${skill.description}</p>
            <div id="learners-${skill.id}" class="learners-box"></div>
            <div class="card-actions">
                <button class="btn btn-outline" onclick="viewLearners(${skill.id})">View Learners</button>
                <button class="btn btn-outline" onclick="location.href='post-skill.html?edit=${skill.id}'">Edit</button>
            </div>
        </article>
    `);

    document.getElementById('my-skills-list').innerHTML = htmlCards.join("");
}
function renderEnrollments(data) {
    if (data.length === 0) {
        document.getElementById('enrollments-list').innerHTML = `<p class="empty-state">No enrollments yet.</p>`;
        return;
    }

    let enrollmentsHTML = data.map((enrollment) => `
        <div class="enrollment-item">
            <div class="enrollment-main">
                <span class="skill-name">${enrollment.skillTitle}</span>
                <span class="mono-tag">Enrolled at ${formatDate(enrollment.enrolledAt)}</span>
            </div>
            <button class="btn btn-danger" onclick="unenrollSkill(${enrollment.id})">Unenroll</button>
        </div>
    `);

    document.getElementById('enrollments-list').innerHTML = enrollmentsHTML.join("");
}

async function loadMyEnrollments() {
    const currentUser = getCurrentUser();
      if (!currentUser) {
        document.getElementById('enrollments-list').innerHTML = `<p class="empty-state">Please sign in from the Home page to view your enrollments.</p>`;
        return;
    }
    if (currentUser.role !== 'LEARNER') {
        document.getElementById('enrollments-list').innerHTML = `<p class="empty-state">Only Learners can enroll in skills. Sign in with a Learner account.</p>`;
        return;
    }
    const response = await fetch(`${API_URL}/enrollments/learner/${currentUser.id}`);
    const data = await response.json();
    renderEnrollments(data);
}
async function unenrollSkill(enrollmentId) {
    const confirmUnenroll = confirm("Are you sure you want to unenroll?");
    if (!confirmUnenroll) return;

    const response = await fetch(`${API_URL}/enrollments/${enrollmentId}`, {
        method: 'DELETE'
    });

    if (response.ok) {
        alert("Unenrolled successfully!");
        loadMyEnrollments();
    } else {
        const errordata = await response.json();
        alert(`Error: ${errordata.error}`);
    }
}
async function viewLearners(skillId) {
    const response = await  fetch(`${API_URL}/enrollments/skill/${skillId}`);
    const data = await response.json();
    const box = document.getElementById(`learners-${skillId}`);
     if (data.length === 0) {
        box.innerHTML = `<p class="empty-state">No learners enrolled yet.</p>`;
        return;
    }
    const namesHTML = data.map((enrollment) => `<span class="pill">${enrollment.learnerName}</span>`).join(" ");
    box.innerHTML = namesHTML;
}
let editingSkillId = null;
function checkEditMode() {
    const params = new URLSearchParams(window.location.search);
    const editId = params.get('edit');

    if(editId){
        editingSkillId = editId;
        prefillSkillForm(editId);
    }
}
async function prefillSkillForm(skillId) {
    const response = await fetch(`${API_URL}/skills/${skillId}`);
    const skill = await response.json();


    document.getElementById('skill-title').value = skill.title;
    document.getElementById('skill-category').value = skill.category;
    document.getElementById('skill-description').value = skill.description;
}
function formatDate(dateStr) {
    if (!dateStr) return '';
    const d = new Date(dateStr.replace(' ', 'T'));
    return d.toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' });
}
