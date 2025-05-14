// Insecure comment handling
class CommentService {
    constructor() {
        this.baseUrl = '/api/users/comments';
        this.setupEventListeners();
        this.loadComments(true);
    }

    setupEventListeners() {
//        document.getElementById('commentForm').addEventListener('submit', this.handleCommentSubmit.bind(this));
    }

    async handleCommentSubmit(e) {
        e.preventDefault();
        const comment = document.getElementById('comment').value;
        const imageUrl = document.getElementById('imageUrl').value;

        try {
            const response = await fetch(this.baseUrl, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ comment, imageUrl })
            });

            if (!response.ok) {
                throw new Error('Failed to post comment');
            }

            document.getElementById('comment').value = '';
            document.getElementById('imageUrl').value = '';
            this.loadComments();
        } catch (error) {
            console.error('Comment submission error:', error);
            alert('Failed to post comment. Please try again.');
        }
    }

    async loadComments(shouldHandleSuccessfulLogin = false) {
        try {
            const response = await fetch(this.baseUrl);

            if (!response.ok) {
                authService.handleSuccessfulLogout();
                throw new Error('Failed to load comments');
            }

            if (shouldHandleSuccessfulLogin) {
                authService.handleSuccessfulLogin({ username: 'User' });
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
                    <span class="comment-username">${comment.username}</span>
                    <span class="comment-timestamp">${new Date(comment.createdAt).toLocaleString()}</span>
                </div>
                <div class="comment-content" id="comment-${comment.id}"></div>
                ${this.deferCommentContent(comment)}
                ${comment.imageData ? `
                    <div class="comment-image">
                        <img src="data:image/jpeg;base64,${comment.imageData}" 
                             alt="Comment image" 
                             style="max-width: 200px; margin-top: 10px;">
                    </div>
                ` : ''}
            </div>
        `).join('');
    }

    deferCommentContent(comment) {
        setTimeout(() => {
            const container = document.getElementById(`comment-${comment.id}`);
            container.appendChild(document.createRange().createContextualFragment(comment.comment));
        }, 0);
        return '';
    }
}

// Initialize comment service
const commentService = new CommentService(); 