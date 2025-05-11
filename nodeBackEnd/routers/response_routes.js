const express = require('express');
const router = express.Router();
const Response = require('../models/response_model');

// GET all replies for a specific review
router.get('/:reviewId/replies', async (req, res) => {
  try {
    const replies = await Response.find({ reviewId: req.params.reviewId }).sort({ createdAt: -1 });
    res.json(replies);
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
});

// POST a new reply to a review
router.post('/:reviewId/reply', async (req, res) => {
  try {
    const { email, username, comment } = req.body;
    if (!email || !comment) {
      return res.status(400).json({ success: false, error: "Email and comment are required" });
    }

    const newReply = new Response({
      reviewId: req.params.reviewId,
      email,
      username,
      comment
    });

    await newReply.save();
    res.status(201).json({ success: true, reply: newReply });
  } catch (error) {
    res.status(500).json({ success: false, error: error.message });
  }
});

module.exports = router;