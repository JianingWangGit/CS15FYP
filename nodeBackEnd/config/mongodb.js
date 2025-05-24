// File: nodeBackEnd/config/mongodb.js
const mongoose = require('mongoose');

async function connectDB(url) {
    try {
        await mongoose.connect(url);
        console.log('Connected Successfully to MongoDB');
        return 'Connected Successfully';
    } catch (err) {
        console.log(err);
        return 'Error Connecting to MongoDB';
    }
}

module.exports = connectDB;
