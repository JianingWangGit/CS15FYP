/**
 * app.js - Main Express app setup
 */
const express = require('express');
const cors = require('cors');
require('dotenv').config();
const connectDB = require('./config/mongodb');

const app = express();
app.use(cors());
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// Connect to DB
connectDB(process.env.MONGO_URI);

// Example route
app.get('/', (req, res) => {
    res.send('API is running...');
});

const reviewRoutes = require('./routers/review_routes');
const restaurantRoutes = require('./routers/restaurant_routes');

app.use('/reviews', reviewRoutes);
app.use('/restaurants', restaurantRoutes);

module.exports = app;
