# TripMate - Mobile Travel Management Application
## Project Overview

*TripMate is a mobile application designed to simplify travel planning and collaboration. It allows users to create trips, manage itineraries, track expenses, invite participants, and interact through polls and notifications, all in one platform.*
**This project was developed as part of the academic requirements for MO-IT119 – Mobile Development Application.**

## Features
**Authentication & User Management**
- User Registration, Login, and Forgot Password
- Input validation (email format, required fields, password rules)
- Firebase Authentication integration
- Auto-login using session persistence
  
**Navigation System**
- Seamless navigation between screens using Android Navigation Component
- Bottom Navigation (Home, Create, Notifications, Profile)
- Tab Navigation (Participants, Polls, Expenses, Documents, Itinerary)

**Trip Management**
- Create, edit, and delete trips
- Date validation and conflict handling
- Dynamic trip display using RecyclerView

**Itinerary Management**
- Add, edit, and delete activities
- Date & time picker enforcement
- Prevention of past-date entries
- Dynamic updates using database integration

**Expense Management**
- Add and delete expenses
- Participant selection validation
- Mark expenses as settled
- Expense summary tracking

**Participant Management**
- Add, edit, and remove trip participants
- Validation for name and email inputs
- Dynamic updates via database

**Poll System**
- Create polls with multiple options
- Voting system (one vote per user)
- Real-time results display

**Notifications**
- Accept / Decline trip invitations
- Dynamic notification handling based on type
- Persistent notification storage

**Profile & Settings**
- Edit user profile (name, age, region)
- Profile image upload (Firebase Storage)
- Dynamic stats (Trips, Buddies, Countries Visited)
- Settings for password and preferences

**Document Management**
- Upload and view trip-related files
- File selection using modern Android APIs

## Technologies Used
**Language: Kotlin**
**Database (Local): Room**
**Backend (Cloud): Firebase (Auth, Firestore, Storage)**
**UI Components: RecyclerView, Navigation Component, ViewBinding**

## Installation
1. Clone the repository:
``
git clone https://github.com/giliannerose/TripMate.git
``
2. Open in Android Studio
3. Sync Gradle and install dependencies
4. Run the application on emulator or physical device

## Team
* Gillian Rose Baguio
* Krisna Danessa Jusay
* Shirly Rose Montes
