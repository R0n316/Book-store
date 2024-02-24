

function sendBookDataToAddInFavorites(imagePath){
    let xhr = new XMLHttpRequest();
    xhr.open('GET','http://localhost:8080/auth/auth-person',true);
    xhr.onload = () => {
        if(xhr.status>=200 && xhr.status<300){
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            fetch('/books/api/toFavorites?imagePath='+encodeURIComponent(imagePath)
                + '&personId=' + encodeURIComponent(data['personId']), {
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



let favoriteIcons = document.querySelectorAll('.favorites');
favoriteIcons.forEach(favoriteIcon => {
    favoriteIcon.addEventListener('click', () => {
        let imagePath = favoriteIcon.closest('.book-card').querySelector('.book-image').src;
        let lastSlashIndex = imagePath.lastIndexOf('/');
        sendBookDataToAddInFavorites(imagePath.substring(lastSlashIndex + 1));
    });
});


favoriteIcons.forEach(icon => {
    let imagePath = icon.closest('.book-card').querySelector('.book-image').src;

    icon.addEventListener('click', () => {
        favoriteIcons.forEach(favoriteIcon => {
            let currentImagePath = favoriteIcon.closest('.book-card').querySelector('.book-image').src;
            if(currentImagePath === imagePath){
                if (favoriteIcon.classList.contains('marked')) {
                    favoriteIcon.classList.remove('marked');
                    favoriteIcon.src = "/static/images/favoritesIcon.svg";
                } else {
                    favoriteIcon.classList.add('marked');
                    favoriteIcon.src = "/static/images/markedFavoritesIcon.svg";
                }
            }
        })
    })
})