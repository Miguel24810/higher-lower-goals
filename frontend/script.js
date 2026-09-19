 let rachaActual = 0;
let categoriaElegida = '';
let jugadorActual1 = null;
let jugadorActual2 = null;

function iniciarSesion(nombre, password) {
    fetch('http://localhost:8081/usuarios/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre: nombre, password: password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('Credenciales incorrectas');
        }
        return response.text();
    })
    .then(token => {
        localStorage.setItem('token', token);
        document.getElementById('pantalla-login').style.display = 'none';
        document.getElementById('pantalla-registro').style.display = 'none';
        document.getElementById('pantalla-categoria').style.display = 'block';
        console.log('Login correcto, token:', token);
    })
    .catch(error => {
        document.getElementById('mensaje-error').textContent = error.message;
    });
}
document.getElementById('link-ir-login').addEventListener('click', function() {
    document.getElementById('pantalla-registro').style.display = 'none';
    document.getElementById('pantalla-login').style.display = 'block';
});
document.getElementById('btn-login').addEventListener('click', function() {
    const nombre = document.getElementById('login-nombre').value;
    const password = document.getElementById('login-password').value;
    iniciarSesion(nombre, password);
});
document.getElementById('link-ir-registro').addEventListener('click', function() {
    document.getElementById('pantalla-login').style.display = 'none';
    document.getElementById('pantalla-registro').style.display = 'block';
});
document.getElementById('btn-registro').addEventListener('click', function() {
    const nombre = document.getElementById('registro-nombre').value;
    const password = document.getElementById('registro-password').value;
    const passwordConfirmar= document.getElementById('registro-password-confirmar').value;

    if(password !== passwordConfirmar){
        document.getElementById('mensaje-error-registro').textContent = 'Las contraseñas no coinciden';
        return;
    }

    fetch('http://localhost:8081/usuarios/registro', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre: nombre, password: password })
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('No se pudo registrar (¿el usuario ya existe?)');
        }
        return response.json();
    })
    .then(usuarioCreado => {
        iniciarSesion(nombre, password);
    })
    .catch(error => {
        document.getElementById('mensaje-error-registro').textContent = error.message;
    });
});

document.querySelectorAll('.btn-categoria').forEach(function(boton) {
    boton.addEventListener('click', function() {
        categoriaElegida = boton.dataset.categoria;
        pedirNuevoPar();
        consultarRecord();
        document.getElementById('pantalla-categoria').style.display = 'none';
        document.getElementById('pantalla-juego').style.display = 'block';
    });
});
document.getElementById('btn-volver-categoria').addEventListener('click', function() {
    document.getElementById('pantalla-juego').style.display = 'none';
    document.getElementById('pantalla-categoria').style.display = 'block';
    
});
document.getElementById('btn-volver-login').addEventListener('click', function() {
    document.getElementById('pantalla-categoria').style.display = 'none';
    document.getElementById('pantalla-login').style.display = 'block';
    localStorage.removeItem('token')
    
    
});

document.getElementById('btn-jugador1').addEventListener('click', function() {
    fetch('http://localhost:8081/jugadores/comparar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            idElegido: jugadorActual1.id,
            idOtro: jugadorActual2.id,
            categoria: categoriaElegida
        })
    })
    .then(response => response.json())
    .then(acerto => {
        if (acerto) {
            rachaActual++;
            document.getElementById('mensaje-resultado').textContent = '¡Acertaste!';
            document.getElementById('racha-visual').textContent = 'Racha: ' + rachaActual;
            pedirNuevoPar();
        } else {
            document.getElementById('mensaje-resultado').textContent = 'Fallaste. Tu racha era: ' + rachaActual;

            const token = localStorage.getItem('token');
            fetch('http://localhost:8081/usuarios/record', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({ racha: rachaActual })
            })
            .then(response => response.json())
            .then(usuario => {
                actualizarRecordVisual(usuario);
            });

            rachaActual = 0;
            document.getElementById('racha-visual').textContent = 'Racha: 0';
            pedirNuevoPar();
        }
    });
});

document.getElementById('btn-jugador2').addEventListener('click', function() {
    fetch('http://localhost:8081/jugadores/comparar', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
            idElegido: jugadorActual2.id,
            idOtro: jugadorActual1.id,
            categoria: categoriaElegida
        })
    })
    .then(response => response.json())
    .then(acerto => {
        if (acerto) {
            rachaActual++;
            document.getElementById('mensaje-resultado').textContent = '¡Acertaste!';
            document.getElementById('racha-visual').textContent = 'Racha: ' + rachaActual;
            pedirNuevoPar();
        } else {
            document.getElementById('mensaje-resultado').textContent = 'Fallaste. Tu racha era: ' + rachaActual;

            const token = localStorage.getItem('token');
            fetch('http://localhost:8081/usuarios/record', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                },
                body: JSON.stringify({ racha: rachaActual })
            })
            .then(response => response.json())
            .then(usuario => {
                actualizarRecordVisual(usuario);
            });

            rachaActual = 0;
            document.getElementById('racha-visual').textContent = 'Racha: 0';
            pedirNuevoPar();
        }
    });
});

function pedirNuevoPar() {
    fetch('http://localhost:8081/jugadores/random-pair')
        .then(response => response.json())
        .then(jugadores => {
            jugadorActual1 = jugadores[0];
            jugadorActual2 = jugadores[1];
            document.getElementById('btn-jugador1').textContent = jugadorActual1.nombre;
            document.getElementById('btn-jugador2').textContent = jugadorActual2.nombre;
        });
}

function actualizarRecordVisual(usuario) {
    document.getElementById('record-visual').textContent = 'Récord: ' + usuario.record;
}

function consultarRecord() {
    const token = localStorage.getItem('token');
    fetch('http://localhost:8081/usuarios/record', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ racha: 0 })
    })
    .then(response => response.json())
    .then(usuario => {
        actualizarRecordVisual(usuario);
    });

}
const videoFondo = document.getElementById('video-fondo');

videoFondo.addEventListener('timeupdate', function() {
    const tiempoRestante = videoFondo.duration - videoFondo.currentTime;

    if (tiempoRestante <= 0.5) {
        videoFondo.classList.add('fade-video');
    }
});

videoFondo.addEventListener('ended', function() {
    videoFondo.currentTime = 0;
    videoFondo.play();

    setTimeout(function() {
        videoFondo.classList.remove('fade-video');
    }, 100);
});