/**
 * @file review_routes.js
 * @description Routes for handling review-related requests
 */

const express = require('express');
const router = express.Router();
const Review = require('../models/review_model'); // Import the Review model

// GET all reviews
router.get('/', async (req, res) => {
    const limit = parseInt(req.query.limit) || 10;
    const skip = parseInt(req.query.skip) || 0;

    const reviews = await Review.find()
        .sort({ createdAt: -1 })
        .skip(skip)
        .limit(limit);

    res.json(reviews);
});

// POST a new review
router.post('/', async (req, res) => {
    try {
        const { username, comment, rating, photos } = req.body;
        if (!rating) {
            return res.status(400).json({ success: false, error: "Rating is required" });
        }
        const newReview = new Review({ username, comment, rating, photos });
        await newReview.save();

        res.status(201).json({ success: true, review: newReview });
    } catch (error) {
        console.log(error);
        res.status(500).json({ success: false, error: error.message });
    }
});

module.exports = router;