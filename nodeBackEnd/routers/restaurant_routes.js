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

// GET by cuisine
router.get('/cuisine/:cuisine', async (req, res) => {
    try {
        const restaurants = await Restaurant.find({ cuisine: req.params.cuisine });
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET by price
router.get('/price/:priceRange', async (req, res) => {
    try {
        const restaurants = await Restaurant.find({ priceRange: parseInt(req.params.priceRange) });
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET by business owner UID
router.get('/business/:uid', async (req, res) => {
    try {
        const uid = req.params.uid;
        const restaurants = await Restaurant.find({ businessUserId: uid });
        res.json({ data: restaurants });
    } catch (err) {
        res.status(500).json({ error: 'Failed to fetch restaurants' });
    }
});

// SEARCH restaurants
router.get('/search', async (req, res) => {
    const { query } = req.query;
    if (!query || query.trim() === '') {
        return res.status(400).json({ success: false, error: 'Search query is required' });
    }

    try {
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

// CREATE restaurant
router.post('/', async (req, res) => {
    try {
        const restaurant = new Restaurant(req.body);
        await restaurant.save();
        res.status(201).json({ message: 'Restaurant created', data: restaurant });
    } catch (err) {
        res.status(400).json({ error: 'Failed to create restaurant' });
    }
});

// ✅ PUT LAST — GET restaurant by ID
router.get('/:id', async (req, res) => {
    try {
        const restaurant = await Restaurant.findById(req.params.id);
        if (!restaurant) {
            return res.status(404).json({ message: 'Restaurant not found' });
        }
        res.json(restaurant);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

module.exports = router;
