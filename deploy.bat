@echo off
setlocal enabledelayedexpansion

echo Student Report Generator - Deployment Script
echo ============================================
echo.

REM Check if build directory exists
if not exist bin (
    echo Bin directory not found. Running build script first...
    call build.bat
    
    if not exist bin (
        echo Build failed. Cannot continue with deployment.
        exit /b 1
    )
    echo.
)

REM Create deployment directory
echo Creating deployment package...
if not exist deploy mkdir deploy

REM Check if iText JAR exists
set ITEXT_JAR=lib\itextpdf-5.5.13.3.jar
set ITEXT_INCLUDED=false

if exist %ITEXT_JAR% (
    echo iText library found - will be included in deployment
    set ITEXT_INCLUDED=true
) else (
    echo iText library not found - offering download
    
    echo Would you like to download iText for PDF support? (Y/N)
    set /p CHOICE=Your choice: 
    
    if /i "!CHOICE!"=="Y" (
        echo Downloading iText library...
        
        if not exist lib mkdir lib
        
        powershell -Command ^
            "& {Invoke-WebRequest -Uri 'https://github.com/itext/itextpdf/releases/download/5.5.13.3/itextpdf-5.5.13.3.jar' -OutFile '%ITEXT_JAR%'}"
        
        if exist %ITEXT_JAR% (
            echo Download complete!
            set ITEXT_INCLUDED=true
        ) else (
            echo Download failed. Continuing without iText.
        )
    ) else (
        echo Continuing deployment without iText library.
    )
)

REM Create manifest
echo Creating manifest...
if not exist META-INF mkdir META-INF
echo Main-Class: view.StudentReportGeneratorApp> META-INF\MANIFEST.MF

REM Create JAR file
echo Creating executable JAR...
if "%ITEXT_INCLUDED%"=="true" (
    jar cvfm deploy\StudentReportGenerator.jar META-INF\MANIFEST.MF -C bin . lib\itextpdf-5.5.13.3.jar
) else (
    jar cvfm deploy\StudentReportGenerator.jar META-INF\MANIFEST.MF -C bin .
)

REM Create run script for the deployed app
echo Creating launcher script...
echo @echo off> deploy\run_application.bat
echo java -jar StudentReportGenerator.jar>> deploy\run_application.bat

REM Copy documentation
echo Copying documentation...
copy README.md deploy\README.md
copy PROJECT_DOCUMENTATION.md deploy\PROJECT_DOCUMENTATION.md

REM Create a lib directory in deploy if needed
if "%ITEXT_INCLUDED%"=="true" (
    if not exist deploy\lib mkdir deploy\lib
    copy lib\itextpdf-5.5.13.3.jar deploy\lib\
)

REM Create a sample students.ser if it exists
if exist students.ser (
    echo Copying existing student data...
    copy students.ser deploy\students.ser
)

echo.
echo Deployment package created successfully!
echo.
echo The deployment package is in the 'deploy' directory and contains:
echo  - StudentReportGenerator.jar (executable JAR)
echo  - run_application.bat (launcher script)
echo  - Documentation files
if "%ITEXT_INCLUDED%"=="true" (
    echo  - iText library for PDF generation
)

echo.
echo To run the application:
echo  1. Navigate to the 'deploy' directory
echo  2. Run the 'run_application.bat' script or use 'java -jar StudentReportGenerator.jar'
echo.
echo To distribute the application:
echo  - Share the entire 'deploy' directory
echo  - Ensure users have Java installed (JDK 8 or higher)

endlocal
