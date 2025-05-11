/**
 * @file review_routes.js
 * @description Routes for handling review-related requests
 */

const express = require('express');
const router = express.Router();
const Review = require('../models/review_model');
const Restaurant = require('../models/restaurant_model');

// GET all reviews
router.get('/', async (req, res) => {
    const limit = parseInt(req.query.limit) || 10;
    const skip = parseInt(req.query.skip) || 0;
    const { restaurantId } = req.query;

    try {
        const filter = restaurantId ? { restaurantId } : {};
        const reviews = await Review.find(filter)
            .sort({ createdAt: -1 })
            .skip(skip)
            .limit(limit);
        res.json(reviews);
    } catch (error) {
        res.status(500).json({ success: false, error: error.message });
    }
});

// POST a new review
router.post('/', async (req, res) => {
    try {
        const { restaurantId, userId, username, comment, rating, photos } = req.body;

        // Validate required fields
        if (!restaurantId || !rating || !userId) {
            return res.status(400).json({
                success: false,
                error: "Missing required fields: restaurantId, userId, or rating"
            });
        }

        // Create and save the new review
        const newReview = new Review({
            restaurantId,
            userId,
            username,
            comment,
            rating,
            photos
        });

        await newReview.save();

        // Update average rating for the restaurant
        const allReviews = await Review.find({ restaurantId });
        const avg = allReviews.reduce((sum, r) => sum + r.rating, 0) / allReviews.length;

        await Restaurant.findByIdAndUpdate(restaurantId, { rating: avg }, { new: true });

        res.status(201).json({ success: true, review: newReview });
    } catch (error) {
        console.error("Error submitting review:", error);
        res.status(500).json({ success: false, error: error.message });
    }
});

module.exports = router;
