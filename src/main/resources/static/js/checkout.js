// checkout.js

document.addEventListener('DOMContentLoaded', () => {
    // Elemte DOM
    const checkoutForm = document.getElementById('checkoutForm');
    const locationSelect = document.getElementById('location');
    const paymentTypeRadios = document.querySelectorAll('input[name="paymentType"]');
    const cardModal = document.getElementById('cardModal');
    const cardForm = document.getElementById('cardForm');
    const closeModalBtn = document.querySelector('.close-modal');
    const cancelCardBtn = document.getElementById('cancelCardBtn');
    const applyPromoBtn = document.getElementById('applyPromoBtn');
    const backToCartBtn = document.getElementById('backToCartBtn');
    const loadingOverlay = document.getElementById('loadingOverlay');

    // Variabile pentru stocarea datelor
    let currentCart = [];
    let currentLocations = [];
    let appliedPromo = null;
    let cardDetails = null;

    // Inițializare pagină
    init();

    function init() {
        loadCartData();
        setupEventListeners();
        verifyAndLoadLocations();
        setMinDateTime();
        populateOrderSummary();
    }

    function setupEventListeners() {
        // Form submission
        checkoutForm.addEventListener('submit', handleCheckoutSubmit);

        // Payment method change
        paymentTypeRadios.forEach(radio => {
            radio.addEventListener('change', handlePaymentMethodChange);
        });

        // Modal events
        closeModalBtn.addEventListener('click', closeCardModal);
        cancelCardBtn.addEventListener('click', closeCardModal);
        cardForm.addEventListener('submit', handleCardFormSubmit);

        // Window click to close modal
        window.addEventListener('click', (e) => {
            if (e.target === cardModal) {
                closeCardModal();
            }
        });

        // Promo code
        applyPromoBtn.addEventListener('click', handlePromoCode);

        // Back to cart
        backToCartBtn.addEventListener('click', () => {
            window.location.href = '/cart';
        });

        // Card number formatting
        const cardNumberInput = document.getElementById('cardNumber');
        cardNumberInput.addEventListener('input', formatCardNumber);

        // Expiry formatting
        const expiryInput = document.getElementById('expiry');
        expiryInput.addEventListener('input', formatExpiry);

        // CVV validation
        const cvvInput = document.getElementById('cvv');
        cvvInput.addEventListener('input', validateCVV);
    }

    function loadCartData() {
        currentCart = JSON.parse(localStorage.getItem('easyToEatCart')) || [];
        if (currentCart.length === 0) {
            alert('Coșul este gol. Veți fi redirecționat la meniu.');
            window.location.href = '/menu';
            return;
        }
    }

    function setMinDateTime() {
        const now = new Date();
        now.setMinutes(now.getMinutes() + 30); // Minimum 30 minutes from now
        const minDateTime = now.toISOString().slice(0, 16);
        document.getElementById('bookingDate').min = minDateTime;
        document.getElementById('bookingDate').value = minDateTime;
    }

    async function verifyAndLoadLocations() {
        const itemIds = currentCart.map(item => item.id);

        try {
            // Verifică dacă toate itemele sunt de la același restaurant
            const verifyResponse = await fetch(`/restaurant-resource/verify-cart?${itemIds.map(id => `itemIds=${id}`).join('&')}`);
            const isValid = await verifyResponse.json();

            if (!isValid) {
                showError('Toate produsele trebuie să fie de la același restaurant!');
                setTimeout(() => window.location.href = '/cart', 2000);
                return;
            }

            // Încarcă locațiile pentru primul item
            const locationsResponse = await fetch(`/restaurant-resource/locations-by-item?itemId=${itemIds[0]}`);
            if (locationsResponse.ok) {
                currentLocations = await locationsResponse.json();
                populateLocationSelect();
            }
        } catch (error) {
            showError('Eroare la încărcarea datelor.');
        }
    }

    function populateLocationSelect() {
        locationSelect.innerHTML = '<option value="">Alegeți locația...</option>';
        currentLocations.forEach(location => {
            const option = document.createElement('option');
            option.value = location.id;
            option.textContent = location.address;
            locationSelect.appendChild(option);
        });
    }

    function populateOrderSummary() {
        const orderItemsContainer = document.getElementById('orderItems');
        const subtotalElem = document.getElementById('subtotal');
        const finalTotalElem = document.getElementById('finalTotal');

        orderItemsContainer.innerHTML = '';
        let subtotal = 0;

        currentCart.forEach(item => {
            const itemTotal = item.price * item.quantity;
            subtotal += itemTotal;

            const orderItem = document.createElement('div');
            orderItem.className = 'order-item';
            orderItem.innerHTML = `
                <div class="item-info">
                    <div class="item-name">${item.name}</div>
                    <div class="item-quantity">Cantitate: ${item.quantity}</div>
                </div>
                <div class="item-price">${itemTotal.toFixed(2)} MDL</div>
            `;
            orderItemsContainer.appendChild(orderItem);
        });

        subtotalElem.textContent = `${subtotal.toFixed(2)} MDL`;
        updateFinalTotal(subtotal);
    }

    function updateFinalTotal(subtotal) {
        const discountRow = document.getElementById('discountRow');
        const discountElem = document.getElementById('discount');
        const finalTotalElem = document.getElementById('finalTotal');

        let finalTotal = subtotal;

        if (appliedPromo && appliedPromo.discount > 0) {
            const discount = (subtotal * appliedPromo.discount) / 100;
            finalTotal = subtotal - discount;

            discountRow.style.display = 'flex';
            discountElem.textContent = `-${discount.toFixed(2)} MDL`;
        } else {
            discountRow.style.display = 'none';
        }

        finalTotalElem.textContent = `${finalTotal.toFixed(2)} MDL`;
    }

    function handlePaymentMethodChange(e) {
        if (e.target.value === 'card') {
            showCardModal();
        } else {
            cardDetails = null;
        }
    }

    function showCardModal() {
        cardModal.style.display = 'block';
        document.body.style.overflow = 'hidden';
    }

    function closeCardModal() {
        cardModal.style.display = 'none';
        document.body.style.overflow = 'auto';

        // Reset to cash if no card details
        if (!cardDetails) {
            document.getElementById('cash').checked = true;
        }
    }

    function handleCardFormSubmit(e) {
        e.preventDefault();

        const cardNumber = document.getElementById('cardNumber').value;
        const expiry = document.getElementById('expiry').value;
        const cvv = document.getElementById('cvv').value;

        if (validateCardDetails(cardNumber, expiry, cvv)) {
            cardDetails = {
                cardNumber: cardNumber.replace(/\s/g, ''),
                expiry: expiry,
                cvv: cvv
            };

            closeCardModal();
            showSuccess('Detaliile cardului au fost salvate.');
        }
    }

    function validateCardDetails(cardNumber, expiry, cvv) {
        // Validare număr card (basic)
        const cleanCardNumber = cardNumber.replace(/\s/g, '');
        if (cleanCardNumber.length < 16 || cleanCardNumber.length > 19) {
            showError('Numărul cardului trebuie să aibă între 16 și 19 cifre.');
            return false;
        }

        // Validare expiry
        const expiryRegex = /^(0[1-9]|1[0-2])\/\d{2}$/;
        if (!expiryRegex.test(expiry)) {
            showError('Data expirării trebuie să fie în formatul MM/YY.');
            return false;
        }

        // Validare CVV
        if (cvv.length < 3 || cvv.length > 4) {
            showError('CVV-ul trebuie să aibă 3 sau 4 cifre.');
            return false;
        }

        return true;
    }

    function formatCardNumber(e) {
        let value = e.target.value.replace(/\s/g, '').replace(/[^0-9]/gi, '');
        let formattedValue = value.match(/.{1,4}/g)?.join(' ') || value;
        e.target.value = formattedValue;
    }

    function formatExpiry(e) {
        let value = e.target.value.replace(/\D/g, '');
        if (value.length >= 2) {
            value = value.substring(0, 2) + '/' + value.substring(2, 4);
        }
        e.target.value = value;
    }

    function validateCVV(e) {
        e.target.value = e.target.value.replace(/[^0-9]/g, '');
    }

    async function handlePromoCode() {
        const promoCode = document.getElementById('promoCode').value.trim();
        const promoMessage = document.getElementById('promoMessage');

        if (!promoCode) {
            showPromoMessage('Introduceți un cod promoțional.', 'error');
            return;
        }

        try {
            // Aici ar trebui să faci un request la backend pentru validarea codului promo
            // Pentru moment simulez cu un timeout
            applyPromoBtn.textContent = 'Se verifică...';
            applyPromoBtn.disabled = true;

            await new Promise(resolve => setTimeout(resolve, 1000));

            // Simulare validare cod promo - înlocuiește cu request real
            if (promoCode.toLowerCase() === 'discount10') {
                appliedPromo = { code: promoCode, discount: 10 };
                showPromoMessage('Cod promoțional aplicat! Reducere de 10%', 'success');
                const subtotal = currentCart.reduce((sum, item) => sum + (item.price * item.quantity), 0);
                updateFinalTotal(subtotal);
            } else {
                showPromoMessage('Cod promoțional invalid.', 'error');
            }

        } catch (error) {
            console.error('Eroare la aplicarea codului promo:', error);
            showPromoMessage('Eroare la verificarea codului promo.', 'error');
        } finally {
            applyPromoBtn.textContent = 'Aplică';
            applyPromoBtn.disabled = false;
        }
    }

    function showPromoMessage(message, type) {
        const promoMessage = document.getElementById('promoMessage');
        promoMessage.textContent = message;
        promoMessage.className = `promo-message ${type}`;
    }

    async function handleCheckoutSubmit(e) {
        e.preventDefault();

        if (!validateForm()) {
            return;
        }

        const formData = new FormData(checkoutForm);
        const paymentType = formData.get('paymentType');

        // Verifică dacă pentru card avem detaliile necesare
        if (paymentType === 'card' && !cardDetails) {
            showError('Vă rugăm să completați detaliile cardului.');
            return;
        }

        try {
            showLoading(true);

            const bookingData = await prepareBookingData(formData, paymentType);
            const response = await submitBooking(bookingData);

            if (response.ok) {
                const result = await response.text();
                showSuccess('Comanda a fost înregistrată cu succes!');

                // Clear cart
                localStorage.removeItem('easyToEatCart');

                // Redirect after success
                setTimeout(() => {
                    window.location.href = '/orders'; // sau pagina de confirmări
                }, 2000);
            } else {
                const errorText = await response.text();
                showError(`Eroare la procesarea comenzii: ${errorText}`);
            }

        } catch (error) {
            console.error('Eroare la trimiterea comenzii:', error);
            showError('Eroare de conexiune. Vă rugăm să încercați din nou.');
        } finally {
            showLoading(false);
        }
    }

    function validateForm() {
        const noPeople = document.getElementById('noPeople').value;
        const bookingDate = document.getElementById('bookingDate').value;
        const locationId = document.getElementById('location').value;

        if (!noPeople || noPeople < 1) {
            showError('Vă rugăm să specificați numărul de persoane.');
            return false;
        }

        if (!bookingDate) {
            showError('Vă rugăm să selectați data și ora rezervării.');
            return false;
        }

        if (!locationId) {
            showError('Vă rugăm să selectați o locație.');
            return false;
        }

        // Verifică dacă data este în viitor
        const selectedDate = new Date(bookingDate);
        const now = new Date();
        if (selectedDate <= now) {
            showError('Data rezervării trebuie să fie în viitor.');
            return false;
        }

        return true;
    }

    async function prepareBookingData(formData, paymentType) {
        const finalTotal = parseFloat(document.getElementById('finalTotal').textContent.replace(' MDL', ''));

        // Pregătire BookingDTO
        const bookingDTO = {
            noPeople: parseInt(formData.get('noPeople')),
            preferences: formData.get('preferences') || '',
            locationId: parseInt(formData.get('locationId')),
            tableId: null, // Poate fi gestionat de backend
            itemIds: currentCart.map(item => item.id),
            bookingDate: formData.get('bookingDate')
        };

        // Pregătire PaymentRequest
        let paymentRequest = {
            amount: finalTotal
        };

        if (paymentType === 'card' && cardDetails) {
            paymentRequest = {
                ...paymentRequest,
                cardNumber: cardDetails.cardNumber,
                expiry: cardDetails.expiry,
                cvv: cardDetails.cvv
            };
        }

        return {
            bookingDTO,
            paymentRequest,
            promoCode: appliedPromo ? appliedPromo.code : '',
            paymentType
        };
    }

    async function submitBooking(data) {
        const authToken = localStorage.getItem("token");

        // Construiește URL-ul cu parametrii de query
        const url = new URL('/booking/create', window.location.origin);
        url.searchParams.append('promoCode', data.promoCode);
        url.searchParams.append('paymentType', data.paymentType);

        return await fetch(url, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + authToken
            },
            body: JSON.stringify({
                bookingDTO: data.bookingDTO,
                paymentRequest: data.paymentRequest
            })
        });
    }

    function getAuthToken() {
        // Înlocuiește cu logica ta de obținere a token-ului
        // Poate fi din localStorage, sessionStorage, sau cookie
        return localStorage.getItem('authToken') || sessionStorage.getItem('authToken') || '';
    }

    function showLoading(show) {
        loadingOverlay.style.display = show ? 'flex' : 'none';

        const submitBtn = document.getElementById('submitOrderBtn');
        const btnText = submitBtn.querySelector('.btn-text');
        const btnLoader = submitBtn.querySelector('.btn-loader');

        if (show) {
            btnText.style.display = 'none';
            btnLoader.style.display = 'inline';
            submitBtn.disabled = true;
        } else {
            btnText.style.display = 'inline';
            btnLoader.style.display = 'none';
            submitBtn.disabled = false;
        }
    }

    function showError(message) {
        // Poți înlocui cu o bibliotecă de notificări mai avansată
        alert(`Eroare: ${message}`);
    }

    function showSuccess(message) {
        // Poți înlocui cu o bibliotecă de notificări mai avansată
        alert(`Succes: ${message}`);
    }

    // Utility function pentru debugging
    window.checkoutDebug = {
        getCurrentCart: () => currentCart,
        getCurrentLocations: () => currentLocations,
        getAppliedPromo: () => appliedPromo,
        getCardDetails: () => cardDetails ? { ...cardDetails, cvv: '***' } : null
    };
});