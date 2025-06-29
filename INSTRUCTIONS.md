# Getting Started with Student Report Generator

## Prerequisites

- Java JDK 8 or higher installed on your system
- The iText library JAR file (for PDF export functionality)

## Setup Instructions

1. Download the iText library:
   - Visit https://github.com/itext/itextpdf/releases
   - Download the latest version of `itextpdf-x.x.x.jar`
   - Create a `lib` folder in the project directory if it doesn't exist
   - Place the downloaded JAR file in the `lib` folder

2. Compile and run the application:
   - Double-click the `run.bat` file in the project directory
   - Alternatively, open the project in Visual Studio Code and run the "Compile and Run" task

## Using the Application

1. **Adding a Student**:
   - Click the "Add" button
   - Fill in the student details (ID, Name, Age, Grade/Class)
   - After adding basic info, you'll be prompted to add subjects

2. **Managing Subjects**:
   - Add subjects by entering the subject name and mark (0-100)
   - Remove subjects by selecting them and clicking "Remove Subject"

3. **Managing Attendance**:
   - Edit a student and click "Edit Attendance"
   - Enter dates in yyyy-MM-dd format and select the attendance status

4. **Generating Reports**:
   - Select a student from the list
   - Click "Generate Report"
   - Choose export format (Text or PDF)
   - Select a location to save the report

## Troubleshooting

- If you encounter errors about missing classes, ensure the iText JAR file is properly placed in the lib folder
- If the application won't start, ensure you have Java installed and accessible in your PATH
