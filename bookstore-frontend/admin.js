const token = localStorage.getItem("token");

if (!token) {
    window.location.href = "login.html";
}

function decodeJwtPayload(token) {
    const base64Url = token.split(".")[1];
    const base64 = base64Url
        .replace(/-/g, "+")
        .replace(/_/g, "/");

    const padded = base64.padEnd(
        base64.length + (4 - base64.length % 4) % 4,
        "="
    );

    return JSON.parse(atob(padded));
}

const payload = decodeJwtPayload(token);

if (payload.role !== "ADMIN") {
    alert("Access denied. Admin only.");
    window.location.href = "index.html";
}

document.getElementById("logout-btn").addEventListener("click", function () {
    localStorage.removeItem("token");
    window.location.href = "login.html";
});

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

        if (!booksResponse.ok) {
            throw new Error("Failed to load books");
        }

        const books = await booksResponse.json();

        const ordersResponse = await fetch(
            "http://localhost:8080/api/orders/admin",
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        if (!ordersResponse.ok) {
            throw new Error("Failed to load orders");
        }

        const orders = await ordersResponse.json();

        document.getElementById("book-count").textContent =
            books.length;

        document.getElementById("order-count").textContent =
            orders.length;
            displayOrders(orders);

        const lowStockBooks = books.filter(
            book => book.stockQuantity <= 5
        );

        document.getElementById("low-stock-count").textContent =
            lowStockBooks.length;

        displayBooks(books);

    } catch (error) {

        console.error(
            "Error loading dashboard data:",
            error
        );
    }
}

function displayBooks(books) {

    const container =
        document.getElementById("books-table-container");

    if (books.length === 0) {
        container.innerHTML = `
            <p>No books found.</p>
        `;
        return;
    }

    let tableHTML = `
        <div class="table-wrapper">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Book</th>
                        <th>Author</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Category</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
    `;

    books.forEach(book => {

        tableHTML += `
            <tr>
                <td>${book.id}</td>

                <td>
                    <div class="book-info">
                        <img
                            src="${book.image}"
                            alt="${book.title}"
                        >
                        <span>${book.title}</span>
                    </div>
                </td>

                <td>${book.author}</td>

                <td>₹${book.price}</td>

                <td>${book.stockQuantity}</td>

                <td>${book.category}</td>

                <td>
                    <button
                        class="action-btn edit-btn"
                        onclick="editBook(${book.id})"
                    >
                        Edit
                    </button>

                    <button
                        class="action-btn delete-btn"
                        onclick="deleteBook(${book.id})"
                    >
                        Delete
                    </button>
                </td>
            </tr>
        `;
    });

    tableHTML += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = tableHTML;
}
async function editBook(bookId) {

    const title = prompt("Enter new book title:");
    if (!title) return;

    const author = prompt("Enter new author name:");
    if (!author) return;

    const price = prompt("Enter new price:");
    if (!price) return;

    const image = prompt("Enter new image URL:");
    if (!image) return;

    const category = prompt("Enter new category:");
    if (!category) return;

    const rating = prompt("Enter new rating (0-5):");
    if (!rating) return;

    const description = prompt("Enter new book description:");
    if (!description) return;

    const stockQuantity = prompt("Enter new stock quantity:");
    if (!stockQuantity) return;

    const updatedBook = {
        title: title,
        author: author,
        price: Number(price),
        image: image,
        category: category,
        rating: Number(rating),
        description: description,
        stockQuantity: Number(stockQuantity)
    };

    try {

        const response = await fetch(
            `http://localhost:8080/api/books/${bookId}`,
            {
                method: "PUT",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(updatedBook)
            }
        );

        const data = await response.json();

        if (!response.ok) {
            alert(
                "Failed to update book: " +
                (data.message || "Something went wrong")
            );
            return;
        }

        alert("Book updated successfully!");

        loadDashboardData();

    } catch (error) {

        console.error("Error updating book:", error);

        alert("Unable to connect to server.");
    }
}

