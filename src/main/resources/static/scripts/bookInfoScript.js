const url = window.location.href;
let lastSlashIndex = url.lastIndexOf("/");
let bookId = url.substring(lastSlashIndex + 1);


function sendBookDataToServer(){
    let xhr = new XMLHttpRequest();
    xhr.open('GET','http://dpg-cnpjj2f109ks73eupq5g-a.oregon-postgres.render.com:8080/auth/auth-person',false);
    xhr.onload = () => {
        if(xhr.status>=200 && xhr.status<300){
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            fetch('/books/api/toFavoritesById?personId='
                + encodeURIComponent(data['personId'])
                + '&bookId='+encodeURIComponent(bookId), {
                method: 'PATCH',
                headers: {
                    'Content-Type': 'application/json'
                },
            }).then(r => console.log(r.ok));
        } else{
            console.log('Request failed with status: ' + xhr.status);
        }
    }
    xhr.send();
}

let favoriteIcon = document.querySelector('.favorites');
favoriteIcon.addEventListener('click', () =>{
    sendBookDataToServer();
    if (favoriteIcon.classList.contains('marked')) {
        favoriteIcon.classList.remove('marked');
        favoriteIcon.src = "/static/images/favoritesIcon.svg";
    } else {
        favoriteIcon.classList.add('marked');
        favoriteIcon.src = "/static/images/markedFavoritesIcon.svg";
    }
})