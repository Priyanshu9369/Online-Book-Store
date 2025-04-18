document.addEventListener('DOMContentLoaded', function() {
    // Sample book data
    const books = [
        {
            id: 1,
            title: "The Great Gatsby",
            author: "F. Scott Fitzgerald",
            price: 12.99,
            image: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "fiction",
            rating: 4,
            description: "A portrait of the Jazz Age in all of its decadence and excess."
        },
        {
            id: 2,
            title: "To Kill a Mockingbird",
            author: "Harper Lee",
            price: 10.50,
            image: "https://images.unsplash.com/photo-1589998059171-988d887df646?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "fiction",
            rating: 5,
            description: "A powerful story of racial injustice and the loss of innocence."
        },
        {
            id: 3,
            title: "1984",
            author: "George Orwell",
            price: 9.99,
            image: "https://images.unsplash.com/photo-1531346878377-a5be20888e57?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "fiction",
            rating: 4,
            description: "A dystopian novel about totalitarianism and surveillance."
        },
        {
            id: 4,
            title: "The Hobbit",
            author: "J.R.R. Tolkien",
            price: 14.99,
            image: "https://images.unsplash.com/photo-1589998059171-988d887df646?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "fantasy",
            rating: 5,
            description: "A fantasy novel about the quest of home-loving Bilbo Baggins."
        },
        {
            id: 5,
            title: "Sapiens",
            author: "Yuval Noah Harari",
            price: 15.99,
            image: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "non-fiction",
            rating: 4,
            description: "A brief history of humankind."
        },
        {
            id: 6,
            title: "Becoming",
            author: "Michelle Obama",
            price: 18.99,
            image: "https://images.unsplash.com/photo-1531346878377-a5be20888e57?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "biography",
            rating: 5,
            description: "A memoir by the former First Lady of the United States."
        },
        {
            id: 7,
            title: "The Silent Patient",
            author: "Alex Michaelides",
            price: 13.50,
            image: "https://images.unsplash.com/photo-1589998059171-988d887df646?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "fiction",
            rating: 4,
            description: "A psychological thriller about a woman's act of violence against her husband."
        },
        {
            id: 8,
            title: "Educated",
            author: "Tara Westover",
            price: 16.99,
            image: "https://images.unsplash.com/photo-1544947950-fa07a98d237f?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80",
            category: "biography",
            rating: 5,
            description: "A memoir about a woman who leaves her survivalist family and goes on to earn a PhD."
        }
    ];

    // DOM Elements
    const booksContainer = document.getElementById('books-container');
    const filterButtons = document.querySelectorAll('.filter-btn');
    const cartButton = document.getElementById('cart-button');
    const cartModal = document.getElementById('cart-modal');
    const closeCart = document.querySelector('.close-cart');
    const cartItemsContainer = document.getElementById('cart-items');
    const cartTotalPrice = document.getElementById('cart-total-price');
    const cartCount = document.querySelector('.cart-count');
    const checkoutButton = document.querySelector('.checkout-btn');
    const checkoutModal = document.getElementById('checkout-modal');
    const closeCheckout = document.querySelector('.close-checkout');
    const checkoutForm = document.getElementById('checkout-form');
    const orderConfirmation = document.getElementById('order-confirmation');
    const closeConfirmation = document.querySelector('.close-confirmation');
    const newsletterForm = document.getElementById('newsletter-form');

    // Cart state
    let cart = JSON.parse(localStorage.getItem('cart')) || [];

    // Display books
    function displayBooks(filter = 'all') {
        booksContainer.innerHTML = '';
        
        const filteredBooks = filter === 'all' 
            ? books 
            : books.filter(book => book.category === filter);
        
        filteredBooks.forEach(book => {
            const isInCart = cart.some(item => item.id === book.id);
            const isWishlisted = false; // This would come from a wishlist state in a real app
            
            const bookElement = document.createElement('div');
            bookElement.className = 'book-card';
            bookElement.dataset.category = book.category;
            bookElement.innerHTML = `
                <div class="book-image">
                    <img src="${book.image}" alt="${book.title}">
                </div>
                <div class="book-info">
                    <h3>${book.title}</h3>
                    <p class="book-author">${book.author}</p>
                    <p class="book-price">$${book.price.toFixed(2)}</p>
                    <div class="book-rating">
                        ${'★'.repeat(book.rating)}${'☆'.repeat(5 - book.rating)}
                    </div>
                    <div class="book-actions">
                        <button class="add-to-cart" data-id="${book.id}" ${isInCart ? 'disabled' : ''}>
                            ${isInCart ? 'Added to Cart' : 'Add to Cart'}
                        </button>
                        <button class="wishlist" data-id="${book.id}">
                            <i class="fas fa-heart ${isWishlisted ? 'active' : ''}"></i>
                        </button>
                    </div>
                </div>
            `;
            booksContainer.appendChild(bookElement);
        });

        // Add event listeners to the new buttons
        document.querySelectorAll('.add-to-cart').forEach(button => {
            button.addEventListener('click', addToCart);
        });

        document.querySelectorAll('.wishlist').forEach(button => {
            button.addEventListener('click', toggleWishlist);
        });
    }

    // Filter books
    filterButtons.forEach(button => {
        button.addEventListener('click', () => {
            filterButtons.forEach(btn => btn.classList.remove('active'));
            button.classList.add('active');
            displayBooks(button.dataset.filter);
        });
    });

    // Add to cart
    function addToCart(e) {
        const bookId = parseInt(e.target.dataset.id);
        const book = books.find(book => book.id === bookId);
        
        const existingItem = cart.find(item => item.id === bookId);
        
        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            cart.push({
                ...book,
                quantity: 1
            });
        }
        
        updateCart();
        e.target.textContent = 'Added to Cart';
        e.target.disabled = true;
        
        // Show a quick notification
        showNotification(`${book.title} added to cart!`);
    }

    // Toggle wishlist (placeholder functionality)
    function toggleWishlist(e) {
        const heartIcon = e.target.querySelector('i') || e.target;
        heartIcon.classList.toggle('active');
        
        const bookId = parseInt(e.target.closest('.wishlist').dataset.id);
        const book = books.find(book => book.id === bookId);
        
        if (heartIcon.classList.contains('active')) {
            showNotification(`${book.title} added to wishlist!`);
        } else {
            showNotification(`${book.title} removed from wishlist!`);
        }
    }

    // Update cart
    function updateCart() {
        localStorage.setItem('cart', JSON.stringify(cart));
        
        // Update cart count
        const totalItems = cart.reduce((total, item) => total + item.quantity, 0);
        cartCount.textContent = totalItems;
        
        // Update cart items display
        renderCartItems();
    }

    // Render cart items
    function renderCartItems() {
        if (cart.length === 0) {
            cartItemsContainer.innerHTML = '<p class="empty-cart">Your cart is empty</p>';
            cartTotalPrice.textContent = '$0.00';
            return;
        }
        
        cartItemsContainer.innerHTML = '';
        
        let total = 0;
        
        cart.forEach(item => {
            const itemTotal = item.price * item.quantity;
            total += itemTotal;
            
            const cartItemElement = document.createElement('div');
            cartItemElement.className = 'cart-item';
            cartItemElement.innerHTML = `
                <div class="cart-item-image">
                    <img src="${item.image}" alt="${item.title}">
                </div>
                <div class="cart-item-details">
                    <p class="cart-item-title">${item.title}</p>
                    <p class="cart-item-author">${item.author}</p>
                    <p class="cart-item-price">$${item.price.toFixed(2)}</p>
                </div>
                <div class="cart-item-actions">
                    <div class="quantity-control">
                        <button class="quantity-btn minus" data-id="${item.id}">-</button>
                        <span>${item.quantity}</span>
                        <button class="quantity-btn plus" data-id="${item.id}">+</button>
                    </div>
                    <button class="remove-item" data-id="${item.id}">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            `;
            cartItemsContainer.appendChild(cartItemElement);
        });
        
        cartTotalPrice.textContent = `$${total.toFixed(2)}`;
        
        // Add event listeners to quantity buttons
        document.querySelectorAll('.quantity-btn').forEach(button => {
            button.addEventListener('click', updateQuantity);
        });
        
        document.querySelectorAll('.remove-item').forEach(button => {
            button.addEventListener('click', removeItem);
        });
    }

    // Update quantity
    function updateQuantity(e) {
        const bookId = parseInt(e.target.dataset.id);
        const item = cart.find(item => item.id === bookId);
        
        if (e.target.classList.contains('plus')) {
            item.quantity += 1;
        } else if (e.target.classList.contains('minus')) {
            if (item.quantity > 1) {
                item.quantity -= 1;
            } else {
                // If quantity would go to 0, remove the item instead
                cart = cart.filter(item => item.id !== bookId);
            }
        }
        
        updateCart();
    }

    // Remove item
    function removeItem(e) {
        const bookId = parseInt(e.target.closest('button').dataset.id);
        cart = cart.filter(item => item.id !== bookId);
        
        updateCart();
        
        // Also update the "Add to Cart" button on the book card
        const addToCartButton = document.querySelector(`.add-to-cart[data-id="${bookId}"]`);
        if (addToCartButton) {
            addToCartButton.textContent = 'Add to Cart';
            addToCartButton.disabled = false;
        }
    }

    // Show notification
    function showNotification(message) {
        const notification = document.createElement('div');
        notification.className = 'notification';
        notification.textContent = message;
        document.body.appendChild(notification);
        
        setTimeout(() => {
            notification.classList.add('show');
        }, 10);
        
        setTimeout(() => {
            notification.classList.remove('show');
            setTimeout(() => {
                document.body.removeChild(notification);
            }, 300);
        }, 3000);
    }

    // Cart modal
    cartButton.addEventListener('click', () => {
        cartModal.style.display = 'flex';
    });

    closeCart.addEventListener('click', () => {
        cartModal.style.display = 'none';
    });

    window.addEventListener('click', (e) => {
        if (e.target === cartModal) {
            cartModal.style.display = 'none';
        }
    });

    // Checkout
    checkoutButton.addEventListener('click', () => {
        cartModal.style.display = 'none';
        checkoutModal.style.display = 'flex';
    });

    closeCheckout.addEventListener('click', () => {
        checkoutModal.style.display = 'none';
    });

    window.addEventListener('click', (e) => {
        if (e.target === checkoutModal) {
            checkoutModal.style.display = 'none';
        }
    });

    // Checkout form
    checkoutForm.addEventListener('submit', (e) => {
        e.preventDefault();
        
        // In a real app, you would process the payment here
        // For this demo, we'll just show a confirmation
        
        checkoutModal.style.display = 'none';
        orderConfirmation.style.display = 'flex';
        
        // Clear the cart
        cart = [];
        updateCart();
        
        // Reset all "Add to Cart" buttons
        document.querySelectorAll('.add-to-cart').forEach(button => {
            button.textContent = 'Add to Cart';
            button.disabled = false;
        });
    });

    // Order confirmation
    closeConfirmation.addEventListener('click', () => {
        orderConfirmation.style.display = 'none';
    });

    window.addEventListener('click', (e) => {
        if (e.target === orderConfirmation) {
            orderConfirmation.style.display = 'none';
        }
    });

    // Newsletter form
    newsletterForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const email = e.target.querySelector('input').value;
        
        // In a real app, you would send this to your server
        console.log('Subscribed email:', email);
        
        showNotification('Thanks for subscribing to our newsletter!');
        e.target.reset();
    });

    // Initialize
    displayBooks();
    updateCart();

    // Add some CSS for the notification
    const style = document.createElement('style');
    style.textContent = `
        .notification {
            position: fixed;
            bottom: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: var(--primary-color);
            color: white;
            padding: 12px 24px;
            border-radius: 5px;
            box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
            opacity: 0;
            transition: opacity 0.3s ease;
            z-index: 1001;
        }
        .notification.show {
            opacity: 1;
        }
    `;
    document.head.appendChild(style);
});