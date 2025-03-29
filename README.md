# CS15-FYP Full-Stack Review Platform

This project contains two parts:

- `app/`: Android frontend built with Android Studio (Java + XML)
- `nodeBackEnd/`: Node.js backend using Express + MongoDB Atlas (hosted on Google Cloud server in future)

---

## 📁 Project Structure

```plaintext
CS-15-FYP/
├── app/                      # Android frontend (Java + XML)
│   └── src/
│       └── main/
│           ├── java/com/example/cs_15_fyp/  # Java activity & adapter classes
│           └── res/
│               ├── layout/                  # XML layouts
│               └── drawable/                # Images, icons, backgrounds
│
├── nodeBackEnd/              # Node.js backend API
│   ├── config/               # MongoDB connection setup
│   ├── models/               # (Optional) Mongoose schemas
│   ├── routes/               # (Optional) Route handlers
│   ├── app.js                # Express app logic (middleware, routes)
│   ├── server.js             # Entry point to start the server
│   ├── .env                  # Environment variables (MongoDB URI, PORT)
│   ├── .gitignore            # Ignore node_modules, .env, etc.
│   └── package.json          # Node.js dependencies

---

## 🛠️ Prerequisites

Make sure the following are installed:

- [Node.js or Nodemon]
- [npm]
- [MongoDB Atlas]

---

## Running the Backend Locally

### 1. Clone this repository

```bash
git clone <your-git-repo-url>
cd CS-15-FYP/nodeBackEnd
http://localhost:3000 for current local testing
```
### 2. Install dependencies
```bash
npm install
```
### 3. Start the server
```bash
node server.js (or install nodemon) nodemon server.js
```
### 4. Test the server
http://localhost:portNumber
