# CS15-FYP Full-Stack Review Platform

Final Year Project for FIT3162 units at Monash University, Australia.
This project is an Android-based food review platform for Monash University students and staff. 
It supports posting reviews with star ratings, text, and optional images. Business owners can respond to each review.


## 🔌 Technologies Used

- **Frontend**: Android Studio (Java + XML) +  Firebase Storage
- **Backend**: Node.js + Express
- **Database**: MongoDB Atlas
- **Image Storage**: Firebase Storage
- **Communication**: RESTful API (future: hosted on Google Cloud)

Firebase **Storage** to upload and host review images. This prevents oversized payload issues with MongoDB.

## 📁 Project Structure

```plaintext
CS-15-FYP/
    ├── app/                      # Android frontend (Java + XML)
    │   └── src/
    │       └── main/
    │           ├── java/com/example/cs_15_fyp/  # Java activity & adapter classes
    │               ├── activities/
    │                   ├──MainActivity
    │                   ├──InfoRestaurantActivity
    │                   ├──GiveReviewActivity
    │                   ├── AllReviewsActivity
    │               ├── fragments/
    │                   ├──RestaurantSearchFragment
    │                   ├──NotificationsFragment
    │                   ├──UserProfileFragment
    │
    │               ├── adapters/               # RecyclerView adapters
    │                   ├── ReviewAdapter.java
    │               ├── models/                 # Data models
    │                   ├── Review.java
    │               ├── api/                    # Retrofit API interfaces
    │                   ├── ApiClient.java
    │                   ├── ReviewApi.java
    │
    │           └── res/
    │               ├── layout/                 # XML layouts
    │                   ├── activity_info_restaurant.xml
    │                   ├── activity_give_review.xml
    │                   ├── activity_all_reviews.xml
    │                   ├── item_review.xml     # Layout for individual review items
    │
    │               └── drawable/               # Images, icons, backgrounds
    │                   ├── ic_launcher.png     # App launcher icon
    │                   ├── background_main.xml # XML drawable for main screen background
    │                   ├── btn_rounded.xml     # XML drawable for rounded buttons
    │                   ├── star_filled.png     # Icon for filled star (used in ratings)
    │                   ├── star_empty.png      # Icon for empty star (used in ratings)
    │                   ├── placeholder.png     # Placeholder image for reviews without images
    │
    ├── nodeBackEnd/              # Node.js backend API
    │   ├── config/               # MongoDB connection setup
    │       └── mongodb.js
    │   ├── models/               # Mongoose schemas (e.g., Review)
    │       ├── review.js         # Review schema
    │   ├── routes/               # Route handlers (e.g., reviewRoutes.js)
    │       ├── reviewRoutes.js
    │   ├── app.js                # Express app logic (middleware, routes)
    │   ├── server.js             # Entry point to start the server
    │   ├── .env                  # Environment variables (MongoDB URI, PORT)
    │   ├── .gitignore            # Ignore node_modules, .env, etc.
    │   ├── package.json          # Node.js dependencies
    │   └── package-lock.json     # Node.js dependency lock file
    |   ├── gradle.properties       # Gradle properties for Android project
    |   ├── build.gradle            # Gradle build file for Android project
    |   ├── settings.gradle         # Gradle settings file for Android project
    └── README.md                # Project documentation
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
