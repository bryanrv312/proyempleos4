// Función para cerrar manualmente el toast
function cerrarAlerta(alerta) {
    if (alerta && alerta.classList) {
        alerta.classList.add('ocultar');
        setTimeout(() => {
            if (alerta && alerta.remove) {
                alerta.remove();
            }
        }, 350);
    }
}

document.addEventListener('DOMContentLoaded', function () {
    console.log("hola");

    // Selecciona TODOS los toasts que existan
    document.querySelectorAll('.toast-alert').forEach(toast => {
        setTimeout(() => cerrarAlerta(toast), 4000); // autocierre en 4s
    });
});
