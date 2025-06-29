# Contributing to Student Report Generator

Thank you for your interest in contributing to the Student Report Generator project! This document provides guidelines and instructions for contributing.

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** to your local machine
3. **Set up the development environment**:
   ```bash
   # Build the project
   .\build.bat
   
   # Run the application
   .\run.bat
   ```

## Making Changes

1. **Create a branch** for your feature or bugfix:
   ```bash
   git checkout -b feature/your-feature-name
   ```
   
2. **Make your changes**:
   - Follow the existing code style
   - Add appropriate comments
   - Update documentation if necessary
   
3. **Test your changes**:
   - Ensure the application builds successfully
   - Test the functionality you've changed
   - Check for any regressions

4. **Commit your changes**:
   ```bash
   git add .
   git commit -m "Description of your changes"
   ```

## Submitting Changes

1. **Push your changes** to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
   
2. **Create a Pull Request** on GitHub:
   - Go to the original repository
   - Click "Pull Requests" and then "New Pull Request"
   - Click "Compare across forks"
   - Select your fork and branch
   - Click "Create Pull Request"
   - Provide a clear title and description

## Coding Guidelines

- Follow Java naming conventions
- Maintain the MVC architecture
- Comment your code, especially complex logic
- Update documentation for user-facing changes
- Write clean, readable code

## Project Structure

- `src/main/java/model/` - Data models
- `src/main/java/view/` - GUI components
- `src/main/java/controller/` - Business logic
- `src/main/java/util/` - Utility classes

## Building and Deployment

- Use `build.bat` to compile the application
- Use `deploy.bat` to create a deployable package
- Use `run.bat` to run the application during development

## Questions?

If you have any questions or need help, please open an issue on GitHub or contact the project maintainers.

Thank you for contributing to Student Report Generator!
