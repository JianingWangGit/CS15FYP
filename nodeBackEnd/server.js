/**
 * Starts the server (calls app.js)
 */
const app = require('./app');

const PORT = process.env.PORT;

app.listen(PORT, () => {
    console.log(`Backend server is running on port ${PORT}`);
});