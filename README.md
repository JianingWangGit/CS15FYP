# CS15-FYP Full-Stack Review Platform

Final Year Project for FIT3162 at Monash University, Australia.  
This is an Android-based food review platform designed for Monash students and staff.  
It allows users to post reviews with star ratings, comments, and optional images. Business owners can log in and respond to each review.

---

## 🏗️ Software Structure

Our system follows a **client-server architecture**:

- **Frontend**: Android app developed using Java and XML in Android Studio. It communicates with the backend via Retrofit using RESTful APIs.
- **Backend**: A Node.js + Express server that handles API requests and business logic.
- **Database**: MongoDB Atlas for storing restaurant and review data.
- **Authentication**: Firebase Authentication for secure login (email/password).
- **Image Hosting**: Firebase Storage for uploading and retrieving review images.

This modular architecture ensures separation of concerns between UI, logic, and data management.

---

## 🔌 Technologies Used

> ⚠️ The `google-services.json` file is excluded for security reasons. You must place it in the `app/` directory for Firebase features to work.

- **Frontend**: Android Studio (Java + XML)
- **Authentication**: Firebase Authentication
- **Backend**: Node.js + Express
- **Database**: MongoDB Atlas
- **Image Storage**: Firebase Storage
- **Communication**: RESTful API (future hosting planned on Google Cloud)

---

## 📁 Project Structure

```plaintext
CS-15-FYP/
├── app/
│   └── src/
│       └── main/
│           ├── java/com/example/cs_15_fyp/
│           │   ├── activities/              # Activity screens
│           │   ├── fragments/               # Search, notifications, profile
│           │   ├── adapters/                # RecyclerView + ViewPager
│           │   ├── models/                  # Review, Restaurant, ApiResponse
│           │   └── api/                     # Retrofit client and interfaces
│           └── res/
│               ├── layout/                  # XML layout files
│               └── drawable/                # App icons and assets
│
├── nodeBackEnd/                             # Node.js + Express backend
│   ├── config/                              # DB config
│   ├── models/                              # Mongoose schemas
│   ├── routers/                             # API routes
│   ├── scripts/                             # DB seeding script
│   ├── app.js                               # Express app setup
│   ├── server.js                            # Server entry point
│   ├── .env                                 # Environment variables
│   └── package.json
│
├── gradle.properties                        # Android Gradle config
├── build.gradle
├── settings.gradle
└── README.md                                # Project documentation
```

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
http://localhost:2001 for current local testing
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
