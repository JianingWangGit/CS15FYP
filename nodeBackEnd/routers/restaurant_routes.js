const express = require('express');
const router = express.Router();
const Restaurant = require('../models/restaurant_model');
const Review = require('../models/review_model');
const mongoose = require('mongoose');

// GET all restaurants
router.get('/', async (req, res) => {
    try {
        console.log('GET / - Updating all restaurant ratings');
        const restaurants = await Restaurant.find();
        console.log('Returning restaurants:', restaurants.length);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error in GET /:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET a restaurant by it's id
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

// GET restaurants by cuisine
router.get('/cuisine/:cuisine', async (req, res) => {
    try {
        console.log('GET /cuisine/:cuisine - Updating all restaurant ratings');
        const restaurants = await Restaurant.find({ cuisine: req.params.cuisine });
        console.log('Returning restaurants by cuisine:', restaurants.length);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error in GET /cuisine/:cuisine:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET restaurants by price range
router.get('/price/:priceRange', async (req, res) => {
    try {
        console.log('GET /price/:priceRange - Updating all restaurant ratings');
        const restaurants = await Restaurant.find({ priceRange: parseInt(req.params.priceRange) });
        console.log('Returning restaurants by price range:', restaurants.length);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error in GET /price/:priceRange:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// Search restaurants
router.get('/search', async (req, res) => {
    try {
        const { query } = req.query;
        console.log('GET /search - Updating all restaurant ratings');
        const restaurants = await Restaurant.find({
            $or: [
                { name: { $regex: query, $options: 'i' } },
                { description: { $regex: query, $options: 'i' } },
                { cuisine: { $regex: query, $options: 'i' } }
            ]
        });
        console.log('Returning search results:', restaurants.length);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error in GET /search:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

router.post('/', async (req, res) => {
  try {
    const restaurant = new Restaurant(req.body);
    await restaurant.save();
    res.status(201).json({ message: 'Restaurant created', data: restaurant });
  } catch (err) {
    console.error(err);
    res.status(400).json({ error: 'Failed to create restaurant' });
  }
});

// GET /restaurants/business/:uid
router.get('/business/:uid', async (req, res) => {
  try {
    const uid = req.params.uid;
    const restaurants = await Restaurant.find({ businessUserId: uid });
    res.json({ data: restaurants });
  } catch (err) {
    res.status(500).json({ error: 'Failed to fetch restaurants' });
  }
});

module.exports = router;