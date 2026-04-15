# Library Booking Mobile Application 
This project is a Booth Booking app developed for Bradford College Library 
This application is a mobile library booking system that allows students to view booths and create, edit, and cancel bookings, and receive notifications. 

## Description 

The application was developed using Kotlin and Jetpack Compose for the front-end, and Firebase Authentication and Firestore for the back-end. It provides real-time booking functionality, secure user management, and a user-friendly mobile interface. 

## Features 

- User registration and login  

- Viewing available booths  

- Real-time booking and database synchronisation 

- Dynamic generation of dates. 

- Booking, editing, and cancelling  

- Prevention of double bookings  

- Android notifications for booking actions with Android 13+ Permission Handling 

- User profile with active bookings  

- Interactive navigation drawer with expandable Guide and Terms and Conditions 

- Session persistence 

- Loading states and visual feedback 

- Responsive layout in booking screens 

## How the Application Meets Requirements 

**Real-time availability:** Booth availability is updated using Firestore queries to ensure accurate data and prevent booking conflicts. 

**Booking management**: Users can create, edit, and cancel bookings  

**User feedback:** Notifications for booking actions, error messages for invalid inputs and visual loading states. 

**Secure data handling:** Firebase Authentication restricts access to registered users, session persistence leaves the user logged in after restarting an app and Firestore security rules allow the users to access only their data.  

**Mobile usability:** The interface is designed using Jetpack Compose with simple navigation and responsive layouts that adapts to different screen sizes.  

## Getting Started 

### Dependencies 

- Android Studio  

- Android device or emulator  

- Internet connection  

- Firebase project setup  

### Installing 

1. Open the project in Android Studio 

2. Clone the repository in terminal:  

git clone https://github.com/Mykhail-Leonov/Library-Booking-App 

3. Connect Firebase 

### Executing Program 

1. Run the application from Android Studio  

2. Register a new account  

3. Log in  

4. Select a booth and create a booking  

## Help 

If the application does not run: 

- Ensure Firebase is connected correctly  

- Check internet connection  

- Verify Android Studio configuration  

 
## Version History 

v1.0.2 - Notifications added and improved usability 

v1.0.1 - Polished menu  

v1.0 - Fully working system  

v0.9 - Implemented booking functionality. 

v0.8 - Implemented loading of booths from Firestore. 

v0.7 - Connected real data to Profile from Firestore  

v0.6 - Integration of firebase authentication  

v0.5 - Finished UI with hardcoded testing 

 
## Acknowledgments 

- Firebase documentation (https://firebase.google.com/docs?utm_source=google&utm_medium=cpc&utm_campaign=Cloud-SS-DR-Firebase-FY26-global-gsem-1713590&utm_content=text-ad&utm_term=KW_firebase%20documentation&gclsrc=aw.ds&gad_source=1&gad_campaignid=23417478209&gbraid=0AAAAADpUDOj1VWawKF-OPpI5vONmxa7oc&gclid=CjwKCAjw7vzOBhBxEiwAc7WNr-hkSN8iBN2vavkreSVPK6kAgeaUeoxqcM-0Kmu21QilGH_jwnUhjBoChqQQAvD_BwE)

- Android developer documentation (https://developer.android.com/)  

- The template for readme.md was taken from DomPizzie (https://gist.github.com/DomPizzie/7a5ff55ffa9081f2de27c315f5018afc) 

- Google Gemini, (2026). Assisted in idea generation and explanation of Android development concepts [Software]. Available at: https://gemini.google.com 
