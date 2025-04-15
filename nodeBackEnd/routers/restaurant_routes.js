const express = require('express');
const router = express.Router();
const Restaurant = require('../models/restaurant_model');

// GET all restaurants
router.get('/', async (req, res) => {
    try {
        const restaurants = await Restaurant.find();
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET restaurants by cuisine
router.get('/cuisine/:cuisine', async (req, res) => {
    try {
        const restaurants = await Restaurant.find({ cuisine: req.params.cuisine });
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET restaurants by price range
router.get('/price/:priceRange', async (req, res) => {
    try {
        const restaurants = await Restaurant.find({ priceRange: parseInt(req.params.priceRange) });
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// Search restaurants
router.get('/search', async (req, res) => {
    try {
        const { query } = req.query;
        const restaurants = await Restaurant.find({
            $or: [
                { name: { $regex: query, $options: 'i' } },
                { description: { $regex: query, $options: 'i' } },
                { cuisine: { $regex: query, $options: 'i' } }
            ]
        });
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

module.exports = router; 