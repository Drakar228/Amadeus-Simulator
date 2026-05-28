# Amadeus GDS Training Simulator

A free, open-source simulator that replicates the **Amadeus Selling Platform Connect (SPC)** terminal — designed to help travel agents, students, and aviation enthusiasts practice real Amadeus GDS commands without needing a live system.

Built with **Java Spring Boot** + **MySQL** + a fully authentic terminal UI.

---

## Created By

**Ahmed Emad Mohamed**  
LinkedIn: [linkedin.com/in/ahmed-aiden-a13a9a22b](https://www.linkedin.com/in/ahmed-aiden-a13a9a22b)  
Phone: +20 1119742859

---

## Application Features

- Search for flight **availability** between cities
- Browse **fares** by route, airline, and cabin class
- **Create, view, edit, and search PNRs** (Passenger Name Records)
- Practice real Amadeus commands in an authentic terminal environment
- All training commands are **logged per session** for review

---

## Prerequisites

Before running the simulator, make sure you have these 4 tools installed on your computer:

| Tool | What it's for | Download |
|------|--------------|----------|
| **Java 17** | Runs the application | [adoptium.net](https://adoptium.net) |
| **Maven** | Builds the project | [maven.apache.org](https://maven.apache.org/download.cgi) |
| **MySQL Community Server** | The database | [dev.mysql.com/downloads](https://dev.mysql.com/downloads/mysql/) |
| **MySQL Workbench** | Manage the database visually | [dev.mysql.com/downloads/workbench](https://dev.mysql.com/downloads/workbench/) |
| **IntelliJ IDEA** | Open and run the project | [jetbrains.com/idea](https://www.jetbrains.com/idea/download/) *(Community Edition is free)* |


## Installation and Setup

### Step 1 — Install MySQL and Set Up the Database

1. Install **MySQL Community Server** from the link above
2. During installation, when it asks you to set a **root password** — remember it, you'll need it later
3. Open **MySQL Workbench**
4. Click the **"+"** icon next to "MySQL Connections" to create a new connection
5. Fill in:
   - Connection Name: `amadeus`
   - Hostname: `localhost`
   - Port: `3306`
   - Username: `root`
6. Click **"Test Connection"** → enter your password → it should say "Successfully made the MySQL connection"
7. Click **OK** to save the connection
8. Open the connection
9. In the top menu, click **File → Open SQL Script**
10. Navigate to the project folder and open the file: `sql/amadeus_workbench.sql`
11. Click the **lightning bolt** button to run the script
12. You should see the `amadeus_db` database appear in the left panel under "Schemas"

---

### Step 2 — Open the Project in IntelliJ IDEA

1. Open **IntelliJ IDEA**
2. Click **"Open"** (or File → Open)
3. Navigate to the project folder and select it → click **OK**
4. IntelliJ will detect it's a Maven project and ask to **"Load Maven Project"** — click it.
5. Wait for IntelliJ to finish downloading all dependencies (you'll see a progress bar at the bottom — this may take a few minutes the first time)

---

### Step 3 — Configure Your Database Password

1. In IntelliJ, navigate to:
   `src → main → resources`
2. Create a **new file** in that folder named exactly:
   `application-local.properties`
3. Open the new file and paste this inside:

```properties
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD_HERE
```

4. Replace `YOUR_MYSQL_PASSWORD_HERE` with the actual password you set during MySQL installation
5. Save the file

> ⚠️ **Important:** Never share or upload this file. It contains your personal database password.


### Step 4 — Run the Application

1. In IntelliJ, find this file in the left panel:
   `src → main → java → com → amadeus → simulator → AmadeusSimulatorApplication.java`
2. Open it and click the **green Run button** at the top
3. Watch the console at the bottom — when you see:
   ```
   Started AmadeusSimulatorApplication
   ```
   ...the app is running!


### Step 5 — Open the Simulator in Your Browser

1. Open any web browser (Chrome, Firefox, Edge)
2. Go to:
   ```
   http://localhost:8080
   ```
3. The **Amadeus Selling Platform Connect** terminal will load.


## Verify the Installation

Once the simulator is open in your browser, you can test it by typing a basic availability command. If flights appear on screen — everything is working perfectly!

You can also check the system health by visiting:
```
http://localhost:8080/api/health
```
It should return: `{"ok": true, "db": "MySQL OK"}`


## Project Structure

```
amadeus-simulator/
├── sql/
│   └── amadeus_workbench.sql        # Run this in MySQL Workbench first
├── src/
│   └── main/
│       ├── java/com/amadeus/simulator/
│       │   ├── AmadeusSimulatorApplication.java   # Main entry point
│       │   ├── controller/ApiController.java       # All API endpoints
│       │   └── config/DataInitializer.java         # Seeds initial data
│       └── resources/
│           ├── application.properties              # App configuration
│           ├── schema.sql                          # Database schema
│           └── static/index.html                  # The terminal UI
└── pom.xml                                         # Maven dependencies
```


## Troubleshooting

**The app won't start / database error**

**Port 8080 is already in use**
- Another app is using port 8080. In `application.properties`, change `server.port=8080` to `server.port=9090`, then go to `http://localhost:9090` instead

**IntelliJ can't find Java 17**
- Go to File → Project Structure → SDK → and select Java 17

**The database schema didn't load**
- Open MySQL Workbench, run the `sql/amadeus_workbench.sql` script manually as described in Step 1


## License

This project is open-source and free to use for educational and training purposes.
*Developed for the global aviation and travel industry community.*
