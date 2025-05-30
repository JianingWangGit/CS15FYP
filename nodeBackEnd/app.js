/**
 * app.js - Main Express app setup
 */
const express = require('express');
const cors = require('cors');
require('dotenv').config();
const connectDB = require('./config/mongodb');

const app = express();

// More explicit CORS configuration
const corsOptions = {
  origin: '*',
  methods: 'GET,HEAD,PUT,PATCH,POST,DELETE',
  preflightContinue: false,
  optionsSuccessStatus: 204,
  credentials: true
};
app.use(cors(corsOptions));

// Handle OPTIONS requests explicitly
app.options('*', cors(corsOptions));

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
const responseRoutes = require('./routers/response_routes');

app.use('/responses', responseRoutes);
app.use('/reviews', reviewRoutes);
app.use('/restaurants', restaurantRoutes);

module.exports = app;
