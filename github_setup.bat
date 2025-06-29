@echo off
setlocal enabledelayedexpansion

echo Student Report Generator - GitHub Setup Script
echo =============================================
echo.

REM Check if Git is installed
git --version >nul 2>&1
if %errorlevel% neq 0 (
    echo Git is not installed or not in your PATH.
    echo Please install Git from https://git-scm.com/ and try again.
    exit /b 1
)

echo This script will help you push your project to GitHub.
echo.
echo Before continuing, make sure you have:
echo  1. Created a GitHub account
echo  2. Created a new empty repository on GitHub
echo.
set /p CONTINUE=Do you want to continue? (Y/N): 

if /i not "!CONTINUE!"=="Y" (
    echo Setup canceled.
    exit /b 0
)

echo.
echo Please enter your GitHub username:
set /p USERNAME=Username: 

echo.
echo Please enter your repository name (e.g., Student-Report-Generator):
set /p REPO_NAME=Repository name: 

echo.
echo Initializing Git repository...
git init

echo.
echo Creating .gitattributes file for proper line endings...
echo * text=auto > .gitattributes

echo.
echo Adding files to Git (excluding files specified in .gitignore)...
git add .

echo.
echo Making initial commit...
git commit -m "Initial commit of Student Report Generator"

echo.
echo Setting up remote repository...
git remote add origin https://github.com/%USERNAME%/%REPO_NAME%.git

echo.
echo Determining default branch name...
for /f "tokens=* USEBACKQ" %%g in (`git branch --show-current`) do (
    set BRANCH=%%g
)

if "!BRANCH!"=="" (
    set BRANCH=main
    echo Setting default branch to main...
    git branch -M main
)

echo.
echo Ready to push to GitHub. Your code will be pushed to:
echo https://github.com/%USERNAME%/%REPO_NAME%
echo.
set /p PUSH=Push to GitHub now? (Y/N): 

if /i "!PUSH!"=="Y" (
    echo Pushing to GitHub...
    git push -u origin !BRANCH!
    
    if !errorlevel! neq 0 (
        echo.
        echo Push failed. This could be due to:
        echo  - The repository doesn't exist
        echo  - You need to authenticate with GitHub
        echo.
        echo Try pushing manually with:
        echo git push -u origin !BRANCH!
        echo.
        echo You may need to set up GitHub authentication:
        echo  1. Use a personal access token: https://github.com/settings/tokens
        echo  2. Configure the Git credential manager
        echo  3. Set up SSH keys for GitHub
    ) else (
        echo.
        echo Success! Your code has been pushed to GitHub.
        echo Repository URL: https://github.com/%USERNAME%/%REPO_NAME%
    )
) else (
    echo.
    echo Setup complete but code was not pushed.
    echo To push manually, run:
    echo git push -u origin !BRANCH!
)

echo.
echo GitHub setup process complete.

endlocal
