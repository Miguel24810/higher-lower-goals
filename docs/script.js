const API_URL = 'https://higher-lower-goals.onrender.com';

let rachaActual = 0;
let categoriaElegida = '';
let jugadorActual1 = null;
let jugadorActual2 = null;
let intervaloTemporizador = null;
let usuariosRanking = [];
let indiceCategoriaRanking = 0;

const categoriasRanking = [
    { id: 'golesCarrera', nombre: 'Goles en la carrera', record: 'recordCarrera' },
    { id: 'golesTemporada', nombre: 'Goles Temporada 24/25', record: 'recordTemporada' },
    { id: 'golesSeleccion', nombre: 'Goles Selección Temporada 24/25', record: 'recordSeleccion' }
];

const preguntasCategoria = {
    golesCarrera: '¿Quién tiene más goles en la carrera?',
    golesTemporada: '¿Quién tiene más goles en la temporada 24/25?',
    golesSeleccion: '¿Quién tiene más goles con su selección?'
};

function limpiarEstadoJuego() {
    detenerTemporizador();
    rachaActual = 0;
    document.getElementById('mensaje-resultado').textContent = '';
    document.getElementById('temporizador-visual').textContent = '10.0 s';
    document.querySelector('#temporizador-barra span').style.transform = 'scaleX(1)';
    document.getElementById('temporizador-barra').setAttribute('aria-valuenow', '10');
    document.getElementById('racha-visual').textContent = 'Racha: 0';
}

function detenerTemporizador() {
    clearInterval(intervaloTemporizador);
    intervaloTemporizador = null;
}

function bloquearBotonesJugador(bloqueados) {
    document.getElementById('btn-jugador1').disabled = bloqueados;
    document.getElementById('btn-jugador2').disabled = bloqueados;
}

function iniciarTemporizador() {
    detenerTemporizador();
    const duracion = 10000;
    const horaFin = Date.now() + duracion;
    const temporizadorVisual = document.getElementById('temporizador-visual');
    const barraTemporizador = document.getElementById('temporizador-barra');
    const rellenoBarra = barraTemporizador.querySelector('span');

    function actualizarTemporizador() {
        const restante = Math.max(0, horaFin - Date.now());
        const segundosRestantes = restante / 1000;
        temporizadorVisual.textContent = segundosRestantes.toFixed(1) + ' s';
        rellenoBarra.style.transform = 'scaleX(' + (restante / duracion) + ')';
        barraTemporizador.setAttribute('aria-valuenow', segundosRestantes.toFixed(1));

        if (restante <= 0) {
            detenerTemporizador();
            bloquearBotonesJugador(true);
            document.getElementById('mensaje-resultado').textContent = 'Se acabó el tiempo. Tu racha era: ' + rachaActual;
            guardarRecord();
            rachaActual = 0;
            document.getElementById('racha-visual').textContent = 'Racha: 0';
            pedirNuevoPar();
        }
    }

    actualizarTemporizador();
    intervaloTemporizador = setInterval(actualizarTemporizador, 50);
}

