const chai = require('chai');
const sinon = require('sinon');
const supertest = require('supertest');
const proxyquire = require('proxyquire');
const makeApp = require('./helpers/makeApp');

const { expect } = chai;

describe('White-Box: Restaurant Routes', () => {
  let RestaurantStub, app, request;

  const setupApp = () => {
    const restaurantRouter = proxyquire
      .noCallThru()
      .load('../routers/restaurant_routes', {
        '../models/restaurant_model': RestaurantStub
      });

    app = makeApp({ restaurantRouter });
    request = supertest(app);
  };

  beforeEach(() => {
    RestaurantStub = {
      find: sinon.stub(),
      findById: sinon.stub(),
      findByIdAndUpdate: sinon.stub(),
      save: sinon.stub(),
      create: sinon.stub(),
    };
  });

  afterEach(() => sinon.restore());

  const validPayload = {
    name: 'UpdatedName',
    description: 'UpdatedDesc',
    cuisine: 'Italian',
    address: '123 St',
    imageUrl: 'http://image.jpg',
    priceRange: 2,
    hours: {},
    businessUserId: 'biz1'
  };

  it('T2.1 - should update restaurant successfully', async () => {
    RestaurantStub.findById.resolves({ _id: 'r1' });
    RestaurantStub.findByIdAndUpdate.resolves(validPayload);

    setupApp();
    const res = await request.put('/restaurants/r1').send(validPayload);
    expect(res.status).to.equal(200);
    expect(res.body.name).to.equal('UpdatedName');
  });

  it('T2.2 - should return 400 if required field missing', async () => {
    RestaurantStub.findById.resolves({ _id: 'r1' });

    setupApp();
    const payload = { ...validPayload };
    delete payload.name;

    const res = await request.put('/restaurants/r1').send(payload);
    expect(res.status).to.equal(400);
    expect(res.body.message).to.match(/Missing required field: name/);
  });

  it('T2.3 - should return 404 if restaurant not found', async () => {
    RestaurantStub.findById.resolves(null);

    setupApp();
    const res = await request.put('/restaurants/r1').send(validPayload);
    expect(res.status).to.equal(404);
  });

  it('T2.4 - should return 500 if update fails', async () => {
    RestaurantStub.findById.resolves({ _id: 'r1' });
    RestaurantStub.findByIdAndUpdate.rejects(new Error('Update failed'));

    setupApp();
    const res = await request.put('/restaurants/r1').send(validPayload);
    expect(res.status).to.equal(500);
  });

  it('T2.5 - should fetch all restaurants', async () => {
    RestaurantStub.find.resolves([{ name: 'Test Restaurant' }]);

    setupApp();
    const res = await request.get('/restaurants');
    expect(res.status).to.equal(200);
    expect(res.body.data).to.be.an('array');
  });

  it('T2.6 - should handle DB error fetching restaurants', async () => {
    RestaurantStub.find.rejects(new Error('DB error'));

    setupApp();
    const res = await request.get('/restaurants');
    expect(res.status).to.equal(500);
  });

  it('T2.7 - should search restaurants successfully', async () => {
    RestaurantStub.find.resolves([{ name: 'Pizza' }]);

    setupApp();
    const res = await request.get('/restaurants/search?query=pizza');
    expect(res.status).to.equal(200);
    expect(res.body.data).to.have.length(1);
  });

  it('T2.8 - should return 400 for empty search query', async () => {
    setupApp();
    const res = await request.get('/restaurants/search');
    expect(res.status).to.equal(400);
  });

  it('T2.9 - should handle DB error during search', async () => {
    RestaurantStub.find.rejects(new Error('DB error'));

    setupApp();
    const res = await request.get('/restaurants/search?query=test');
    expect(res.status).to.equal(500);
  });

  it('T2.10 - should fetch restaurants by business user', async () => {
    RestaurantStub.find.resolves([{ name: 'Biz Restaurant' }]);

    setupApp();
    const res = await request.get('/restaurants/business/biz1');
    expect(res.status).to.equal(200);
    expect(res.body.data).to.have.length(1);
  });

  it('T2.11 - should fetch restaurants by cuisine', async () => {
    RestaurantStub.find.resolves([{ cuisine: 'Thai' }]);
    setupApp();
    const res = await request.get('/restaurants/cuisine/Thai');
    expect(res.status).to.equal(200);
  });

  it('T2.12 - should handle DB error for cuisine search', async () => {
    RestaurantStub.find.rejects(new Error('DB error'));
    setupApp();
    const res = await request.get('/restaurants/cuisine/Thai');
    expect(res.status).to.equal(500);
  });

  it('T2.13 - should fetch restaurants by price range', async () => {
    RestaurantStub.find.resolves([{ priceRange: 2 }]);
    setupApp();
    const res = await request.get('/restaurants/price/2');
    expect(res.status).to.equal(200);
  });

  it('T2.14 - should handle DB error for price range search', async () => {
    RestaurantStub.find.rejects(new Error('DB error'));
    setupApp();
    const res = await request.get('/restaurants/price/2');
    expect(res.status).to.equal(500);
  });

  // T2.15 - Create a restaurant successfully
  it('T2.15 - should create a new restaurant successfully', async () => {
    RestaurantStub = function (data) {
      Object.assign(this, data);
      this.save = sinon.stub().resolves(); // simulate DB save success
    };

    setupApp();
    const res = await request.post('/restaurants').send({
      name: 'New Resto',
      description: 'Tasty food',
      cuisine: 'Thai',
      address: '123 Lane',
      imageUrl: 'http://img.png',
      priceRange: 2,
      hours: {},
      businessUserId: 'biz9'
    });

    expect(res.status).to.equal(201);
    expect(res.body.message).to.equal('Restaurant created');
  });

  it('T2.16 - should fail to create a restaurant with invalid payload', async () => {
    RestaurantStub.create = sinon.stub().rejects(new Error('Validation error'));
    setupApp();
    const res = await request.post('/restaurants').send({});
    expect(res.status).to.equal(400);
  });

  it('T2.17 - should fetch restaurant by ID successfully', async () => {
    RestaurantStub.findById.resolves(validPayload);
    setupApp();
    const res = await request.get('/restaurants/r1');
    expect(res.status).to.equal(200);
  });

  it('T2.18 - should return 404 if restaurant by ID not found', async () => {
    RestaurantStub.findById.resolves(null);
    setupApp();
    const res = await request.get('/restaurants/r999');
    expect(res.status).to.equal(404);
  });

  it('T2.19 - should handle DB error fetching by ID', async () => {
    RestaurantStub.findById.rejects(new Error('DB error'));
    setupApp();
    const res = await request.get('/restaurants/r1');
    expect(res.status).to.equal(500);
  });
});
