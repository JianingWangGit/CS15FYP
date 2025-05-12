const express = require('express');
const router = express.Router();
const Restaurant = require('../models/restaurant_model');

// Middleware for logging all requests
router.use((req, res, next) => {
    console.log(`[${new Date().toISOString()}] ${req.method} ${req.originalUrl}`);
    next();
});

// GET all restaurants
router.get('/', async (req, res) => {
    try {
        console.log('Fetching all restaurants');
        const restaurants = await Restaurant.find();
        console.log(`Found ${restaurants.length} restaurants`);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error fetching all restaurants:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// SEARCH restaurants (must be before /:id)
router.get('/search', async (req, res) => {
    const { query } = req.query;
    console.log('Search request with query:', query);
    
    if (!query || query.trim() === '') {
        console.log('Empty search query');
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
        console.log(`Search found ${restaurants.length} restaurants`);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error searching restaurants:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET by business owner UID
router.get('/business/:uid', async (req, res) => {
    const uid = req.params.uid;
    console.log('Fetching restaurants for business user:', uid);
    
    try {
        const restaurants = await Restaurant.find({ businessUserId: uid });
        console.log(`Found ${restaurants.length} restaurants for business user ${uid}`);
        res.json({ data: restaurants });
    } catch (err) {
        console.error('Error fetching business restaurants:', err);
        res.status(500).json({ error: 'Failed to fetch restaurants' });
    }
});

// GET by cuisine
router.get('/cuisine/:cuisine', async (req, res) => {
    const cuisine = req.params.cuisine;
    console.log('Fetching restaurants by cuisine:', cuisine);
    
    try {
        const restaurants = await Restaurant.find({ cuisine: cuisine });
        console.log(`Found ${restaurants.length} restaurants for cuisine ${cuisine}`);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error fetching restaurants by cuisine:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET by price
router.get('/price/:priceRange', async (req, res) => {
    const priceRange = parseInt(req.params.priceRange);
    console.log('Fetching restaurants by price range:', priceRange);
    
    try {
        const restaurants = await Restaurant.find({ priceRange: priceRange });
        console.log(`Found ${restaurants.length} restaurants for price range ${priceRange}`);
        res.status(200).json({ success: true, data: restaurants });
    } catch (error) {
        console.error('Error fetching restaurants by price:', error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// CREATE restaurant
router.post('/', async (req, res) => {
    console.log('Creating new restaurant:', JSON.stringify(req.body, null, 2));
    
    try {
        const restaurant = new Restaurant(req.body);
        await restaurant.save();
        console.log('Restaurant created successfully:', restaurant._id);
        res.status(201).json({ message: 'Restaurant created', data: restaurant });
    } catch (err) {
        console.error('Error creating restaurant:', err);
        res.status(400).json({ error: 'Failed to create restaurant: ' + err.message });
    }
});

// UPDATE restaurant
router.put('/:id', async (req, res) => {
    const restaurantId = req.params.id;
    console.log('PUT request received for restaurant:', restaurantId);
    console.log('Request body:', JSON.stringify(req.body, null, 2));
    
    if (!restaurantId) {
        console.log('Error: Restaurant ID is missing');
        return res.status(400).json({ success: false, message: 'Restaurant ID is required' });
    }

    try {
        // First check if restaurant exists
        const existingRestaurant = await Restaurant.findById(restaurantId);
        if (!existingRestaurant) {
            console.log('Error: Restaurant not found:', restaurantId);
            return res.status(404).json({ success: false, message: 'Restaurant not found' });
        }

        console.log('Existing restaurant data:', JSON.stringify(existingRestaurant, null, 2));
        
        // Validate that all required fields are present in the request
        const requiredFields = ['name', 'description', 'cuisine', 'address', 'imageUrl', 'priceRange', 'businessUserId'];
        for (const field of requiredFields) {
            if (!req.body[field] && req.body[field] !== 0) {
                console.log(`Error: Missing required field '${field}'`);
                return res.status(400).json({ 
                    success: false, 
                    message: `Missing required field: ${field}` 
                });
            }
        }
        
        // Perform the update
        const restaurant = await Restaurant.findByIdAndUpdate(
            restaurantId,
            req.body,
            { new: true, runValidators: true }
        );
        
        if (!restaurant) {
            console.error('Error: Failed to update restaurant after validation');
            return res.status(500).json({ 
                success: false, 
                message: 'Failed to update restaurant after validation' 
            });
        }
        
        console.log('Restaurant updated successfully:', JSON.stringify(restaurant, null, 2));
        res.status(200).json(restaurant);
    } catch (err) {
        console.error('Error updating restaurant:', err);
        console.error('Error details:', {
            message: err.message,
            stack: err.stack,
            name: err.name
        });
        res.status(500).json({ 
            success: false, 
            message: 'Failed to update restaurant: ' + err.message 
        });
    }
});

// GET restaurant by ID (must be last)
router.get('/:id', async (req, res) => {
    const restaurantId = req.params.id;
    console.log('Fetching restaurant by ID:', restaurantId);
    
    try {
        const restaurant = await Restaurant.findById(restaurantId);
        if (!restaurant) {
            console.log('Restaurant not found:', restaurantId);
            return res.status(404).json({ message: 'Restaurant not found' });
        }
        console.log('Restaurant found:', JSON.stringify(restaurant, null, 2));
        res.json(restaurant);
    } catch (err) {
        console.error('Error fetching restaurant by ID:', err);
        res.status(500).json({ message: err.message });
    }
});

module.exports = router;
