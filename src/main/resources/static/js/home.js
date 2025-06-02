document.addEventListener('DOMContentLoaded', () => {
    const categoriesGrid = document.getElementById('categoriesGrid');
    const searchInput = document.querySelector('.search-input');
    const searchBtn = document.querySelector('.search-btn');

    // Container pentru dropdown-ul de rezultate restaurante
    let dropdown = null;

    const categoryIcons = {
        'Pizza': '🍕',
        'Traditional': '🍲',
        'Burger': '🍔',
        'Asian': '🥢',
        'Kebab': '🥙'
    };

    const categoryDescriptions = {
        'Pizza': 'Delicioase pizza-uri cu ingrediente fresh',
        'Traditional': 'Mâncăruri tradiționale românești',
        'Burger': 'Burgeri suculenți și crispy',
        'Asian': 'Bucătărie asiatică autentică',
        'Kebab': 'Kebab-uri aromate și savuroase'
    };

    loadCategories();

    searchBtn?.addEventListener('click', performSearch);
    searchInput?.addEventListener('input', () => {
        if (searchInput.value.trim()) {
            performSearch();
        } else {
            removeDropdown();
        }
    });


    async function loadCategories() {
        try {
            const res = await fetch('/category-manager', {
                headers: { 'Content-Type': 'application/json' }
            });
            if (!res.ok) throw new Error();
            const categories = await res.json();
            displayCategories(categories);
        } catch {
            categoriesGrid.innerHTML = `
            <div class="category-loading" style="color:#e74c3c;">
                Eroare la încărcarea categoriilor. 
                <button onclick="location.reload()" style="
                    background:#FF5C00;color:#fff;border:none;padding:8px 16px;border-radius:4px;cursor:pointer;margin-left:10px;">
                    Încearcă din nou
                </button>
            </div>`;
        }
    }

    function displayCategories(categories) {
        if (!categories?.length) {
            categoriesGrid.innerHTML = '<div class="category-loading">Nu s-au găsit categorii.</div>';
            return;
        }

        categoriesGrid.innerHTML = categories.map(cat => {
            const icon = categoryIcons[cat.categoryName] || '🍽️';
            const desc = categoryDescriptions[cat.categoryName] || 'Descoperă mâncăruri delicioase';
            return `
                <div class="category-card" data-category-id="${cat.id}">
                    <div class="category-icon">${icon}</div>
                    <div class="category-name">${cat.categoryName}</div>
                    <div class="category-description">${desc}</div>
                </div>`;
        }).join('');

        // document.querySelectorAll('.category-card').forEach(card => {
        //     card.onclick = () => {
        //         const id = +card.dataset.categoryId;
        //         getRestaurants({ categoryIds: [id] });
        //         removeDropdown();
        //     };
        // });
    }

    function performSearch() {
        const query = searchInput.value.trim();
        if (query) {
            getRestaurants({ restaurantName: query });
        } else {
            removeDropdown();
        }
    }

    async function getRestaurants(filter) {
        if (!filter.categoryIds) filter.categoryIds = [];

        try {
            const res = await fetch('/restaurant-resource', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(filter)
            });
            if (!res.ok) throw new Error();

            const restaurants = await res.json();

            if (filter.restaurantName) {
                showDropdown(restaurants);
            } else {
                // Dacă se caută după categorie, afișăm restaurantele ca înainte
                displayRestaurants(restaurants);
                removeDropdown();
            }
        } catch {
            categoriesGrid.innerHTML = '<div class="category-loading" style="color:#e74c3c;">Eroare la încărcarea restaurantelor.</div>';
        }
    }

    function displayRestaurants(restaurants) {
        if (!restaurants?.length) {
            categoriesGrid.innerHTML = '<div class="category-loading">Nu s-au găsit restaurante.</div>';
            return;
        }

        categoriesGrid.innerHTML = restaurants.map(r => `
        <div class="restaurant-card" data-restaurant-id="${r.id}" style="cursor:pointer;">
            <img src="${r.logo}" alt="${r.restaurantName}" class="restaurant-logo">
            <div class="restaurant-info">
                <h3>${r.restaurantName}</h3>
                <p>${r.locations.map(loc => loc.city + ', ' + loc.address).join('<br>')}</p>
            </div>
        </div>
    `).join('');

        document.querySelectorAll('.restaurant-card').forEach(card => {
            card.addEventListener('click', () => {
                const id = card.dataset.restaurantId;
                window.location.href = `/menu.html?restaurantId=${id}`;
            });
        });
    }

    function showDropdown(restaurants) {
        removeDropdown();

        if (!restaurants?.length) return;

        dropdown = document.createElement('div');
        dropdown.classList.add('search-dropdown');
        dropdown.style.position = 'absolute';
        dropdown.style.background = 'white';
        dropdown.style.border = '1px solid #ddd';
        dropdown.style.borderRadius = '5px';
        dropdown.style.maxHeight = '250px';
        dropdown.style.overflowY = 'auto';
        dropdown.style.width = searchInput.offsetWidth + 'px';
        dropdown.style.zIndex = 1000;
        dropdown.style.top = (searchInput.getBoundingClientRect().bottom + window.scrollY) + 'px';
        dropdown.style.left = (searchInput.getBoundingClientRect().left + window.scrollX) + 'px';

        dropdown.innerHTML = restaurants.map(r => `
        <div class="dropdown-item" data-restaurant-id="${r.id}" style="padding: 10px; cursor: pointer; border-bottom: 1px solid #eee;">
            ${r.restaurantName}
        </div>
    `).join('');

        document.body.appendChild(dropdown);

        dropdown.querySelectorAll('.dropdown-item').forEach(item => {
            item.addEventListener('click', () => {
                const id = item.dataset.restaurantId;
                window.location.href = `/menu?id=${id}`;
            });
        });
    }

    function removeDropdown() {
        if (dropdown) {
            dropdown.remove();
            dropdown = null;
        }
    }

    // Ajustare poziție dropdown la scroll sau resize
    window.addEventListener('scroll', () => {
        if (dropdown) {
            dropdown.style.top = (searchInput.getBoundingClientRect().bottom + window.scrollY) + 'px';
            dropdown.style.left = (searchInput.getBoundingClientRect().left + window.scrollX) + 'px';
        }
    });
    window.addEventListener('resize', () => {
        if (dropdown) {
            dropdown.style.width = searchInput.offsetWidth + 'px';
            dropdown.style.top = (searchInput.getBoundingClientRect().bottom + window.scrollY) + 'px';
            dropdown.style.left = (searchInput.getBoundingClientRect().left + window.scrollX) + 'px';
        }
    });
});
