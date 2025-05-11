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
        default: 2,
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
    hours: {
        type: Map,
        of: {
            open: String,
            close: String
        },
        required: true
    },
    priceRange: {
        type: Number,
        required: true,
        min: 1,
        max: 4
    },
    businessUserId: { type: String, required: true },
});

module.exports = mongoose.model('Restaurant', restaurantSchema);