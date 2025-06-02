document.addEventListener("DOMContentLoaded", () => {
    const bookingList = document.getElementById("bookingList");
    const noBookingsMsg = document.getElementById("noBookingsMsg");

    const token = localStorage.getItem("token");
    if (!token) {
        bookingList.innerHTML = '<p>Trebuie să fii autentificat pentru a vedea bookingurile.</p>';
        return;
    }
    console.log("Token din bookings = " + token)


    const loadBookings = () => {
        fetch('/booking/user-bookings', {
            method: 'POST',
            headers: {
                'Authorization': 'Bearer ' + token,
                'Content-Type': 'application/json'
            }
        })
            .then(res => {
                if (!res.ok) throw new Error('Eroare la încărcarea bookingurilor');
                return res.json();
            })
            .then(bookings => {
                if (!bookings || bookings.length === 0) {
                    noBookingsMsg.style.display = 'block';
                    return;
                }

                noBookingsMsg.style.display = 'none';
                bookingList.innerHTML = '';

                // sortare descrescătoare după orderTime
                bookings.sort((a, b) => new Date(b.orderTime) - new Date(a.orderTime));

                bookings.forEach(b => {
                    const date = new Date(b.orderTime);
                    const formattedDate = date.toLocaleString('ro-RO', {
                        year: 'numeric', month: 'numeric', day: 'numeric',
                        hour: '2-digit', minute: '2-digit'
                    });

                    const itemsList = b.items ? b.items.split(',') : [];
                    const itemsHtml = itemsList.map(item => `<li>${item.trim()}</li>`).join('');
                    const itemsBlock = itemsHtml ? `<ul>${itemsHtml}</ul>` : '-';

                    const statusColor = b.bookingStatus === 'CANCELED' ? 'red' : 'green';

                    const card = document.createElement('div');
                    card.classList.add('booking-card');
                    card.innerHTML = `
                        <div class="booking-left">
                            <div class="status-indicator" style="background-color: ${statusColor};"></div>
                            <div class="booking-info">
                                <div><strong>ID Booking:</strong> ${b.id}</div>
                                <div><strong>Status:</strong> ${b.bookingStatus}</div>
                                <div><strong>Data și ora:</strong> ${formattedDate}</div>
                                <div><strong>Număr persoane:</strong> ${b.noPeople}</div>
                                <div><strong>Preț final:</strong> ${b.finalPrice.toFixed(2)} MDL</div>
                                <div><strong>Detalii comenzi:</strong>${itemsBlock}</div>
                            </div>
                        </div>
                        <div class="booking-right">
                            <button class="reorder-button" data-booking-id="${b.id}">Repetă rezervarea</button>
                        </div>
                    `;
                    bookingList.appendChild(card);

                    card.querySelector(".reorder-button").addEventListener("click", () => {
                        fetch(`/booking/reorder?bookingId=${b.id}`, {
                            method: 'GET',
                            headers: {
                                'Authorization': 'Bearer ' + token
                            }
                        })
                            .then(r => {
                                if (!r.ok) throw new Error("Eroare la repetarea rezervării");
                                return r.text();
                            })
                            .then(msg => {
                                alert(msg);
                                loadBookings(); // Refresh automat după reorder
                            })
                            .catch(err => alert(err.message));
                    });
                });
            })
            .catch(err => {
                bookingList.innerHTML = `<p>Eroare: ${err.message}</p>`;
            });
    };

    loadBookings(); // Inițializare la încărcarea paginii
});
