document.addEventListener('DOMContentLoaded', () => {
    const cartContainer = document.getElementById('cartContainer');
    const cartTotalElem = document.getElementById('cartTotal');
    const checkoutBtn = document.getElementById('checkoutBtn');

    function updateCartCount() {
        const cartCountElem = document.getElementById('cartCount');
        if (!cartCountElem) return;
        const cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        const totalCount = cart.reduce((acc, item) => acc + item.quantity, 0);
        cartCountElem.textContent = totalCount;
    }

    function loadCart() {
        const cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        renderCart(cart);
        updateCartCount();
    }

    function renderCart(cart) {
        if (cart.length === 0) {
            cartContainer.innerHTML = '<p>Coșul este gol.</p>';
            cartTotalElem.textContent = '0.00';
            checkoutBtn.disabled = true;
            return;
        }
        checkoutBtn.disabled = false;
        cartContainer.innerHTML = '';
        let total = 0;
        cart.forEach(item => {
            total += item.price * item.quantity;
            const itemElem = document.createElement('div');
            itemElem.classList.add('cart-item');
            itemElem.innerHTML = `
                <div class="cart-item-info">
                    <div class="cart-item-name">${item.name}</div>
                    <div class="cart-item-quantity">
                        Cantitate: 
                        <button class="quantity-btn" data-id="${item.id}" data-action="decrease">−</button>
                        <span class="quantity-value">${item.quantity}</span>
                        <button class="quantity-btn" data-id="${item.id}" data-action="increase">+</button>
                    </div>
                </div>
                <div class="cart-item-price">${(item.price * item.quantity).toFixed(2)} MDL</div>
                <button class="remove-btn" data-id="${item.id}" title="Șterge produs">×</button>
            `;
            cartContainer.appendChild(itemElem);
        });
        cartTotalElem.textContent = total.toFixed(2);

        // Handler pentru butoane cantitate +
        document.querySelectorAll('.quantity-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = e.target.dataset.id;
                const action = e.target.dataset.action;
                changeQuantity(id, action);
            });
        });

        // Handler pentru ștergere produs
        document.querySelectorAll('.remove-btn').forEach(btn => {
            btn.addEventListener('click', (e) => {
                const id = e.target.dataset.id;
                removeFromCart(id);
            });
        });
    }

    function changeQuantity(id, action) {
        let cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        cart = cart.map(item => {
            if (item.id == id) {
                if (action === 'increase') {
                    item.quantity++;
                } else if (action === 'decrease' && item.quantity > 1) {
                    item.quantity--;
                }
            }
            return item;
        });
        localStorage.setItem('easyToEatCart', JSON.stringify(cart));
        loadCart();
    }

    function removeFromCart(id) {
        let cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        cart = cart.filter(item => item.id != id);
        localStorage.setItem('easyToEatCart', JSON.stringify(cart));
        loadCart();
    }

    checkoutBtn.addEventListener('click', () => {
        const cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        if (cart.length === 0) {
            alert('Coșul este gol!');
            return;
        }

        // Salvăm itemIds și detaliile în localStorage pentru finalize-order
        const itemIds = cart.map(item => item.id);
        localStorage.setItem('cartItemIds', JSON.stringify(itemIds));
        localStorage.setItem('cartItemsDetails', JSON.stringify(cart));

        // Redirect la pagina de checkout
        window.location.href = '/finalize-order';
    });

    loadCart();
});
