# Examination project Michael De Stefano

Mixologic - An application for sharing and creating drink recipes.

## Description

The application is built with Kotlin(1.4.31) and using Firebase(8.4.3).

Full list of dependencies and packages used can be found in './app/build.gradle'.

Mixologic sought to provide an easy to use experience where mixologists could share their drink-creations. The application was to be easy to use and provide users with filtering options that allowed them to find new recipes based on the ingredients they have at home.

## Project file structure

### Main structure
The project is separated into four main packages (all located in 'mixologic/app/src/main/java/com/example/mixologic'). 
These are:

- './application': Contains the custom application class.
- './data: Contains the applications data classes.
- './features: Contains the applications features. Each feature is its own screen in the application and its package contains its activity or fragment, viewmodel and eventual adapter.
- './managers: Contains the applications managers. These are used to handle fetches from databases, encrypt and sort.

### Specific packages
The following packages are considered to need further explanation:
- './features/splash' : The SplashActivity is launched upon application start. Its role is to determine the users logged in status and either direct to MainActivity if the user is logged in or LoginActivity if he/she isn't.
- './features/main' : The Main feature is the wrapper for the applications logged in state. The MainActivity displays the applications other main features as fragments inside its viewpager.
- './data/room : The package for Room contains repositories, DAO:s and the RoomDatabase that the application uses to cache data.

## Authors

Contributor name and contact info

Michael De Stefano (https://github.com/dondestefano)
