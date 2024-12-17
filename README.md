# Java Chat Application

Branch 'v2proj' has the updated code

## Overview

This Java-based chat application allows multiple clients to connect to a single server, enabling communication and broadcasting messages within a closed network. It is designed for use cases like corporate environments where employees need to log their daily updates or scrum activities securely.

## Prerequisites

- **IDE**: Eclipse (or compatible Java IDE)
- **JDK**: Java Development Kit (v8 or later)
- **Dependencies**: SQLite JAR file (already included in the project)

## Setup Instructions

1. **Unzip the Project**  
   After downloading and unzipping the project folder, open it in Eclipse.

2. **Project Structure**  
   The project follows the following directory structure:
```
   src/main/java
    ├── client
    ├── server
    └── shared
```
- The SQLite JAR file is already included, and dependencies have been configured.

3. **Running the Server**  
- Navigate to `src/main/java` > `server` > `com.projex.javafx_chat.server`.  
- Open `main.java` and run it as a Java application to start the server.

4. **Running the Clients**  
- Navigate to `src/main/java` > `client` > `com.projex.javafx_chat.client`.  
- Open `main.java` and run it as a Java application.  
- Run the client multiple times (twice for two clients, thrice for three clients, etc.) to simulate multiple users.

## Testing the Application

### 1. **Login Functionality**  
- Use the following test credentials to log in:  
  - **Username**: `testuser`  
  - **Password**: `password123`  
- Click the **Login** button to log in.  
- Once logged in, you can send messages, which will be broadcasted to the server dashboard.

### 2. **User Registration**  
- Click **Sign Up** to navigate to the registration page.  
- Provide a username and password (no restrictions).  
- After successful registration, the system will automatically redirect you to the login page.  
- Use the newly created credentials to log in.

> **Note**: If a username already exists, an alert will notify the user. Click **Back to Login** to return to the login page.

### 3. **Server Dashboard Features**  
- **Broadcast Messages**: All messages sent by clients will appear in the server dashboard.  
- **Retrieve Chats**:  
  - Select a user from the dropdown menu.  
  - Click **Retrieve Chats** to display all messages sent by that user in the text area below.  
- **Send All Text**:  
  - After retrieving messages, click **Send All Text** to forward all messages back to the selected user.

### 4. **Client Logout**  
- On the client window, click **Logout**. The client will log out, and the window will close automatically.

### 5. **Starting/Stopping the Server**  
- **Stop Server**: Clicking this button will halt server operations without closing the server window.  
- **Start Server**: Clicking this button will restart the server.

## Intention

This chat application is intended for closed networks such as companies where employees use cloud-based or virtual machine (VM) systems. Key functionalities include:

- **Daily Work Logs**: Employees can send their daily updates and scrum logs to the server.  
- **Secure Chat History Retrieval**: Server admins can retrieve and send back previous messages to users upon request.  
- **Broadcast Messaging**: Allows broadcasting messages to all online users simultaneously.

## Features

- Multi-client support for simultaneous messaging.  
- Secure login and user registration system.  
- Admin-controlled chat retrieval and broadcasting.  
- Start/stop server functionality for administrative control.  

## How It Works

1. **Server**: Central hub for communication and message storage.  
2. **Clients**: Users interact with the server to send and receive messages.  
3. **SQLite Integration**: Stores chat logs and user credentials locally.  

This project provides a foundation for secure, localized messaging within a controlled environment.
