const express = require('express');
const router = express.Router();
const Restaurant = require('../models/restaurant_model');
const Review = require('../models/review_model');
const mongoose = require('mongoose');

// Helper function to update restaurant rating
async function updateRestaurantRating(restaurantId) {
    try {
        console.log('Updating rating for restaurant:', restaurantId);
        const allReviews = await Review.find({ restaurantId: new mongoose.Types.ObjectId(restaurantId) });
        console.log('Found reviews:', allReviews.length);
        
        if (allReviews.length > 0) {
            const avg = allReviews.reduce((sum, r) => sum + r.rating, 0) / allReviews.length;
            console.log('Calculated average rating:', avg);
            
            const updatedRestaurant = await Restaurant.findByIdAndUpdate(
                restaurantId,
                { rating: avg },
                { new: true }
            );
            console.log('Updated restaurant:', updatedRestaurant);
            return updatedRestaurant;
        }
    } catch (error) {
        console.error('Error updating restaurant rating:', error);
        throw error;
    }
}

// Helper function to update all restaurant ratings
async function updateAllRestaurantRatings() {
    try {
        console.log('Starting to update all restaurant ratings');
        const restaurants = await Restaurant.find();
        console.log('Found restaurants to update:', restaurants.length);
        
        for (const restaurant of restaurants) {
            try {
                await updateRestaurantRating(restaurant._id);
            } catch (error) {
                console.error(`Error updating rating for restaurant ${restaurant._id}:`, error);
            }
        }
        console.log('Finished updating all restaurant ratings');
    } catch (error) {
        console.error('Error updating all restaurant ratings:', error);
        throw error;
    }
}

// GET all restaurants
router.get('/', async (req, res) => {
    try {
        console.log('GET / - Updating all restaurant ratings');
        await updateAllRestaurantRatings();
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
        await updateAllRestaurantRatings();
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
        await updateAllRestaurantRatings();
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
        await updateAllRestaurantRatings();
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

module.exports = router; 