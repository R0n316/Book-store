

function sendBookDataToServer(bookName,author){
    let xhr = new XMLHttpRequest();
    xhr.open('GET','http://localhost:8080/auth/auth-person',true);
    xhr.onload = () => {
        if(xhr.status>=200 && xhr.status<300){
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            fetch('/books/favoritesByNameAndAuthor?name=' + encodeURIComponent(bookName) + '&author=' + encodeURIComponent(author)
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
        let bookName = favoriteIcon.closest('.book-card-container1').querySelector('.book-name').textContent;
        let author = favoriteIcon.closest('.book-card-container1').querySelector('.book-author').textContent;
        sendBookDataToServer(bookName, author);
    });
});


favoriteIcons.forEach(icon => {
    const bookName = icon.closest('.book-card-container1').querySelector('.book-name').textContent;
    const author = icon.closest('.book-card-container1').querySelector('.book-author').textContent;

    icon.addEventListener('click', () => {
        favoriteIcons.forEach(favoriteIcon => {
            const currentBookName = favoriteIcon.closest('.book-card-container1').querySelector('.book-name').textContent;
            const currentAuthor = favoriteIcon.closest('.book-card-container1').querySelector('.book-author').textContent;
            if(currentBookName===bookName&&currentAuthor===author){
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


