/**
 * Main Express app setup
 */
const express = require('express');
const cors = require('cors');
require('dotenv').config();
const connectDB = require('./config/mongodb');

const app = express();
app.use(cors());
app.use(express.json());

// Connect to DB
connectDB(process.env.MONGO_URI);

// Example route
app.get('/', (req, res) => {
    res.send('API is running...');
});

module.exports = app;
