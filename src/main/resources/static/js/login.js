document.addEventListener("DOMContentLoaded", function () {
    const loginForm = document.getElementById("loginForm");
    const errorMessage = document.getElementById("errorMessage");
    const loginBtn = document.querySelector(".login-btn");
    const btnText = document.querySelector(".btn-text");
    const btnLoader = document.querySelector(".btn-loader");

    loginForm.addEventListener("submit", async function (e) {
        e.preventDefault();

        errorMessage.style.display = "none";
        loginBtn.disabled = true;
        btnText.style.display = "none";
        btnLoader.style.display = "inline";

        const username = document.getElementById("username").value.trim();
        const password = document.getElementById("password").value.trim();

        const payload = {
            username: username,
            password: password
        };

        try {
            const response = await fetch("/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(payload)
            });

            if (response.ok) {
                const data = await response.json();
                localStorage.setItem("token", data.token);
                localStorage.setItem("role", data.role);
                window.location.href = "/home"; // înlocuiește cu pagina principală după autentificare
            } else {
                const errorData = await response.text();
                errorMessage.textContent = errorData || "Date invalide.";
                errorMessage.style.display = "block";
            }
        } catch (error) {
            errorMessage.textContent = "Eroare de rețea. Încearcă din nou.";
            errorMessage.style.display = "block";
        }

        loginBtn.disabled = false;
        btnText.style.display = "inline";
        btnLoader.style.display = "none";
    });
});
