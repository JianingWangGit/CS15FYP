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
        const { restaurantId, username, comment, rating, photos } = req.body;
        if (!restaurantId || !rating) {
            return res.status(400).json({ success: false, error: "Restaurant ID and rating are required" });
        }
        const newReview = new Review({ restaurantId, username, comment, rating, photos });
        await newReview.save();
        res.status(201).json({ success: true, review: newReview });
    } catch (error) {
        console.log(error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// GET all replies for a specific review
router.get('/:id/replies', async (req, res) => {
    try {
        const review = await Review.findById(req.params.id);
        if (!review) {
            return res.status(404).json({ success: false, error: "Review not found" });
        }
        res.json(review.replies);  // return only the replies array
    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: error.message });
    }
});


// POST a reply to a specific review
router.post('/:id/reply', async (req, res) => {
    try {
        const reviewId = req.params.id;
        const { username, comment } = req.body;

        if (!username || !comment) {
            return res.status(400).json({ success: false, error: "Username and comment are required" });
        }

        const review = await Review.findById(reviewId);
        if (!review) {
            return res.status(404).json({ success: false, error: "Review not found" });
        }

        review.replies.push({ username, comment });
        await review.save();

        const newReply = review.replies[review.replies.length - 1]; // Get the last inserted reply
        res.status(201).json({ success: true, reply: newReply });

    } catch (error) {
        console.error(error);
        res.status(500).json({ success: false, error: error.message });
    }
});

// UPDATE a reply
router.put('/:id/replies/:replyId', async (req, res) => {
    try {
      const { id, replyId } = req.params;
      const { comment, username, email } = req.body;  
      // find the review
      const review = await Review.findById(id);
      if (!review) return res.status(404).json({ error: 'Review not found' });
  
      // find the reply subdoc
      const reply = review.replies.id(replyId);
      if (!reply) return res.status(404).json({ error: 'Reply not found' });
  
      // authorize: only same email can edit
      if (reply.email !== email) return res.status(403).json({ error: 'Not allowed' });
  
      // perform update
      reply.comment = comment;
      await review.save();
      res.json({ success: true, reply });
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  });
  
  // DELETE a reply
  router.delete('/:id/replies/:replyId', async (req, res) => {
    try {
      const { id, replyId } = req.params;
      const { email } = req.body;  
      const review = await Review.findById(id);
      if (!review) return res.status(404).json({ error: 'Review not found' });
  
      const reply = review.replies.id(replyId);
      if (!reply) return res.status(404).json({ error: 'Reply not found' });
  
      if (reply.email !== email) return res.status(403).json({ error: 'Not allowed' });
  
      reply.remove();
      await review.save();
      res.json({ success: true });
    } catch (err) {
      res.status(500).json({ error: err.message });
    }
  });

module.exports = router;