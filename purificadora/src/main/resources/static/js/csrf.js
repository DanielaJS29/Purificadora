// Helper to read CSRF meta tags and provide a fetch wrapper that sets the CSRF header.
function getCsrf() {
    const tokenMeta = document.querySelector('meta[name="_csrf"]');
    const headerMeta = document.querySelector('meta[name="_csrf_header"]');
    return {
        token: tokenMeta ? tokenMeta.getAttribute('content') : null,
        header: headerMeta ? headerMeta.getAttribute('content') : null
    };
}

async function csrfFetch(url, options = {}) {
    const csrf = getCsrf();
    options.headers = options.headers || {};
    if (csrf && csrf.token && csrf.header) {
        options.headers[csrf.header] = csrf.token;
    }
    return fetch(url, options);
}

// Expose helpers
window.getCsrf = getCsrf;
window.csrfFetch = csrfFetch;
