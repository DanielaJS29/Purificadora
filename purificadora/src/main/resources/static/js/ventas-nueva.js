const productos = Array.from(document.querySelectorAll('#productoSelect option'))
    .filter(option => option.value !== '');

const precioUnitarioInput = document.getElementById('precioUnitario');
const cantidadInput = document.getElementById('cantidadProducto');
const subtotalInput = document.getElementById('subtotalProducto');
const productoSelect = document.getElementById('productoSelect');
const agregarBtn = document.getElementById('agregarProductoBtn');
const cartBody = document.getElementById('cartBody');
const totalGeneral = document.getElementById('totalGeneral');
const limpiarCarritoBtn = document.getElementById('limpiarCarritoBtn');
const ventaForm = document.getElementById('ventaForm');

let cartItems = [];

function actualizarSubtotal() {
    const precio = parseFloat(precioUnitarioInput.value) || 0;
    const cantidad = parseInt(cantidadInput.value) || 0;
    subtotalInput.value = (precio * cantidad).toFixed(2);
}

function actualizarTotal() {
    const total = cartItems.reduce((sum, item) => sum + parseFloat(item.subtotal), 0);
    totalGeneral.textContent = total.toFixed(2);
}

function renderCart() {
    cartBody.innerHTML = '';
    cartItems.forEach((item, index) => {
        const row = document.createElement('tr');
        row.className = 'rounded-2xl bg-purple-50/50 clay-row';
        row.innerHTML = `
            <td class="rounded-l-2xl px-4 py-4 font-bold text-indigo-900">${item.nombre}</td>
            <td class="px-4 py-4 font-medium text-slate-700">${item.cantidad}</td>
            <td class="px-4 py-4 font-medium text-slate-700">${parseFloat(item.precioUnitario).toFixed(2)}</td>
            <td class="px-4 py-4 font-bold text-indigo-900">${parseFloat(item.subtotal).toFixed(2)}</td>
            <td class="rounded-r-2xl px-4 py-4">
                <button type="button" data-index="${index}" class="remove-item inline-flex items-center justify-center rounded-full bg-rose-500 px-4 py-2 text-xs font-bold text-white transition clay-btn-danger">Eliminar</button>
            </td>
        `;
        cartBody.appendChild(row);
    });
    actualizarTotal();
    attachRemoveButtons();
}

function attachRemoveButtons() {
    document.querySelectorAll('.remove-item').forEach(button => {
        button.addEventListener('click', function () {
            const index = parseInt(this.dataset.index, 10);
            cartItems.splice(index, 1);
            renderCart();
        });
    });
}

productoSelect.addEventListener('change', () => {
    const selected = productoSelect.selectedOptions[0];
    const precio = parseFloat(selected.dataset.precio || 0);
    precioUnitarioInput.value = precio.toFixed(2);
    actualizarSubtotal();
});

cantidadInput.addEventListener('input', actualizarSubtotal);

agregarBtn.addEventListener('click', () => {
    const selected = productoSelect.selectedOptions[0];
    if (!selected || !selected.value) return;

    const item = {
        productoId: selected.value,
        nombre: selected.textContent,
        cantidad: parseInt(cantidadInput.value, 10) || 0,
        precioUnitario: parseFloat(precioUnitarioInput.value) || 0,
        subtotal: parseFloat(subtotalInput.value) || 0
    };

    if (item.cantidad <= 0 || item.precioUnitario <= 0) return;

    cartItems.push(item);
    renderCart();
});

limpiarCarritoBtn.addEventListener('click', () => {
    cartItems = [];
    renderCart();
});

ventaForm.addEventListener('submit', function (event) {
    cartItems.forEach(item => {
        const productoInput = document.createElement('input');
        productoInput.type = 'hidden';
        productoInput.name = 'productoId';
        productoInput.value = item.productoId;
        productoInput.classList.add('cart-input');

        const cantidadInputHidden = document.createElement('input');
        cantidadInputHidden.type = 'hidden';
        cantidadInputHidden.name = 'cantidad';
        cantidadInputHidden.value = item.cantidad;
        cantidadInputHidden.classList.add('cart-input');

        const precioInput = document.createElement('input');
        precioInput.type = 'hidden';
        precioInput.name = 'precioUnitario';
        precioInput.value = item.precioUnitario;
        precioInput.classList.add('cart-input');

        const subtotalInputHidden = document.createElement('input');
        subtotalInputHidden.type = 'hidden';
        subtotalInputHidden.name = 'subtotal';
        subtotalInputHidden.value = item.subtotal;
        subtotalInputHidden.classList.add('cart-input');

        ventaForm.appendChild(productoInput);
        ventaForm.appendChild(cantidadInputHidden);
        ventaForm.appendChild(precioInput);
        ventaForm.appendChild(subtotalInputHidden);
    });

    if (cartItems.length === 0) {
        event.preventDefault();
        alert('Agrega al menos un producto al carrito antes de guardar la venta.');
    }
});
