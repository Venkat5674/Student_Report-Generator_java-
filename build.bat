@echo off
setlocal enabledelayedexpansion

echo Student Report Generator Build Script
echo ====================================

REM Check if lib directory exists
if not exist lib (
    echo Creating lib directory...
    mkdir lib
)

REM Check if iText JAR exists
set ITEXT_JAR=lib\itextpdf-5.5.13.3.jar
if not exist %ITEXT_JAR% (
    echo iText library not found. Would you like to download it now? (Y/N)
    set /p CHOICE=Your choice: 
    if /i "!CHOICE!"=="Y" (
        echo Downloading iText library...
        echo This feature requires PowerShell to download files
        
        REM Download using PowerShell
        powershell -Command ^
            "& {Invoke-WebRequest -Uri 'https://github.com/itext/itextpdf/releases/download/5.5.13.3/itextpdf-5.5.13.3.jar' -OutFile '%ITEXT_JAR%'}"
        
        if exist %ITEXT_JAR% (
            echo Download complete!
        ) else (
            echo Download failed. Please download the iText JAR manually:
            echo 1. Visit https://github.com/itext/itextpdf/releases
            echo 2. Download itextpdf-5.5.13.3.jar
            echo 3. Place it in the lib directory
        )
    ) else (
        echo Skipping iText download. The application will use text-based PDF export.
    )
)

REM Compile the application
echo.
echo Compiling the application...
if not exist bin mkdir bin

REM Compile with iText JAR if available
if exist %ITEXT_JAR% (
    javac -d bin -cp ".;%ITEXT_JAR%" src\main\java\**\*.java
) else (
    javac -d bin src\main\java\**\*.java
)

if %errorlevel% neq 0 (
    echo.
    echo Compilation failed. Please check for errors.
    exit /b %errorlevel%
)

echo.
echo Compilation successful!
echo.
echo Run the application using: run.bat

endlocal
