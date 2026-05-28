# Amadeus GDS Training Simulator

This is a free training simulator built to replicate the Amadeus Selling Platform Connect (SPC) terminal. I created it for travel agents, students, and anyone in the aviation industry who wants to practice real Amadeus GDS commands without needing access to a live system. It runs locally on your computer and gives you a realistic terminal experience to build confidence before working on the actual platform.

The backend is built with Java Spring Boot and MySQL, and the front end is a fully authentic terminal interface that looks and feels like the real thing.


## Created By

**Ahmed Emad Mohamed**
LinkedIn: [linkedin.com/in/ahmed-aiden-a13a9a22b](https://www.linkedin.com/in/ahmed-aiden-a13a9a22b)
Phone: +20 1119742859


## Screenshots

**Terminal Login Screen**
![Login Screen](screenshots/Screenshot_2026-05-28_045620.png)

**PNR Display**
![PNR Display](screenshots/Screenshot_2026-05-28_045920.png)

**TST - Fare Calculation per Passenger**
![TST Fare Calculation](screenshots/Screenshot_2026-05-28_045957.png)

**Split Screen Mode - Original vs New Ticket**
![Split Screen](screenshots/Screenshot_2026-05-28_050028.png)

**TWD - Ticket Display with Full PNR**
![TWD Ticket Display](screenshots/Screenshot_2026-05-28_050135.png)

**Fare Rules Display**
![Fare Rules](screenshots/Screenshot_2026-05-28_050216.png)


## What the Simulator Can Do

Once it is running, you can search for flight availability between cities, browse fares by route and airline, create and manage PNRs (Passenger Name Records), practice ticketing commands, and view fare rules. Every command you type gets logged per session so you can review your practice history.


## What You Need to Install

Before you can run the simulator, you need to install four things on your computer. Do not skip any of them.

| Tool | Purpose | Download |
|------|---------|----------|
| Java 17 | Runs the application | [adoptium.net](https://adoptium.net) |
| Maven | Builds the project | [maven.apache.org](https://maven.apache.org/download.cgi) |
| MySQL Community Server | The database | [dev.mysql.com/downloads](https://dev.mysql.com/downloads/mysql/) |
| MySQL Workbench | Visual database manager | [dev.mysql.com/downloads/workbench](https://dev.mysql.com/downloads/workbench/) |
| IntelliJ IDEA | Opens and runs the project | [jetbrains.com/idea](https://www.jetbrains.com/idea/download/) (the free Community Edition is fine) |


## How to Set It Up

### Step 1 - Set Up the Database in MySQL Workbench

Start here. The simulator needs a database to store flights, fares, and PNRs, and MySQL Workbench is how you set that up.

1. Install MySQL Community Server. During installation it will ask you to create a root password. Write it down, you will need it later.
2. Open MySQL Workbench.
3. On the home screen, click the "+" button next to "MySQL Connections" to add a new connection.
4. Fill in the following details:
   - Connection Name: amadeus
   - Hostname: localhost
   - Port: 3306
   - Username: root
5. Click "Test Connection", enter your root password when prompted, and it should confirm the connection was successful.
6. Click OK to save, then double-click the connection to open it.
7. In the top menu go to File, then Open SQL Script.
8. Find the project folder you downloaded and open the file inside the `sql` folder called `amadeus_workbench.sql`.
9. Click the lightning bolt button at the top of the editor to run the script.
10. After it runs, look at the left panel under "Schemas" and you should see `amadeus_db` listed there. That means the database is ready.


### Step 2 - Open the Project in IntelliJ IDEA

1. Open IntelliJ IDEA.
2. Click "Open" from the welcome screen, or go to File and then Open.
3. Browse to the project folder and select it, then click OK.
4. IntelliJ will detect that this is a Maven project and show a small popup asking if you want to load it. Click "Load Maven Project".
5. Wait for it to finish downloading all the dependencies. You will see a progress bar at the bottom of the screen. This can take a few minutes the first time, especially if your internet connection is slow.


### Step 3 - Add Your Database Password

The project does not store your password in the code for security reasons. You need to create a small local file that tells the app what your MySQL password is.

1. Inside IntelliJ, look at the left panel and navigate to: `src` then `main` then `resources`.
2. Right-click on the `resources` folder and choose New, then File.
3. Name the file exactly: `application-local.properties`
4. Open the file and paste in the following two lines:

```
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD_HERE
```

5. Replace `YOUR_PASSWORD_HERE` with the actual root password you set in Step 1.
6. Save the file.

Please do not upload this file to GitHub or share it with anyone. It contains your database credentials.


### Step 4 - Run the Application

1. In the left panel, navigate to: `src` then `main` then `java` then `com` then `amadeus` then `simulator`.
2. Open the file called `AmadeusSimulatorApplication.java`.
3. Click the green Run button at the top of IntelliJ (it looks like a play button).
4. Watch the console output at the bottom. When you see a line that says "Started AmadeusSimulatorApplication", the app is up and running.


### Step 5 - Open the Simulator in Your Browser

Open any browser (Chrome, Firefox, Edge, it does not matter) and go to:

```
http://localhost:8080
```

The Amadeus Selling Platform Connect terminal should load on your screen. You are ready to start practicing.


## How to Check That Everything Is Working

If the terminal loads and you can type commands, you are good to go. You can also open a new browser tab and go to:

```
http://localhost:8080/api/health
```

If the database is connected properly, you will see: `{"ok": true, "db": "MySQL OK"}`


## Project Structure

Here is a quick overview of how the files are organized, in case you want to explore or modify the project:

```
amadeus-simulator/
├── sql/
│   └── amadeus_workbench.sql        # Run this in MySQL Workbench first
├── src/
│   └── main/
│       ├── java/com/amadeus/simulator/
│       │   ├── AmadeusSimulatorApplication.java   # Main entry point
│       │   ├── controller/ApiController.java       # All API endpoints
│       │   └── config/DataInitializer.java         # Seeds the initial data
│       └── resources/
│           ├── application.properties              # App configuration
│           ├── schema.sql                          # Database schema
│           └── static/index.html                  # The terminal UI
└── pom.xml                                         # Maven dependencies
```


## Troubleshooting

**The app fails to start or shows a database error**
Make sure MySQL is actually running. Open MySQL Workbench and check that your connection has a green indicator. Also double-check that the password in your `application-local.properties` file matches the one you set during MySQL installation.

**It says port 8080 is already in use**
Something else on your computer is using that port. Open `application.properties` and change `server.port=8080` to `server.port=9090`. Then go to `http://localhost:9090` in your browser instead.

**IntelliJ cannot find Java 17**
Go to File, then Project Structure, then SDK, and point it to your Java 17 installation.

**The database schema did not load or amadeus_db is not showing in Workbench**
Go back to Step 1 and run the SQL script again manually. Make sure you click the lightning bolt to execute it and that no errors appeared in the output panel below the editor.


## License

This project is free to use for educational and training purposes.

## Advice..
Keep it up and do your best to learn a new stuff, Good Luck!!!!!!!! 
