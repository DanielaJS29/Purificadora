(function() {
    const storageKey = 'purificadora-theme';
    let button = null;
    let label = null;

    function getPreferredTheme() {
        const storedTheme = localStorage.getItem(storageKey);
        if (storedTheme === 'light' || storedTheme === 'dark') {
            return storedTheme;
        }
        return window.matchMedia('(prefers-color-scheme: dark)').matches ? 'dark' : 'light';
    }

    function updateToggleLabel(theme) {
        if (!label) {
            return;
        }
        label.textContent = theme === 'dark' ? 'Modo claro' : 'Modo oscuro';
    }

    function applyTheme(theme) {
        document.body.classList.remove('light', 'dark');
        document.body.classList.add(theme);
        updateToggleLabel(theme);
        localStorage.setItem(storageKey, theme);
    }

    function toggleTheme() {
        const nextTheme = document.body.classList.contains('dark') ? 'light' : 'dark';
        applyTheme(nextTheme);
    }

    document.addEventListener('DOMContentLoaded', function() {
        button = document.getElementById('themeToggleBtn');
        label = document.getElementById('themeToggleLabel');
        applyTheme(getPreferredTheme());
        if (button) {
            button.addEventListener('click', toggleTheme);
        }
    });
})();
