document.addEventListener("DOMContentLoaded", function() {
    var nav = document.querySelector('nav');
    if (nav) {
        nav.innerHTML = `
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
        `;
    }
});