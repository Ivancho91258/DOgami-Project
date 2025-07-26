const form= document.getElementById("Login-autenticación");
const emailInput = document.getElementById("email");
const passwordInput = document.getElementById("password");
const mensajeError = document.getElementById("mensaje-error");

form.addEventListener("submit", function(Event){
    Event.preventDefault(); /*evita que se envíe el formulario*/
    verificarCredenciales();
});

function verificarCredenciales(){
    mensajeError.textContent="";

    const email = emailInput.value;
    const password = passwordInput.value;

    if (!email || !password) {
        mensajeError.textContent = "Por favor, ingresa un correo y la contraseña";
        if (!email) {
            emailInput.focus();
        } else {
            passwordInput.focus();
        }
        return; /*detiene la ejecución si hay campos vacíos*/
    }

    /*Datos de ejemplo para la verificación*/
    const emailCorrecto = "ivancho@gmail.com";
    const passwordCorrecto = "Clave12345.";

    if (email === emailCorrecto && password === passwordCorrecto){
        console.log("Datos correctos");
        window.location.href = "licencia.html";
    } else {
        mensajeError.textContent = "Correo o contraseña incorrectos";
        if (emai !== emailCorrecto){
            emailInput.focus();
        }else{
            passwordInput.focus();
        }
    }
}