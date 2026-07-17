(function () {
    var STORAGE_KEY = 'sidebarCollapsed';
    var body = document.body;
    var root = document.documentElement;
    var toggleBtn = document.getElementById('sidebarToggleBtn');
    var toggleLabel = toggleBtn ? toggleBtn.querySelector('.sidebar-text') : null;

    if (!toggleBtn) return;

    function applyState(collapsed) {
        if (body) {
            body.classList.toggle('sidebar-collapsed', collapsed);
        }
        root.classList.toggle('sidebar-collapsed', collapsed);
        toggleBtn.setAttribute('aria-expanded', String(!collapsed));
        toggleBtn.title = collapsed ? 'Expandir menú' : 'Colapsar menú';
        if (toggleLabel) {
            toggleLabel.textContent = collapsed ? 'Expandir' : 'Colapsar';
        }
    }

    // Restaura el estado guardado (por defecto: expandido)
    applyState(localStorage.getItem(STORAGE_KEY) === 'true');

    toggleBtn.addEventListener('click', function () {
        var collapsed = !root.classList.contains('sidebar-collapsed');
        applyState(collapsed);
        localStorage.setItem(STORAGE_KEY, String(collapsed));
    });
})();