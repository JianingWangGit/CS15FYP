require('dotenv').config();
const mongoose = require('mongoose');
const Restaurant = require('../models/restaurant_model.js');

const sampleRestaurants = [
    {
        name: "Pizza Place",
        description: "Best pizza in town",
        cuisine: "Italian",
        rating: 4.5,
        address: "123 Main St",
        imageUrl: "https://example.com/pizza.jpg",
        isOpen: true,
        priceRange: 2
    },
    {
        name: "Sushi Bar",
        description: "Fresh sushi daily",
        cuisine: "Japanese",
        rating: 4.8,
        address: "456 Oak Ave",
        imageUrl: "https://example.com/sushi.jpg",
        isOpen: true,
        priceRange: 3
    },
    {
        name: "Taco Fiesta",
        description: "Authentic Mexican",
        cuisine: "Mexican",
        rating: 4.2,
        address: "789 Pine Rd",
        imageUrl: "https://example.com/tacos.jpg",
        isOpen: false,
        priceRange: 1
    }
];

async function seedDatabase() {
    try {
        // Connect to MongoDB
        await mongoose.connect(process.env.MONGO_URI);
        console.log('Connected to MongoDB');

        // Clear existing data
        await Restaurant.deleteMany({});
        console.log('Cleared existing restaurants');

        // Insert sample data
        await Restaurant.insertMany(sampleRestaurants);
        console.log('Inserted sample restaurants');

        // Disconnect
        await mongoose.disconnect();
        console.log('Disconnected from MongoDB');
    } catch (error) {
        console.error('Error seeding database:', error);
    }
}

seedDatabase(); 