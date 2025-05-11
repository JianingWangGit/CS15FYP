const mongoose = require('mongoose');

const responseSchema = new mongoose.Schema({
  reviewId: {
    type: mongoose.Schema.Types.ObjectId,
    required: true,
    ref: 'Review'
  },
  email: {
    type: String,
    required: true
  },
  username: String,
  comment: {
    type: String,
    required: true
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('Response', responseSchema);