// GET all reviews
router.get('/', async (req, res) => {
    const reviews = await Review.find().sort({ createdAt: -1 });
    res.json(reviews);
});

// POST a new review
router.post('/', async (req, res) => {
    try {
        const { username, comment, rating } = req.body;
        const newReview = new Review({ username, comment, rating });
        await newReview.save();
        res.status(201).json({ success: true, review: newReview });
    } catch (error) {
        console.log(error);
        res.status(500).json({ success: false, error: error.message });
    }
});

module.exports = router;