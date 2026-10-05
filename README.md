
# Who Has More? ⚽

Juego web "higher/lower" de fútbol: adivina qué futbolista tiene más goles. Con cuentas de usuario, racha de aciertos, récord por categoría y ranking.

**🎮 [Juega aquí](https://miguel24810.github.io/higher-lower-goals/)**

> El backend está en el plan gratuito de Render y puede tardar ~30-60s en arrancar si lleva un rato dormido.

## Stack

- **Backend:** Java 21, Spring Boot (Web, JPA, Security), MySQL, JWT, tests con JUnit + Mockito, Docker
- **Frontend:** HTML/CSS/JS vanilla
- **Datos:** [API-Football](https://www.api-football.com/)

## Cómo funciona

El backend cachea en su propia base de datos los datos de los jugadores, sincronizados manualmente desde API-Football (su plan gratuito limita a 100 peticiones/día, así que el juego nunca la consulta en directo). Por el mismo límite, los datos de liga y selección son de la temporada 2024/25; los goles de carrera se introducen a mano.

### Backend
```
DB_URL=jdbc:mysql://localhost:3306/tu_bd
DB_USERNAME=...
DB_PASSWORD=...
API_FOOTBALL_KEY=...
ADMIN_SYNC_KEY=...
JWT_SECRET=...
```
cd higher-lower-game
./mvnw spring-boot:run
Arranca en `http://localhost:8081`.

### Frontend
Abre `docs/index.html` directamente en el navegador (doble clic), o sírvelo con una extensión tipo Live Server. No necesita instalación ni build.

### Tests
cd higher-lower-game
./mvnw test

### Docker (backend)
cd higher-lower-game
docker build -t higher-lower-game .
docker run -p 8081:8081 --env-file .env higher-lower-game
