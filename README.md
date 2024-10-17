# Employee Pairs

## Overview
This application identifies employee pairs who worked together the most, both across all projects and on specific ones. It consists of two main parts:

1. **Server** - built using Java and Spring Boot.
2. **Client** - built using Angular for a simple, user-friendly interface.

---

## How It Works

### Server Side - Java (Spring Boot)
The backend of the application performs the following key tasks:

#### 1. On Application Startup:
   - Reads all the files in the `project_data` folder.
   - For each file, the following data is printed:
     1. **File Name**
     2. **Pair with Most Time Together Overall**: The pair that worked together the most across all projects, including the projects they worked on and their total time spent together.
     3. **Pair with Most Time on a Single Project**: The pair that spent the most time on a single project.
     4. **All Pairs Sorted**: A list of all employee pairs that worked together, sorted in descending order by the time spent together.

#### 2. API Endpoint:
   - Exposes a single endpoint that:
     - Accepts a CSV file upload.
     - Returns the employee pair that worked together the most, along with details of all the projects they worked on and the time periods.

---

### Client Side - Angular
The frontend provides a simple interface with the following features:

#### 1. File Upload:
   - Allows users to select a CSV file to upload for pair calculation.

#### 2. Data Display:
   - Displays the calculated results in a **datagrid** format for easy viewing of employee pairs and project details.

---

This setup needs a JDK and Node installed
