document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("registerForm");
    const errorMessage = document.getElementById("errorMessage");
    const successMessage = document.getElementById("successMessage");
    const btnText = document.querySelector(".btn-text");
    const btnLoader = document.querySelector(".btn-loader");

    form.addEventListener("submit", async function (e) {
        e.preventDefault();

        errorMessage.style.display = "none";
        successMessage.style.display = "none";
        btnText.style.display = "none";
        btnLoader.style.display = "inline-block";

        const formData = new FormData(form);
        const payload = {
            username: formData.get("username"),
            email: formData.get("email"),
            phone: formData.get("phone"),
            password: formData.get("password"),
            confirmPassword: formData.get("confirmPassword"),
            role: formData.get("role")
        };

        if (payload.password !== payload.confirmPassword) {
            showError("Parolele nu se potrivesc");
            return;
        }

        try {
            const response = await fetch("/auth/register", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(payload)
            });

            const result = await response.text();

            if (!response.ok) {
                throw new Error(result);
            }

            successMessage.innerText = result;
            successMessage.style.display = "block";
            form.reset();
        } catch (err) {
            showError(err.message);
        }
    });

    function showError(msg) {
        errorMessage.innerText = msg;
        errorMessage.style.display = "block";
        btnText.style.display = "inline-block";
        btnLoader.style.display = "none";
    }
});
