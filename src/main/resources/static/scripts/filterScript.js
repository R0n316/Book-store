$(document).ready(function() {
    $('#filterForm input[type="checkbox"]').change(function() {
        let params = {};
        let selectedLanguages = [];
        const urlParams = new URLSearchParams(window.location.search);
        const category = urlParams.get('category');
        if (category) {
            params.category = category;
        }
        $('#filterForm input[type="checkbox"]:checked').each(function() {
            selectedLanguages.push($(this).val());
        });
        if (selectedLanguages.length > 0) {
            params.language = selectedLanguages.join(',');
        }

        $.ajax({
            type: 'GET',
            url: '/books/filter',
            data: params,
            success: function(response) {
                console.log(response);
                $('.book-cards').html(response);
            },
            error: function() {
                alert('Error occurred while fetching filtered books.');
            }
        });
    });
});