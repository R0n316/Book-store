function sendBookDataToAddInCart(imagePath){
    let xhr = new XMLHttpRequest();
    xhr.open('GET','http://dpg-cnpjj2f109ks73eupq5g-a.oregon-postgres.render.com:8080/auth/auth-person',true);
    xhr.onload = () => {
        if(xhr.status>=200 && xhr.status<300){
            let data = JSON.parse(xhr.responseText);
            console.log(data);
            console.log("Сработал скрипт добавления в корзину");
            fetch('/books/api/toCart?imagePath='+encodeURIComponent(imagePath)
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

let toCartButtons = document.querySelectorAll(".add-to-cart-button");

toCartButtons.forEach(toCartButton => {
    toCartButton.addEventListener('click', () => {
        let imagePath = toCartButton.closest('.book-card').querySelector('.book-image').src;
        let lastSlashIndex = imagePath.lastIndexOf('/');
        sendBookDataToAddInCart(imagePath.substring(lastSlashIndex + 1));
    })
})

toCartButtons.forEach(button => {
    let imagePath = button.closest('.book-card').querySelector('.book-image').src;

    button.addEventListener('click', () => {
        toCartButtons.forEach(toCartButton => {
            let currentImagePath = toCartButton.closest('.book-card').querySelector('.book-image').src;
            if(currentImagePath === imagePath){
                if (toCartButton.classList.contains('in-cart')) {
                    toCartButton.classList.remove('in-cart');
                    toCartButton.innerHTML = 'В корзину';
                } else {
                    toCartButton.classList.add('in-cart');
                    toCartButton.innerHTML = 'Удалить';
                }
            }
        })
    })
})