function myFunction() {
    let menu = document.getElementById("my-menu");
    menu.classList.toggle("show");
    let overlay = document.getElementById("overlay");
    if(menu.classList.contains("show")){
        overlay.style.display = "block";
    } else{
        overlay.style.display = "none";
    }
}

window.onclick = function(e) {
    let catalogButton = document.querySelector('.catalog-button');
    let overlay = document.getElementById("overlay");
    if (!catalogButton.contains(e.target)) {
        let menu = document.getElementById("my-menu");
        if (menu.classList.contains('show')) {
            overlay.style.display = "none";
            menu.classList.remove('show');
        }
    }
}