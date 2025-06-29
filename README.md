# Student Report Generator

A Java Swing GUI application for managing student records and generating reports.

## Features

- Add and manage student records (personal information, subjects, and attendance)
- Generate report cards with grades, averages, and attendance statistics
- Export reports to text and PDF formats

## Current Implementation

The current implementation includes:

- Complete GUI for managing student records
- Functionality to add subjects with marks and calculate grades
- Attendance tracking system
- Report generation with export to text files
- PDF export capability using iText library (if available)

## Setup and Running

### Prerequisites

- Java JDK 8 or higher
- iText library (optional, for PDF export)

### Running the Application

1. Open a command prompt or PowerShell in the project directory
2. For first-time setup, run the build script which can automatically download the iText library:
   ```
   .\build.bat
   ```
3. Run the application using:
   ```
   .\run.bat
   ```

Alternatively, you can use VS Code tasks to run the application:
1. Open the project in VS Code
2. Press F1 or Ctrl+Shift+P
3. Type "Tasks: Run Task" and select it
4. Choose "Compile and Run"

## Usage Instructions

### Adding a Student

1. Launch the application
2. Click the "Add" button
3. Enter student details (ID, name, age, grade/class)
4. After saving, you'll be prompted to add subjects

### Managing Subjects

1. Select a student and click "Edit"
2. Click "Edit Subjects"
3. Add subject names and marks (0-100)
4. Click "Add Subject" to add each subject
5. Click "Done" when finished

### Recording Attendance

1. Select a student and click "Edit"
2. Click "Edit Attendance" 
3. Enter a date in yyyy-MM-dd format
4. Select the attendance status
5. Click "Add Attendance Record"

### Generating Reports

1. Select a student from the list
2. Click "Generate Report"
3. Choose export format (Text or PDF)
4. Select a location to save the report
5. For PDF export, the application will show you export options and offer to open the file when completed

## PDF Export Functionality

The application now supports true PDF export using the iText library:

- If iText is installed: Full-featured PDF reports will be generated with proper formatting
- If iText is not installed: The app will offer to download it, or fall back to a text-based PDF

### Installing iText Library

You can install the iText library in any of these ways:

1. **Automatic (Recommended)**: Run `build.bat` and choose 'Y' when prompted to download iText
2. **Manual**: 
   - Download iText JAR from https://github.com/itext/itextpdf/releases
   - Place the JAR file in the `lib` directory of this application
   - Rename it to `itextpdf-5.5.13.3.jar` if necessary

## Future Enhancements

- Data visualization for student performance
- Batch report generation for multiple students
- Import/export functionality for student data
- Enhanced PDF templates with school branding options

## Notes

- Student data is serialized and saved to a file called "students.ser" in the application directory
- The application automatically loads saved student data when started

## GitHub Repository

### Pushing to GitHub

To push this project to GitHub:

1. **Create a GitHub repository**:
   - Go to [GitHub](https://github.com) and sign in
   - Click the "+" icon in the top right and select "New repository"
   - Name your repository (e.g., "Student-Report-Generator")
   - Choose visibility (public or private)
   - Do not initialize with README, .gitignore, or license (we'll push existing code)
   - Click "Create repository"

2. **Initialize Git in your local project**:
   ```bash
   # Navigate to your project directory
   cd "path\to\Student_Report_Generator_java"

   # Initialize Git
   git init

   # Add all files to Git (exclude binary files and build artifacts)
   git add .
   
   # Make the first commit
   git commit -m "Initial commit of Student Report Generator"
   ```

3. **Connect and push to GitHub**:
   ```bash
   # Add the GitHub repository as remote
   git remote add origin https://github.com/YOUR_USERNAME/Student-Report-Generator.git

   # Push to GitHub
   git push -u origin master
   ```

4. **Update after making changes**:
   ```bash
   git add .
   git commit -m "Description of your changes"
   git push
   ```

### Cloning from GitHub

To clone this repository to a new computer:

```bash
git clone https://github.com/YOUR_USERNAME/Student-Report-Generator.git
cd Student-Report-Generator
.\build.bat
.\run.bat
```
