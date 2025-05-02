# CS15-FYP Full-Stack Review Platform

Final Year Project for FIT3162 units at Monash University, Australia.
This project is an Android-based food review platform for Monash University students and staff. 
It supports posting reviews with star ratings, text, and optional images. Business owners can respond to each review.

## 🔌 Technologies Used
(We are not upload the google-service-account.json file for the security reason, If you put that file under app folder everything will work fine)
- **Frontend**: Android Studio (Java + XML) 
- **Authentication**: Firebase Authentication
- **Backend**: Node.js + Express
- **Database**: MongoDB Atlas
- **Image Storage**: Firebase Storage
- **Communication**: RESTful API (future: hosted on Google Cloud)

Firebase **Storage** to upload and host review images. This prevents oversized payload issues with MongoDB.

## 📁 Project Structure

```plaintext
    CS-15-FYP/
    ├── app/                                # Android frontend (Java + XML)
    │   └── src/                            # Source code for Android app
    │       └── main/                       
    │           ├── java/com/example/cs_15_fyp/     # logic for Android app
    │           │   ├── activities/
    │           │   │   ├── MainActivity
    │           │   │   ├── InfoRestaurantActivity
    │           │   │   ├── GiveReviewActivity
    │           │   │   ├── AllReviewsActivity
    │           │   │   ├── ReviewDetailActivity
    │           │   │   ├── LoginActivity
    │           │   │   ├── SignUpActivity
    │           │   │   ├── ChangeEmailActivity
    │           │   │   └── ChangePasswordActivity
    │           │   ├── fragments/
    │           │   │   ├── RestaurantSearchFragment
    │           │   │   ├── NotificationsFragment
    │           │   │   └── UserProfileFragment
    │           │   ├── adapters/
    │           │   │   ├── ReviewAdapter.java
    │           │   │   ├── RestaurantAdapter.java
    │           │   │   └── ImagePagerAdapter.java
    │           │   ├── models/
    │           │   │   ├── Review.java
    │           │   │   ├── Restaurant.java
    │           │   │   └── ApiResponse.java
    │           │   └── api/
    │           │       ├── ApiClient.java
    │           │       ├── ReviewApi.java
    │           │       └── RestaurantService.java
    │           └── res/                            # Resources for Android app (UI elements)
    │               ├── layout/             # XML layout files for activities, fragments, and item views
    │               └── drawable/           # Icons, backgrounds, buttons, and image placeholders
    │ 
    ├── nodeBackEnd/                        # Node.js backend (Express + MongoDB)
    │   ├── config/
    │   │   └── mongodb.js                  # MongoDB connection config
    │   ├── models/
    │   │   ├── review_model.js             # Mongoose schema for reviews
    │   │   └── restaurant_model.js         # Mongoose schema for restaurants
    │   ├── routers/
    │   │   ├── review_routes.js            # Review-related API endpoints
    │   │   └── restaurant_routes.js        # Restaurant-related API endpoints
    │   ├── scripts/
    │   │   └── seedRestaurants.js          # Script to populate DB with sample restaurants
    │   ├── app.js                          # Express app configuration
    │   ├── server.js                       # Server entry point
    │   ├── .env                            # Environment config (e.g., MONGO_URI, PORT)
    │   ├── .gitignore
    │   ├── package.json
    │   └── package-lock.json
    
    ├── gradle.properties                   # Gradle configuration for Android
    ├── build.gradle
    ├── settings.gradle
    └── README.md                           # Project documentation
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