async function deleteBook(bookId) {

    const confirmed = confirm(
        "Are you sure you want to delete this book?"
    );

    if (!confirmed) {
        return;
    }

    try {

        const response = await fetch(
            `http://localhost:8080/api/books/${bookId}`,
            {
                method: "DELETE",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const data = await response.text();

        if (!response.ok) {
            alert(
                "Failed to delete book: " +
                (data || "Something went wrong")
            );
            return;
        }

        alert("Book deleted successfully!");

        loadDashboardData();

    } catch (error) {

        console.error("Error deleting book:", error);

        alert("Unable to connect to server.");
    }
}

loadDashboardData();
document.getElementById("add-book-btn").addEventListener("click", function () {

    const title = prompt("Enter book title:");
    if (!title) return;

    const author = prompt("Enter author name:");
    if (!author) return;

    const price = prompt("Enter price:");
    if (!price) return;

    const image = prompt("Enter image URL:");
    if (!image) return;

    const category = prompt("Enter category:");
    if (!category) return;

    const rating = prompt("Enter rating (0-5):");
    if (!rating) return;

    const description = prompt("Enter book description:");
    if (!description) return;

    const stockQuantity = prompt("Enter stock quantity:");
    if (!stockQuantity) return;

    addBook({
        title: title,
        author: author,
        price: Number(price),
        image: image,
        category: category,
        rating: Number(rating),
        description: description,
        stockQuantity: Number(stockQuantity)
    });
});


async function addBook(bookData) {

    try {

        const response = await fetch(
            "http://localhost:8080/api/books",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${token}`
                },
                body: JSON.stringify(bookData)
            }
        );

        const data = await response.json();

        if (!response.ok) {
            alert(
                "Failed to add book: " +
                (data.message || "Something went wrong")
            );
            return;
        }

        alert("Book added successfully!");

        loadDashboardData();

    } catch (error) {

        console.error("Error adding book:", error);

        alert("Unable to connect to server.");
    }
}
function displayOrders(orders) {

    const container =
        document.getElementById("orders-table-container");

    if (orders.length === 0) {
        container.innerHTML = `
            <p>No orders found.</p>
        `;
        return;
    }

    let tableHTML = `
        <div class="table-wrapper">
            <table class="admin-table">
                <thead>
                    <tr>
                        <th>Order ID</th>
                        <th>Total Amount</th>
                        <th>Status</th>
                        <th>Customer Address</th>
                        <th>Items</th>
                    </tr>
                </thead>
                <tbody>
    `;

    orders.forEach(order => {

        const itemCount =
            order.items ? order.items.length : 0;

        const address =
            order.address
                ? order.address
                : `${order.city || ""}, ${order.state || ""} - ${order.pincode || ""}`;

        tableHTML += `
            <tr>

                <td>
                    <strong>#${order.id}</strong>
                </td>

                <td>
                    ₹${order.totalAmount}
                </td>

               <td>
    <span class="order-status ${order.status.toLowerCase()}">
        ${order.status}
    </span>

    <select
        class="status-select"
        onchange="updateOrderStatus(${order.id}, this.value)"
    >
        <option value="">Change Status</option>
        <option value="PLACED">PLACED</option>
        <option value="SHIPPED">SHIPPED</option>
        <option value="DELIVERED">DELIVERED</option>
        <option value="CANCELLED">CANCELLED</option>
    </select>
</td>

                <td>
                    ${address || "N/A"}
                </td>

                <td>
                    ${itemCount}
                </td>

            </tr>
        `;
    });

    tableHTML += `
                </tbody>
            </table>
        </div>
    `;

    container.innerHTML = tableHTML;
}
async function updateOrderStatus(orderId, status) {

    if (!status) {
        return;
    }

    const confirmed = confirm(
        `Change Order #${orderId} status to ${status}?`
    );

    if (!confirmed) {
        loadDashboardData();
        return;
    }

    try {

        const response = await fetch(
            `http://localhost:8080/api/orders/admin/${orderId}/status?status=${status}`,
            {
                method: "PUT",
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

        const data = await response.json();

        if (!response.ok) {
            alert(
                "Failed to update order: " +
                (data.message || "Something went wrong")
            );
            return;
        }

        alert("Order status updated successfully!");

        loadDashboardData();

    } catch (error) {

        console.error(
            "Error updating order status:",
            error
        );

        alert("Unable to connect to server.");
    }
}