// Secure comment handling
class CommentService {
    constructor() {
        this.baseUrl = '/api/users/comments';
        this.setupEventListeners();
        this.loadComments();
    }

    setupEventListeners() {
        document.getElementById('commentForm').addEventListener('submit', (e) => this.handleCommentSubmit(e));
    }

    async handleCommentSubmit(e) {
        e.preventDefault();
        const comment = document.getElementById('comment').value;

        try {
            const response = await fetch(this.baseUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({ comment })
            });

            if (!response.ok) {
                throw new Error('Failed to post comment');
            }

            document.getElementById('comment').value = '';
            this.loadComments();
        } catch (error) {
            console.error('Comment submission error:', error);
            alert('Failed to post comment. Please try again.');
        }
    }

    async loadComments() {
        try {
            const response = await fetch(this.baseUrl, {
                credentials: 'include'
            });

            if (!response.ok) {
                throw new Error('Failed to load comments');
            }

            const comments = await response.json();
            this.displayComments(comments);
        } catch (error) {
            console.error('Comment loading error:', error);
        }
    }

    displayComments(comments) {
        const commentsList = document.getElementById('commentsList');
        commentsList.innerHTML = comments.map(comment => `
            <div class="comment">
                <div class="comment-header">
                    <span class="comment-username">${this.escapeHtml(comment.username)}</span>
                    <span class="comment-timestamp">${new Date().toLocaleString()}</span>
                </div>
                <div class="comment-content">${this.escapeHtml(comment.comment)}</div>
            </div>
        `).join('');
    }

    escapeHtml(unsafe) {
        return unsafe
            .replace(/&/g, "&amp;")
            .replace(/</g, "&lt;")
            .replace(/>/g, "&gt;")
            .replace(/"/g, "&quot;")
            .replace(/'/g, "&#039;");
    }
}

// Initialize comment service
const commentService = new CommentService(); 