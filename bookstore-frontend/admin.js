const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}
const payload = JSON.parse(atob(token.split(".")[1]));

if (payload.role !== "ADMIN") {
    alert("Access denied. Admin only.");
    window.location.href = "index.html";
}
async function loadDashboardData() {

    try {

        const booksResponse = await fetch(
            "http://localhost:8080/api/books",
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const books = await booksResponse.json();

        const ordersResponse = await fetch(
            "http://localhost:8080/api/orders/admin",
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const orders = await ordersResponse.json();

        // Book count
        document.getElementById("book-count").textContent =
            books.length;

        // Order count
        document.getElementById("order-count").textContent =
            orders.length;

        // Low stock count
        const lowStockBooks = books.filter(
            book => book.stockQuantity <= 5
        );

        document.getElementById("low-stock-count").textContent =
            lowStockBooks.length;

    } catch (error) {

        console.error(
            "Error loading dashboard data:",
            error
        );
    }
}

loadDashboardData();