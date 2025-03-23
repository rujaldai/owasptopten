// Secure main application initialization
document.addEventListener('DOMContentLoaded', () => {
    // Check if user is already authenticated
    fetch('/api/auth/validate', {
        credentials: 'include'
    })
    .then(response => {
        if (response.ok) {
            authService.handleSuccessfulLogin({ username: 'User' });
        }
    })
    .catch(error => {
        console.error('Auth validation error:', error);
    });

    // Add security headers to all fetch requests
    const originalFetch = window.fetch;
    window.fetch = function(url, options = {}) {
        options.headers = {
            ...options.headers,
            'X-Requested-With': 'XMLHttpRequest'
        };
        return originalFetch(url, options);
    };
}); 