function iniciarSesion(nombre, password) {
    fetch(`${API_URL}/usuarios/login`, {
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
    })
    .catch(error => {
        document.getElementById('mensaje-error').textContent = error.message;
    });
}
document.getElementById('link-ir-login').addEventListener('click', function(event) {
    event.preventDefault();
    document.getElementById('pantalla-registro').style.display = 'none';
    document.getElementById('pantalla-login').style.display = 'block';
});
document.getElementById('btn-login').addEventListener('click', function() {
    const nombre = document.getElementById('login-nombre').value;
    const password = document.getElementById('login-password').value;
    iniciarSesion(nombre, password);
});
document.getElementById('btn-toggle-login-password').addEventListener('click', function() {
    const password = document.getElementById('login-password');
    const mostrar = password.type === 'password';
    password.type = mostrar ? 'text' : 'password';
    this.dataset.visible = String(mostrar);
    this.setAttribute('aria-label', mostrar ? 'Ocultar contraseña' : 'Mostrar contraseña');
});
document.getElementById('link-ir-registro').addEventListener('click', function(event) {
    event.preventDefault();
    document.getElementById('pantalla-login').style.display = 'none';
    document.getElementById('pantalla-registro').style.display = 'block';
});
document.getElementById('btn-registro').addEventListener('click', function() {
    const nombre = document.getElementById('registro-nombre').value.trim();
    const password = document.getElementById('registro-password').value;
    const passwordConfirmar= document.getElementById('registro-password-confirmar').value;
    const mensajeErrorRegistro = document.getElementById('mensaje-error-registro');

    mensajeErrorRegistro.textContent = '';

    if (nombre.length < 3 || nombre.length > 30) {
        mensajeErrorRegistro.textContent = 'El nombre de usuario debe tener entre 3 y 30 caracteres';
        return;
    }

    if (password.length < 8 || password.length > 64) {
        mensajeErrorRegistro.textContent = 'La contraseña debe tener entre 8 y 64 caracteres';
        return;
    }

    if(password !== passwordConfirmar){
        mensajeErrorRegistro.textContent = 'Las contraseñas no coinciden';
        return;
    }

    fetch(`${API_URL}/usuarios/registro`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ nombre: nombre, password: password })
    })
    .then(response => {
        if (!response.ok) {
            if (response.status === 409) {
                throw new Error('Ese nombre de usuario ya está en uso');
            }
            throw new Error('No se pudo registrar');
        }
        return response.json();
    })
    .then(usuarioCreado => {
        iniciarSesion(nombre, password);
    })
    .catch(error => {
        mensajeErrorRegistro.textContent = error.message;
    });
});

document.querySelectorAll('.btn-categoria').forEach(function(boton) {
    boton.addEventListener('click', function() {
        limpiarEstadoJuego();
        categoriaElegida = boton.dataset.categoria;
        document.getElementById('pregunta-categoria').textContent = preguntasCategoria[categoriaElegida];
        pedirNuevoPar();
        consultarRecord();
        document.getElementById('pantalla-categoria').style.display = 'none';
        document.getElementById('pantalla-juego').style.display = 'block';
    });
});
document.getElementById('btn-volver-categoria').addEventListener('click', function() {
    limpiarEstadoJuego();
    document.getElementById('pantalla-juego').style.display = 'none';
    document.getElementById('pantalla-categoria').style.display = 'block';
});
document.getElementById('btn-volver-login').addEventListener('click', function() {
    limpiarEstadoJuego();
    document.getElementById('pantalla-categoria').style.display = 'none';
    document.getElementById('pantalla-login').style.display = 'block';
    localStorage.removeItem('token');
});

document.getElementById('btn-jugador1').addEventListener('click', function() {
    detenerTemporizador();
    bloquearBotonesJugador(true);
    fetch(`${API_URL}/jugadores/comparar`, {
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
            guardarRecord();
            pedirNuevoPar();
        } else {
            document.getElementById('mensaje-resultado').textContent = 'Fallaste. Tu racha era: ' + rachaActual;
            guardarRecord();
            rachaActual = 0;
            document.getElementById('racha-visual').textContent = 'Racha: 0';
            pedirNuevoPar();
        }
    });
});

document.getElementById('btn-jugador2').addEventListener('click', function() {
    detenerTemporizador();
    bloquearBotonesJugador(true);
    fetch(`${API_URL}/jugadores/comparar`, {
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
            guardarRecord();
            pedirNuevoPar();
        } else {
            document.getElementById('mensaje-resultado').textContent = 'Fallaste. Tu racha era: ' + rachaActual;
            guardarRecord();
            rachaActual = 0;
            document.getElementById('racha-visual').textContent = 'Racha: 0';
            pedirNuevoPar();
        }
    });
});

function pedirNuevoPar() {
    detenerTemporizador();
    bloquearBotonesJugador(true);
    fetch(`${API_URL}/jugadores/random-pair`)
        .then(response => response.json())
        .then(jugadores => {
            jugadorActual1 = jugadores[0];
            jugadorActual2 = jugadores[1];
            document.getElementById('btn-jugador1').innerHTML = '<img class="jugador-foto" src="' + jugadorActual1.urlFoto + '" alt=""><span class="jugador-nombre">' + jugadorActual1.nombre + '</span>';
            document.getElementById('btn-jugador2').innerHTML = '<img class="jugador-foto" src="' + jugadorActual2.urlFoto + '" alt=""><span class="jugador-nombre">' + jugadorActual2.nombre + '</span>';
            document.getElementById('temporizador-visual').textContent = '10.0 s';
            document.querySelector('#temporizador-barra span').style.transform = 'scaleX(1)';
            document.getElementById('temporizador-barra').setAttribute('aria-valuenow', '10');
            bloquearBotonesJugador(false);
            iniciarTemporizador();
        });
}

