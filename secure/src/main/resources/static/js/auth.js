// Insecure authentication handling
class AuthService {
    constructor() {
        this.baseUrl = '/api';
        this.setupEventListeners();
    }

    setupEventListeners() {
        document.getElementById('loginBtn').addEventListener('click', () => this.showLoginForm());
        document.getElementById('logoutBtn').addEventListener('click', () => this.logout());
        document.getElementById('loginFormElement').addEventListener('submit', (e) => this.handleLogin(e));
    }

    async handleLogin(e) {
        e.preventDefault();
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch(`${this.baseUrl}/login`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ username, password })
            });

            if (!response.ok) {
                throw new Error('Login failed');
            }

            const data = await response.json();
            this.handleSuccessfulLogin(data);
            commentService.loadComments();
        } catch (error) {
            console.error('Login error:', error);
            alert('Login failed. Please try again.');
        }
    }

    async logout() {
        try {
            const response = await fetch(`${this.baseUrl}/logout`, {
                method: 'POST'
            });

            if (!response.ok) {
                throw new Error('Logout failed');
            }

            this.handleSuccessfulLogout();
        } catch (error) {
            console.error('Logout error:', error);
        }
    }

    handleSuccessfulLogin(data) {
        document.getElementById('loginForm').classList.add('d-none');
        document.getElementById('mainContent').classList.remove('d-none');
        document.getElementById('loginBtn').classList.add('d-none');
        document.getElementById('logoutBtn').classList.remove('d-none');
        document.getElementById('userInfo').textContent = `Welcome, ${data.username}`;
    }

    handleSuccessfulLogout() {
        document.getElementById('loginForm').classList.add('d-none');
        document.getElementById('mainContent').classList.add('d-none');
        document.getElementById('loginBtn').classList.remove('d-none');
        document.getElementById('logoutBtn').classList.add('d-none');
        document.getElementById('userInfo').textContent = '';
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
    }

    showLoginForm() {
        document.getElementById('loginForm').classList.remove('d-none');
    }
}

// Initialize auth service
const authService = new AuthService(); 