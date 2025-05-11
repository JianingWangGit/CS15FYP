const mongoose = require('mongoose');

const reviewSchema = new mongoose.Schema({
    userId: {
        type: String,
        required: true
        },
    restaurantId: {
        type: mongoose.Schema.Types.ObjectId,
        required: true,
        ref: 'Restaurant'
    },
    username: String,
    comment: String,
    rating: {
        type: Number,
        required: true,
    },
    photos: [String],
    createdAt: { type: Date, default: Date.now }
});

module.exports = mongoose.model('Review', reviewSchema);
