const chai = require('chai');
const sinon = require('sinon');
const supertest = require('supertest');
const proxyquire = require('proxyquire');
const makeApp = require('./helpers/makeApp');

const { expect } = chai;

describe('White-Box: Review Routes', () => {
  let ReviewStub, RestaurantStub, app, request;

  const setupApp = () => {
    const reviewRouter = proxyquire
      .noCallThru()
      .load('../routers/review_routes', {
        '../models/review_model': ReviewStub,
        '../models/restaurant_model': RestaurantStub
      });

    app = makeApp({ reviewRouter });
    request = supertest(app);
  };

  beforeEach(() => {
    RestaurantStub = {
      findByIdAndUpdate: sinon.stub()
    };
  });

  afterEach(() => sinon.restore());

  // T1.1: Create review and trigger rating update
  it('T1.1 - should create review and update restaurant rating', async () => {
    ReviewStub = function (data) {
      Object.assign(this, data);
      this.save = sinon.stub().resolves();
    };
    ReviewStub.find = sinon.stub().resolves([{ rating: 4 }, { rating: 2 }]);
    setupApp();

    const res = await request.post('/reviews').send({
      restaurantId: 'r1',
      userId: 'u1',
      rating: 3,
      username: 'TestUser',
      comment: 'Nice food!',
      photos: []
    });

    expect(res.status).to.equal(201);
    expect(res.body.success).to.be.true;
    const updateArgs = RestaurantStub.findByIdAndUpdate.firstCall.args;
    expect(updateArgs[0]).to.equal('r1');
    expect(updateArgs[1]).to.have.property('rating', 3); // (4+2)/2 = 3
  });

  // T1.2: Missing required fields
  it('T1.2 - should return 400 if required fields are missing', async () => {
    ReviewStub = function () { this.save = sinon.stub().resolves(); };
    ReviewStub.find = sinon.stub().resolves([]);
    setupApp();

    const res = await request.post('/reviews').send({ comment: 'Missing stuff' });
    expect(res.status).to.equal(400);
    expect(res.body.error).to.match(/Missing required fields/);
  });

  // T1.3: Internal DB error (save fails)
  it('T1.3 - should return 500 on internal error', async () => {
    ReviewStub = function () {
      this.save = sinon.stub().rejects(new Error('Simulated DB error'));
    };
    ReviewStub.find = sinon.stub().resolves([]);
    setupApp();

    const res = await request.post('/reviews').send({
      restaurantId: 'r2',
      userId: 'u2',
      rating: 4
    });

    expect(res.status).to.equal(500);
    expect(res.body.error).to.equal('Simulated DB error');
  });

  // T1.4: GET reviews by restaurantId
  it('T1.4 - should return reviews for a restaurant', async () => {
    ReviewStub = {
      find: sinon.stub().returns({
        sort: sinon.stub().returns({
          skip: sinon.stub().returns({
            limit: sinon.stub().resolves([{ rating: 5 }, { rating: 4 }])
          })
        })
      })
    };
    setupApp();

    const res = await request.get('/reviews?restaurantId=r1');
    expect(res.status).to.equal(200);
    expect(res.body).to.be.an('array');
    expect(res.body.length).to.equal(2);
  });

  // T1.5: DB error when fetching reviews
  it('T1.5 - should return 500 on DB error when getting reviews', async () => {
    ReviewStub = {
      find: sinon.stub().throws(new Error('Find failed'))
    };
    setupApp();

    const res = await request.get('/reviews?restaurantId=r1');
    expect(res.status).to.equal(500);
    expect(res.body.error).to.equal('Find failed');
  });

  // T1.6: GET /reviews/user with valid userId
  it('T1.6 - should return user reviews for given userId', async () => {
    ReviewStub = {
      find: sinon.stub().returns({
        sort: sinon.stub().returns({
          skip: sinon.stub().returns({
            limit: sinon.stub().resolves([{ rating: 3 }])
          })
        })
      })
    };
    setupApp();

    const res = await request.get('/reviews/user?userId=u1');
    expect(res.status).to.equal(200);
    expect(res.body).to.be.an('array');
    expect(res.body[0].rating).to.equal(3);
  });

  // T1.7: Missing userId in query
  it('T1.7 - should return 400 if userId is missing', async () => {
    ReviewStub = { find: sinon.stub() };
    setupApp();

    const res = await request.get('/reviews/user');
    expect(res.status).to.equal(400);
    expect(res.body.error).to.match(/Missing userId/);
  });

  // T1.8: DB error when fetching user reviews
  it('T1.8 - should return 500 if DB error occurs in user review fetch', async () => {
    ReviewStub = {
      find: sinon.stub().throws(new Error('User fetch failed'))
    };
    setupApp();

    const res = await request.get('/reviews/user?userId=u1');
    expect(res.status).to.equal(500);
    expect(res.body.error).to.equal('User fetch failed');
  });
});
