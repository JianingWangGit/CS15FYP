const express = require('express');

// White-box test app: injects custom routers
function makeApp({ reviewRouter, restaurantRouter, responseRouter }) {
  const app = express();
  app.use(express.json());

  if (reviewRouter) {
    app.use('/reviews', reviewRouter);
  }

  if (restaurantRouter) {
    app.use('/restaurants', restaurantRouter);
  }

  if (responseRouter) {
    app.use('/responses', responseRouter);
  }

  return app;
}

module.exports = makeApp;
