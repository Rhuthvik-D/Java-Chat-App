## Java Chat Application

The branch 'v2proj' is the branch with the updated code

- Once zip is obtained, unzip the project. Open it in Eclipse. In the project, there is also SQLite jar file included. The JAR dependencies have already been added.

- In src/main/java, there are subfolders, namely client, server and shared. Open the "main.java" in com.projex.javafx_chat.sever. Run it as a java application.

- In src/main/java, go to client folder, open "main.java" in com.projex.javafx_chat.client and run it as a java application. Run it twice to get two clients. Run it thrice to get three clients.

- Once all the clients are up and running, if login is to be tested, use the credentials "testuser" as username and "password123" as password. Click "login" to login with the credentials. Once logged in, send any message to the server. The message will be seen as a broadcast in the server dashboard. 

- If register user is to be tested, click "Sign up" to go to register page. Give the necessary credentials. There are no restrictions posed in username and password. Once registered, it will automatically reditects to login. Enter the new credentials to access the client window.

- If a username is already present, it will alert the user about the existing user. Close the alert and click "Back to Login" and login with credentials.

- In server dashboard, select a user from the dropdown and click "retrieve chats" to get all the texts sent by that user. The texts will be seen in the text area below. 

- Send all the texts to that selected user by clicking "Send All Text".

- If logout is clicked on the client window, the client will logout and the window will close automaically.

- If "stop server" is clicked on server dashboard, the server will stop functioning but will not close. If "start server" is clicked, then the server will start to run again. 

## Intention

With this application we can open multiple client windows and one singular server to text and send broadcasts to all online users.

The purpose of this project is to create a secure logging chat for a closed network such as a company whose employees work on a cloud or VM system. Using this chat app, the employees can send their daily work log and scrum updates to the server and have it saved in the server. 

If they ever need to see the previous updates or broadcast they can ask the server admin to retrieve their chats and send it back to them.
