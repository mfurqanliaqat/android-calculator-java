# Android Calculator

## Overview

This project is an Android Calculator application with multiple functionalities designed for both landscape and portrait layouts. The landscape layout provides advanced functionalities while the portrait layout offers a basic calculator. The application supports scientific expressions such as `cos`, `sin`, `tan`, `cot`, `sqrt`, `sqr`, `cube`, and more, all without using any external libraries.

## Features

- **Basic Calculations**: Addition, subtraction, multiplication, division.
- **Scientific Functions**:
  - Trigonometric functions: `sin`, `cos`, `tan`, `cot`, `sinh`, `cosh`, `tanh`, `sin⁻¹`, `cos⁻¹`, `tan⁻¹`.
  - Power functions: `x²`, `x³`, `xʸ`, `10ˣ`, `eˣ`.
  - Logarithmic functions: `log`, `ln`.
  - Root functions: `√x`, `³√x`.
  - Factorial: `n!`.
  - Inverse: `1/x`.
  - Random number: `Rand`.
  - Angle units: `deg`, `rad`.
- **Expression Support**: Handle complex scientific expressions.
- **Dual Layout**:
  - **Portrait Layout**: Basic calculator functionalities.
  - **Landscape Layout**: Extended functionalities for scientific calculations.

## Screenshots

### Portrait Mode

![Portrait Mode](path_to_portrait_screenshot)

### Landscape Mode

![Landscape Mode](path_to_landscape_screenshot)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/mfurqanliaqat/android-calculator-java.git
   ```
2. Open the project in Android Studio.
3. Build the project and run it on an emulator or a physical device.

## Usage

- **Portrait Mode**: Use this mode for basic calculations. Rotate the device to access the landscape mode.
- **Landscape Mode**: Use this mode for advanced scientific calculations.

## Development

### Prerequisites

- Android Studio
- Java JDK

### Building the Project

1. Open the project in Android Studio.
2. Sync the project with Gradle files.
3. Build and run the project on an Android device or emulator.

### Project Structure

- `MainActivity.java`: The main activity handling the calculator logic.
- `activity_main.xml`: Layout file for the portrait mode.
- `layout-land/activity_main.xml`: Layout file for the landscape mode.
- `FCal.java`: Class containing the logic for calculations.

## Contributing

1. Fork the repository.
2. Create your feature branch (`git checkout -b feature/new-feature`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature/new-feature`).
5. Create a new Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgements

- Inspiration from various calculator apps.
- Thanks to the Android community for their continuous support.

---

Feel free to reach out for any queries or suggestions.
