document.addEventListener("DOMContentLoaded", function() {
    var nav = document.querySelector('nav');
    if (nav) {
        nav.innerHTML = `
            <div class="navbar">
                <div>
                    <img src="images/logo.png" style="width: calc(100% - 20px); margin: 15px 10px 10px;">
                    <a href="index.html" class="mt-4">Home</a>
                    <div class="navbar-label">Features</div>
                    <ul>
                        <li><a href="challenges.html">Challenges</a></li>
                        <li><a href="showcase.html">Showcase</a></li>
                    </ul>
        
                    <div class="navbar-label">Use</div>
                    <ul>
                        <li><a href="install.html">Installation</a></li>
                        <li><a href="api.html">API</a></li>
                    </ul>
                </div>
                <div class="buttonMenu">
                    <button onclick="switchTheme()"><i class="fa-solid fa-sun"></i></button>
                    <button onclick="toggleNavbar()"><i class="fa-solid fa-angles-left"></i></button>
                </div>
            </div>
            <div class="navbar-closes hide">
                <div class="buttonMenu">
                    <button onclick="toggleNavbar()"><i class="fa-solid fa-angles-right"></i></button>
                </div>
            </div>
        `;
    }
});

const currentTheme = localStorage.getItem('theme') ? localStorage.getItem('theme') : null;
let darkMode = true;

if (currentTheme) {
    document.documentElement.setAttribute('data-theme', currentTheme);
    if (currentTheme === 'dark') {
        darkMode = false;
        document.documentElement.setAttribute('data-theme', 'dark');
    }
}else {
    localStorage.setItem('theme', 'dark');
    document.documentElement.setAttribute('data-theme', 'dark');
    darkMode = false;
}

function switchTheme() {
    if (darkMode) {
        document.documentElement.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', 'dark'); //add this
        darkMode = false;
    }
    else {
        document.documentElement.setAttribute('data-theme', 'light');
        localStorage.setItem('theme', 'light'); //add this
        darkMode = true;
    }
}

let enabled = false;
function toggleNavbar() {
    var navbar = document.querySelector('.navbar');
    var navbarCloses = document.querySelector('.navbar-closes');
    var nav = document.querySelector('nav');

    if (navbar) {
        navbar.classList.toggle('hide');
    }
    if (navbarCloses) {
        navbarCloses.classList.toggle('hide');
    }

    if (enabled){
        nav.style.width = "350px";
        nav.style.padding = "0 10px";
        enabled = false;
    }
    else {
        nav.style.width = "0";
        nav.style.padding = "0";
        enabled = true;
    }

}