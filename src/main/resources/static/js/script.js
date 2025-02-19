document.getElementById("loginForm").onsubmit = async function (event) {
    event.preventDefault(); // Prevent the default form submission

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    try {
        const response = await fetch("/api/users/login", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ username, password }),
        });

        if (response.ok) {
            // If login is successful, redirect to index
            window.location.href = "/"; // Redirect to home page
        } else {
            // If login fails, show an error message
            const errorMessage = await response.text(); // Get the error message from response
            alert(`Login failed: ${errorMessage}`);
        }
    } catch (error) {
        console.error("Error:", error);
        alert("An error occurred during login. Please try again.");
    }
};