function obtenerRecordCategoria(usuario) {
    const categoria = categoriasRanking.find(item => item.id === categoriaElegida);
    return categoria ? usuario[categoria.record] : 0;
}

function actualizarRecordVisual(usuario) {
    document.getElementById('record-visual').textContent = 'Récord: ' + obtenerRecordCategoria(usuario);
}

function guardarRecord() {
    const token = localStorage.getItem('token');
    fetch(`${API_URL}/usuarios/record`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ racha: rachaActual, categoria: categoriaElegida })
    })
    .then(response => response.json())
    .then(usuario => {
        actualizarRecordVisual(usuario);
    });
}

function consultarRecord() {
    const token = localStorage.getItem('token');
    fetch(`${API_URL}/usuarios/record`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + token
        },
        body: JSON.stringify({ racha: 0, categoria: categoriaElegida })
    })
    .then(response => response.json())
    .then(usuario => {
        actualizarRecordVisual(usuario);
    });
}

document.getElementById('btn-ver-ranking').addEventListener('click', function() {
    fetch(`${API_URL}/usuarios/ranking`)
        .then(response => response.json())
        .then(usuarios => {
            usuariosRanking = usuarios;
            indiceCategoriaRanking = Math.max(0, categoriasRanking.findIndex(item => item.id === categoriaElegida));
            renderizarRanking();
            document.getElementById('pantalla-categoria').style.display = 'none';
            document.getElementById('pantalla-ranking').style.display = 'block';
        });
});

function renderizarRanking() {
    const categoria = categoriasRanking[indiceCategoriaRanking];
    const lista = document.getElementById('lista-ranking');
    document.getElementById('pantalla-ranking').dataset.rankingCategory = categoria.id;
    lista.innerHTML = '';
    document.getElementById('ranking-categoria').textContent = categoria.nombre;

    usuariosRanking
        .slice()
        .sort((usuarioA, usuarioB) => usuarioB[categoria.record] - usuarioA[categoria.record])
        .forEach(function(usuario, indice) {
            const posicion = indice + 1;
            const item = document.createElement('li');
            const indicadorPosicion = document.createElement('span');
            const nombre = document.createElement('span');
            const record = document.createElement('span');

            if (posicion <= 3) {
                const medallas = ['🥇', '🥈', '🥉'];
                const puestos = ['Primer puesto', 'Segundo puesto', 'Tercer puesto'];
                item.classList.add('puesto-' + posicion);
                indicadorPosicion.className = 'ranking-medalla';
                indicadorPosicion.setAttribute('aria-label', puestos[indice]);
                indicadorPosicion.textContent = medallas[indice];
            } else {
                indicadorPosicion.className = 'ranking-posicion';
                indicadorPosicion.textContent = posicion;
            }

            nombre.className = 'ranking-nombre';
            nombre.textContent = usuario.nombre;
            record.className = 'ranking-record';
            record.textContent = usuario[categoria.record];
            record.setAttribute('aria-label', 'Récord: ' + usuario[categoria.record]);

            item.append(indicadorPosicion, nombre, record);
            lista.appendChild(item);
        });
}

document.getElementById('btn-ranking-anterior').addEventListener('click', function() {
    indiceCategoriaRanking = (indiceCategoriaRanking - 1 + categoriasRanking.length) % categoriasRanking.length;
    renderizarRanking();
});

document.getElementById('btn-ranking-siguiente').addEventListener('click', function() {
    indiceCategoriaRanking = (indiceCategoriaRanking + 1) % categoriasRanking.length;
    renderizarRanking();
});

document.getElementById('btn-volver-ranking').addEventListener('click', function() {
    document.getElementById('pantalla-ranking').style.display = 'none';
    document.getElementById('pantalla-categoria').style.display = 'block';
});
