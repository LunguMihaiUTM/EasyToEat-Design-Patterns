document.addEventListener("DOMContentLoaded", () => {
    const userBtn = document.getElementById("userBtn");
    const userMenu = document.getElementById("userMenu");
    const userDropdown = document.getElementById("userDropdown");
    const mobileMenuBtn = document.getElementById("mobileMenuBtn");
    const navbarMenu = document.getElementById("navbarMenu");
    const logoutBtn = document.getElementById("logoutBtn");

    if (userBtn) {
        userBtn.addEventListener("click", () => {
            userMenu.classList.toggle("active");
        });
    }

    if (mobileMenuBtn) {
        mobileMenuBtn.addEventListener("click", () => {
            navbarMenu.classList.toggle("active");
        });
    }

    document.addEventListener("click", (e) => {
        if (userMenu && !userMenu.contains(e.target) && userMenu.classList.contains("active")) {
            userMenu.classList.remove("active");
        }
    });

    const token = localStorage.getItem("token");
    const userNameSpan = document.getElementById("userName");

    if (token) {
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            const username = payload.sub;
            if (username && userNameSpan) {
                userNameSpan.textContent = username;
            }
        } catch (e) {
            // token invalid sau corupt
        }
    } else {
        const userMenu = document.getElementById("userMenu");
        const loginLink = document.createElement("a");
        loginLink.href = "/login";
        loginLink.className = "nav-link";
        loginLink.textContent = "Autentificare / Înregistrare";

        const navbarUser = document.querySelector(".navbar-user");
        if (userMenu) userMenu.remove();
        if (navbarUser) navbarUser.appendChild(loginLink);
    }

    if (logoutBtn) {
        logoutBtn.addEventListener("click", (e) => {
            e.preventDefault();
            localStorage.removeItem("token");
            window.location.href = "/home";
        });
    }

    // Funcție de actualizare counter coș
    function updateCartCount() {
        const cartCountElem = document.getElementById('cartCount');
        if (!cartCountElem) return;
        const cart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        const totalCount = cart.reduce((acc, item) => acc + item.quantity, 0);
        cartCountElem.textContent = totalCount;
    }

    updateCartCount();

    // Handler click pe butonul coșului, să ducă la pagina coșului
    const cartBtn = document.getElementById('cartBtn');
    if (cartBtn) {
        cartBtn.addEventListener('click', () => {
            window.location.href = '/cart';
        });
    }
});
