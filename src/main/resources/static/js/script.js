document.addEventListener('DOMContentLoaded', function() {
    const deleteForms = document.querySelectorAll('form[action*="/delete"]');

    deleteForms.forEach(form => {
        form.addEventListener('submit', function(event) {
            const confirmDelete = confirm('Are you sure you want to delete this task?');
            if (!confirmDelete) {
                event.preventDefault();
            }
        });
    });
});