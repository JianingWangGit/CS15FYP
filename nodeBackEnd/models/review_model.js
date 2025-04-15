const mongoose = require('mongoose');

const reviewSchema = new mongoose.Schema({
    username: String,
    comment: String,
    rating: {
        type: Number,
        required: true,
    },
    photos: [String], // List of URLs or Base64 strings
    createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Review', reviewSchema);
