# Zort

Zort is a Java-based project that organizes and processes images using computer vision. It can detect faces and manage datasets for machine learning models.

#Demo
[working](https://youtu.be/YlGTkStN5dw?si=54dyEffvqI0UK7R8)

## Project Structure

- `src/` - Source code of the project
- `bin/` - Compiled binaries
- `lib/` - Libraries (currently ignored in Git, to be added via LFS if needed)
- `models/` - Pre-trained models (ignored in Git for now)
- `.vscode/` - VS Code configuration
- `deploy.prototxt` - Model deployment configuration
- `README.md` - Project documentation

## Features

- Organizes images by detected faces
- Supports multiple pre-trained models (TensorFlow, OpenCV)
- Lightweight Git repository without large model files

## Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/w1dow/zort.git
