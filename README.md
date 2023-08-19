# Project Readme

This Readme file provides step-by-step instructions on how to set up the environment, build the application, configure the Apache Tomcat server, and run the tests for the project. Please follow the guidelines below for a smooth setup and execution.

## Table of Contents:

1. **Setting Up the Environment**
   - Prerequisites
   - Installing Java Development Kit (JDK 11 Releases)
   - Installing Apache Maven
   - Setting Environment Variables

2. **Building the Application**
   - Cloning the Repository
   - Building with Maven

3. **Configuring Apache Tomcat**
   - Downloading Apache Tomcat
   - Configuring Tomcat

4. **Running the Tests**
   - Running Unit Tests
   - Verifying Test Results

---

## 1. Setting Up the Environment:

Before you begin, make sure you have the following prerequisites installed on your Windows system:

- Java Development Kit (JDK 11 Releases)
- Apache Maven

**Installing Java Development Kit (JDK):**

1. Download the latest version of JDK for Windows from the official Oracle website.
2. Run the installer and follow the on-screen instructions to install JDK.
3. Set the `JAVA_HOME` environment variable to the JDK installation directory.

**Installing Apache Maven:**

1. Download the latest version of Apache Maven from the official website.
2. Extract the downloaded archive to a directory on your system.
3. Set the `MAVEN_HOME` environment variable to the Maven installation directory.
4. Add `%MAVEN_HOME%\bin` to your system's `PATH` environment variable.

**Setting Environment Variables:**

1. Right-click on the Windows icon and select "System."
2. Click on "Advanced system settings" on the left-hand side.
3. In the System Properties window, click the "Environment Variables" button.
4. Under "System variables," click "New" and add `JAVA_HOME` and `MAVEN_HOME` with their respective installation paths.
5. Edit the `Path` variable under "System variables" and append `%JAVA_HOME%\bin` and `%MAVEN_HOME%\bin`.

## 2. Building the Application:

**Cloning the Repository:**

1. Open a command prompt or terminal.
2. Navigate to the directory where you want to clone the project.
3. Run the following command to clone the repository:
   ```
   git clone <repository_url>
   ```
4. Navigate into the project directory:
   ```
   cd Business-Trip-Reimbursement
   ```

**Building with Maven:**

1. Inside the project directory, execute the following command to build the application:
   ```
   mvn clean install
   ```
2. Maven will download dependencies, compile the code, and create a deployable WAR file.

## 3. Configuring Apache Tomcat:

**Downloading Apache Tomcat:**

1. Download the latest version of Apache Tomcat from the official website.
2. Extract the downloaded archive to a directory on your system.

**Configuring Tomcat:**

1. Open the `conf` directory of your Tomcat installation.
2. Edit the `server.xml` file to configure Tomcat's ports and other settings if necessary.

## 4. Running the Tests:

**Running Unit Tests:**

1. Make sure you are still in the project directory.
2. Run the following command to execute unit tests:
   ```
   mvn test
   ```

**Verifying Test Results:**

1. After running the tests, Maven will display the test results in the console.
2. Look for "BUILD SUCCESS" if all tests passed, or check the detailed test results for any failures or errors.

---

By following these steps, you should be able to set up the environment, build the application, configure the Apache Tomcat server, and run the tests successfully.

Feel free to reach out if you need any further assistance or have questions.

