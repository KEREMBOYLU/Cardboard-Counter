# 📦 Cardboard Counter – Java Image Processing Project

A Java-based image processing tool designed to detect and count stacked cardboard sheets in an image using grayscale pixel analysis, smoothing techniques, and Otsu’s thresholding. The result is visualized both numerically and graphically.

---

## 📌 Overview

This project was developed as part of a summer learning experience, aimed at solving a real-world problem reported by an intern working at a local company. The challenge was to estimate the number of stacked cardboards in a photo. The solution involves pixel-level grayscale analysis and visual feedback through charts and image annotations.

---

## 🖥️ How It Works

1. Select an image file (JPG, PNG)
2. Image is converted to grayscale and its pixel intensities are summed vertically
3. Charts are generated for:
   - Raw grayscale data
   - Smoothed data (moving average)
   - Otsu-thresholded grayscale data
   - Smoothed Otsu data
4. Threshold intersections are counted
5. Peaks above threshold are detected and marked on the image
6. Processed image with red bars is saved and displayed
