document.addEventListener('DOMContentLoaded', function () {

    // DOM Elements
    const loginTab = document.getElementById('login-tab');
    const registerTab = document.getElementById('register-tab');

    const loginForm = document.getElementById('login-form');
    const registerForm = document.getElementById('register-form');

    const switchToRegister =
        document.getElementById('switch-to-register');

    const switchToLogin =
        document.getElementById('switch-to-login');

    const authMessage =
        document.getElementById('auth-message');

    const formSubtitle =
        document.getElementById('form-subtitle');


    // Show Login Form
    function showLogin() {

        loginTab.classList.add('active');
        registerTab.classList.remove('active');

        loginForm.classList.add('active');
        registerForm.classList.remove('active');

        formSubtitle.textContent =
            'Welcome back! Please login';

        clearMessage();
    }


    // Show Register Form
    function showRegister() {

        registerTab.classList.add('active');
        loginTab.classList.remove('active');

        registerForm.classList.add('active');
        loginForm.classList.remove('active');

        formSubtitle.textContent =
            'Create your account';

        clearMessage();
    }


    // Clear Message
    function clearMessage() {

        authMessage.textContent = '';
        authMessage.className = 'auth-message';
    }


    // Show Message
    function showMessage(type, text) {

        authMessage.textContent = text;
        authMessage.className =
            `auth-message ${type}`;

        setTimeout(clearMessage, 5000);
    }


    // Tab Events
    loginTab.addEventListener('click', showLogin);

    registerTab.addEventListener('click', showRegister);

    switchToRegister.addEventListener(
        'click',
        function (e) {
            e.preventDefault();
            showRegister();
        }
    );

    switchToLogin.addEventListener(
        'click',
        function (e) {
            e.preventDefault();
            showLogin();
        }
    );


    // =========================
    // LOGIN
    // =========================

    loginForm.addEventListener(
        'submit',
        async function (e) {

            e.preventDefault();

            const email =
                document.getElementById('login-email')
                    .value
                    .trim();

            const password =
                document.getElementById('login-password')
                    .value;

            const rememberMe =
                document.getElementById('remember-me')
                    .checked;


            // Validation
            if (!email || !password) {

                showMessage(
                    'error',
                    'Please fill in all fields'
                );

                return;
            }


            try {

                // Backend Login API
                const response = await fetch(
                    'http://localhost:8080/api/auth/login',
                    {
                        method: 'POST',

                        headers: {
                            'Content-Type':
                                'application/json'
                        },

                        body: JSON.stringify({
                            email: email,
                            password: password
                        })
                    }
                );


                // Backend response
                const data =
                    await response.text();


                // Login failed
                if (!response.ok) {

                    showMessage(
                        'error',
                        data ||
                        'Invalid email or password'
                    );

                    return;
                }


                // Store JWT token
                localStorage.setItem(
                    'token',
                    data
                );


                // Remember Me
                if (rememberMe) {

                    localStorage.setItem(
                        'rememberedEmail',
                        email
                    );

                } else {

                    localStorage.removeItem(
                        'rememberedEmail'
                    );
                }


                showMessage(
                    'success',
                    'Login successful! Redirecting...'
                );


                // Decode JWT payload
                const payload =
                    JSON.parse(
                        atob(data.split('.')[1])
                    );


                // Redirect according to role
                setTimeout(function () {

                    if (payload.role === 'ADMIN') {

                        window.location.href =
                            'admin.html';

                    } else {

                        window.location.href =
                            'index.html';
                    }

                }, 1500);


            } catch (error) {

                console.error(
                    'Login error:',
                    error
                );

                showMessage(
                    'error',
                    'Unable to connect to server. Please try again.'
                );
            }
        }
    );


    // =========================
    // REGISTER
    // =========================

    registerForm.addEventListener(
        'submit',
        async function (e) {

            e.preventDefault();


            const name =
                document.getElementById(
                    'register-name'
                ).value.trim();

            const email =
                document.getElementById(
                    'register-email'
                ).value.trim();

            const password =
                document.getElementById(
                    'register-password'
                ).value;

            const confirmPassword =
                document.getElementById(
                    'register-confirm-password'
                ).value;


            // Validation
            if (
                !name ||
                !email ||
                !password ||
                !confirmPassword
            ) {

                showMessage(
                    'error',
                    'Please fill in all fields'
                );

                return;
            }


            if (password.length < 8) {

                showMessage(
                    'error',
                    'Password must be at least 8 characters'
                );

                return;
            }


            if (password !== confirmPassword) {

                showMessage(
                    'error',
                    'Passwords do not match'
                );

                return;
            }


            try {

                // Backend Register API
                const response = await fetch(
                    'http://localhost:8080/api/auth/register',
                    {
                        method: 'POST',

                        headers: {
                            'Content-Type':
                                'application/json'
                        },

                        body: JSON.stringify({
                            name: name,
                            email: email,
                            password: password
                        })
                    }
                );


                const data =
                    await response.json();


                // Registration failed
                if (!response.ok) {

                    showMessage(
                        'error',
                        data ||
                        'Registration failed'
                    );

                    return;
                }


                showMessage(
                    'success',
                    'Registration successful! Please login.'
                );


                showLogin();


                // Pre-fill email
                document.getElementById(
                    'login-email'
                ).value = email;


            } catch (error) {

                console.error(
                    'Registration error:',
                    error
                );

                showMessage(
                    'error',
                    'Unable to connect to server. Please try again.'
                );
            }
        }
    );

});