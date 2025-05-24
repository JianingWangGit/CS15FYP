// test/response.test.js
const chai = require('chai');
const sinon = require('sinon');
const supertest = require('supertest');
const proxyquire = require('proxyquire');
const makeApp = require('./helpers/makeApp');

const { expect } = chai;

describe('White-Box: Response Routes', () => {
  let ResponseStub, app, request;

  const setupApp = () => {
    const responseRouter = proxyquire
      .noCallThru()
      .load('../routers/response_routes', {
        '../models/response_model': ResponseStub
      });

    app = makeApp({ responseRouter });
    request = supertest(app);
  };

  afterEach(() => sinon.restore());

  // T3.1 - Post reply success
  it('T3.1 - should create a reply successfully', async () => {
    ResponseStub = function (data) {
      Object.assign(this, data);
      this.save = sinon.stub().resolves();
    };
    setupApp();

    const res = await request.post('/responses/abc123/reply').send({
      email: 'owner@biz.com',
      username: 'Owner',
      comment: 'Thanks for the feedback!'
    });

    expect(res.status).to.equal(201);
    expect(res.body.success).to.be.true;
    expect(res.body.reply).to.include({ comment: 'Thanks for the feedback!' });
  });

  // T3.2 - Post reply with missing required fields
  it('T3.2 - should return 400 if required fields are missing', async () => {
    ResponseStub = function () {
      this.save = sinon.stub().resolves();
    };
    setupApp();

    const res = await request.post('/responses/abc123/reply').send({
      username: 'Owner'
    });

    expect(res.status).to.equal(400);
    expect(res.body.success).to.be.false;
    expect(res.body.error).to.match(/Email and comment are required/);
  });

  // T3.3 - Internal DB error while posting reply
  it('T3.3 - should return 500 on DB error when posting reply', async () => {
    ResponseStub = function () {
      this.save = sinon.stub().rejects(new Error('DB save error'));
    };
    setupApp();

    const res = await request.post('/responses/abc123/reply').send({
      email: 'owner@biz.com',
      comment: 'Appreciate it!'
    });

    expect(res.status).to.equal(500);
    expect(res.body.error).to.equal('DB save error');
  });

  // T3.4 - Get replies for review
  it('T3.4 - should return replies for a review', async () => {
    ResponseStub = {
      find: sinon.stub().returns({ sort: sinon.stub().resolves([{ comment: 'Thanks!' }]) })
    };
    setupApp();

    const res = await request.get('/responses/abc123/replies');
    expect(res.status).to.equal(200);
    expect(res.body).to.be.an('array');
    expect(res.body[0].comment).to.equal('Thanks!');
  });

  // T3.5 - DB error when fetching replies
  it('T3.5 - should return 500 if DB error occurs when getting replies', async () => {
    ResponseStub = {
      find: sinon.stub().throws(new Error('Find error'))
    };
    setupApp();

    const res = await request.get('/responses/abc123/replies');
    expect(res.status).to.equal(500);
    expect(res.body.success).to.be.false;
    expect(res.body.error).to.equal('Find error');
  });
});
