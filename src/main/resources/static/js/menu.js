document.addEventListener('DOMContentLoaded', () => {
    const urlParams = new URLSearchParams(window.location.search);
    const restaurantId = urlParams.get('id');
    if (!restaurantId) {
        document.getElementById('menuItems').innerHTML = '<p>Id restaurant invalid.</p>';
        return;
    }

    const menuContainer = document.getElementById('menuItems');
    const restaurantNameElem = document.getElementById('restaurantName');

    // Mai întâi ia datele restaurantului ca să afișezi numele corect
    fetch(`/restaurant-resource/restaurant-by-id?restaurantId=${restaurantId}`)
        .then(res => {
            if (!res.ok) throw new Error('Restaurantul nu a fost găsit');
            return res.json();
        })
        .then(restaurant => {
            restaurantNameElem.textContent = `Meniu ${restaurant.restaurantName}`;
            fetchMenu(restaurantId);
        })
        .catch(err => {
            restaurantNameElem.textContent = `Meniu Restaurant #${restaurantId}`;
            fetchMenu(restaurantId);
        });

    async function fetchMenu(id) {
        try {
            const res = await fetch(`/restaurant-resource/menu/${id}`);
            if (!res.ok) throw new Error('Eroare la încărcarea meniului');
            const menu = await res.json();

            if (!menu || !menu.items || !menu.items.length) {
                menuContainer.innerHTML = '<p>Nu există produse în meniul acestui restaurant.</p>';
                return;
            }

            displayMenuItems(menu.items);
        } catch (e) {
            menuContainer.innerHTML = `<p style="color:#e74c3c;">${e.message}</p>`;
        }
    }

    function displayMenuItems(items) {
        menuContainer.innerHTML = items.map(item => {
            const availableClass = item.isAvailable ? '' : 'unavailable';
            const restaurantFolder = encodeURIComponent(restaurantNameElem.textContent.replace(/^Meniu\s/, '').replace(/\s+/g, '_'));
            const imagePath = `images/${restaurantFolder}/${item.image || 'default.jpg'}`;

            // Butonul adaugat aici
            return `
            <div class="menu-item ${availableClass}">
                <img src="${imagePath}" alt="${item.dishName}" />
                <h3>${item.dishName}</h3>
                <p class="description">${item.description || ''}</p>
                <p class="price">${item.price.toFixed(2)} MDL</p>
                ${item.isAvailable ? `<button class="add-to-cart-btn" data-id="${item.id}" data-name="${item.dishName}" data-price="${item.price}">Adaugă în coș</button>` : '<p>Produs indisponibil</p>'}
            </div>
        `;
        }).join('');

        // După ce adaugi elementele în DOM, setează handler pe butoane
        const buttons = document.querySelectorAll('.add-to-cart-btn');
        buttons.forEach(btn => {
            btn.addEventListener('click', addToCart);
        });
    }


    // Încarcă coșul din localStorage sau creează gol
    let cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];

// Funcție de actualizare counter în navbar
    function updateCartCount() {
        const cartCountElem = document.getElementById('cartCount');
        if (!cartCountElem) return;
        const totalCount = cart.reduce((acc, item) => acc + item.quantity, 0);
        cartCountElem.textContent = totalCount;
    }

// Funcție adaugă produs în coș
    function addToCart(event) {
        const btn = event.currentTarget;
        const id = btn.dataset.id;
        const name = btn.dataset.name;
        const price = parseFloat(btn.dataset.price);

        // Verifică dacă produsul există deja în coș
        const existing = cart.find(item => item.id === id);
        if (existing) {
            existing.quantity++;
        } else {
            cart.push({id, name, price, quantity: 1});
        }

        // Salvează coșul
        localStorage.setItem('easyToEatCart', JSON.stringify(cart));

        // Actualizează counter
        updateCartCount();
    }

// La încărcare pagină updatează counter
    updateCartCount();


});
