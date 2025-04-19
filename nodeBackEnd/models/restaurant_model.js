const mongoose = require('mongoose');

const restaurantSchema = new mongoose.Schema({
    name: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    cuisine: {
        type: String,
        required: true
    },
    rating: {
        type: Number,
        required: true,
        min: 0,
        max: 5
    },
    address: {
        type: String,
        required: true
    },
    imageUrl: {
        type: String,
        required: true
    },
    isOpen: {
        type: Boolean,
        required: true,
        default: true
    },
    priceRange: {
        type: Number,
        required: true,
        min: 1,
        max: 4
    }
});

module.exports = mongoose.model('Restaurant', restaurantSchema); 