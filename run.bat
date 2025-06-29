@echo off
echo Student Report Generator

REM Check if bin directory exists, if not run build.bat
if not exist bin (
    echo Bin directory not found. Running build script...
    call build.bat
    echo.
)

REM Check if iText JAR exists
set ITEXT_JAR=lib\itextpdf-5.5.13.3.jar
if exist %ITEXT_JAR% (
    echo Found iText library - PDF export will be fully functional
    echo.
    echo Running application with iText library...
    java -cp "bin;%ITEXT_JAR%" view.StudentReportGeneratorApp
) else (
    echo iText library not found - PDF export will use fallback mode
    echo.
    echo Running application...
    java -cp "bin" view.StudentReportGeneratorApp
)

if %ERRORLEVEL% NEQ 0 (
    echo Application encountered an error.
    pause
)
