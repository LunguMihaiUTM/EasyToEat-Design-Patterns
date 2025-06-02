document.addEventListener("DOMContentLoaded", async () => {
    const token = localStorage.getItem("token");
    const itemIds = JSON.parse(localStorage.getItem("cartItemIds") || "[]");
    const orderItemsContainer = document.getElementById("orderItems");
    const subtotalElement = document.getElementById("subtotal");
    const finalTotalElement = document.getElementById("finalTotal");
    const locationSelect = document.getElementById("location");
    const checkoutForm = document.getElementById("checkoutForm");

    let selectedTableId = null;

    function isTokenExpired(token) {
        if (!token) return true;
        try {
            const payload = JSON.parse(atob(token.split('.')[1]));
            return payload.exp * 1000 < Date.now();
        } catch {
            return true;
        }
    }

    if (isTokenExpired(token)) {
        alert("Sesiunea a expirat. Te rugăm să te autentifici din nou.");
        localStorage.removeItem("token");
        localStorage.removeItem("role");
        window.location.href = "/login";
        return;
    }

    const verifyRes = await fetch(`/restaurant-resource/verify-cart?${itemIds.map(id => `itemIds=${id}`).join("&")}`);
    const isSameRestaurant = await verifyRes.json();

    if (!isSameRestaurant) {
        alert("Toate produsele din coș trebuie să fie de la același restaurant.");
        window.location.href = "/cart";
        return;
    }

    const locRes = await fetch(`/restaurant-resource/locations-by-item?itemId=${itemIds[0]}`);
    const locations = await locRes.json();
    locations.forEach(loc => {
        const opt = document.createElement("option");
        opt.value = loc.id;
        opt.textContent = loc.address;
        locationSelect.appendChild(opt);
    });

    locationSelect.addEventListener("change", async () => {
        const locId = locationSelect.value;
        if (!locId) return;

        const res = await fetch(`/restaurant-resource/tables/${locId}`);
        const tables = await res.json();

        if (tables.length === 0) {
            alert("Nu sunt mese libere la această locație.");
            selectedTableId = null;
        } else {
            selectedTableId = tables[0].id;
            console.log("Masa selectată:", selectedTableId);
        }
    });

    let total = 0;
    const fakeItems = JSON.parse(localStorage.getItem("cartItemsDetails") || "[]");
    fakeItems.forEach(item => {
        const itemEl = document.createElement("div");
        itemEl.className = "order-item";
        itemEl.innerHTML = `<span>${item.name}</span><span>${item.price.toFixed(2)} MDL</span>`;
        orderItemsContainer.appendChild(itemEl);
        total += item.price;
    });

    subtotalElement.textContent = `${total.toFixed(2)} MDL`;
    finalTotalElement.textContent = `${total.toFixed(2)} MDL`;

    document.getElementById("backToCartBtn").addEventListener("click", () => {
        window.location.href = "/cart";
    });

    const cardModal = document.getElementById("cardModal");
    const closeModal = document.querySelector(".close-modal");
    const cancelCardBtn = document.getElementById("cancelCardBtn");

    document.getElementById("card").addEventListener("change", () => {
        cardModal.style.display = "block";
    });

    document.getElementById("cash").addEventListener("change", () => {
        cardModal.style.display = "none";
    });

    closeModal.onclick = cancelCardBtn.onclick = () => {
        cardModal.style.display = "none";
        document.getElementById("cash").checked = true;
    };

    document.getElementById("cardForm").addEventListener("submit", (e) => {
        e.preventDefault();
        cardModal.style.display = "none";
    });

    checkoutForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const currentToken = localStorage.getItem("token");
        if (isTokenExpired(currentToken)) {
            alert("Sesiunea a expirat. Te rugăm să te autentifici din nou.");
            localStorage.removeItem("token");
            localStorage.removeItem("role");
            window.location.href = "/login";
            return;
        }

        if (!selectedTableId) {
            alert("Nu există masă disponibilă pentru locația selectată.");
            return;
        }

        const bookingDTO = {
            noPeople: +document.getElementById("noPeople").value,
            preferences: document.getElementById("preferences").value,
            locationId: +document.getElementById("location").value,
            tableId: selectedTableId,
            itemIds: itemIds.map(Number),
            bookingDate: document.getElementById("bookingDate").value
        };

        const promoCode = document.getElementById("promoCode").value;
        const paymentRadio = document.querySelector("input[name='paymentType']:checked");
        if (!paymentRadio) {
            alert("Selectează o metodă de plată!");
            return;
        }
        const paymentType = paymentRadio.value;

        document.getElementById("loadingOverlay").style.display = "flex";

        try {
            const res = await fetch(`/booking/create?promoCode=${encodeURIComponent(promoCode)}&paymentType=${encodeURIComponent(paymentType)}`, {
                method: "POST",
                headers: {
                    'Authorization': 'Bearer ' + currentToken,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bookingDTO)
            });

            if (!res.ok) throw new Error("Eroare la trimiterea comenzii.");
            await res.text();

            alert("Rezervare creată cu succes!");
            localStorage.removeItem("cartItemIds");
            localStorage.removeItem("cartItemsDetails");
            window.location.href = "/home";
        } catch (err) {
            console.error(err);
            alert("Eroare la procesarea rezervării.");
        } finally {
            document.getElementById("loadingOverlay").style.display = "none";
        }
    });
});
