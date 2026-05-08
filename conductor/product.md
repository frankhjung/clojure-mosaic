# Product Definition

## Initial Concept

A Clojure-based tool that transforms a source image into a photo mosaic by sampling local colours and matching them to a library of tile images using the Redmean distance metric.

## Vision

To provide a high-performance, perceptually accurate mosaic generation engine that is accessible to hobbyists and extensible for Clojure developers.

## Target Audience

- **Hobbyist Photographers:** Users seeking a simple way to create artistic mosaics from personal photo libraries.
- **Clojure Developers:** Engineers looking for idiomatic examples of image processing and parallel computation in Clojure.

## Core Features

- **Redmean Colour Matching:** Uses a perceptually weighted colour distance metric for superior visual results.
- **Parallel Processing:** Leverages `pmap` for efficient tile loading and matching.
- **Metadata Caching:** Persists tile analysis to avoid redundant computations on subsequent runs.
- **Flexible Grid Scaling:** Allows users to define output dimensions and tile sizes.

## Future Roadmap

- **Advanced Colour Math:** Implementation of CIELAB or other Delta-E metrics for even better perceptual matching.
- **Enhanced Memory Management:** Optimised handling of massive tile libraries and high-resolution outputs.

## Constraints

- **JVM Focused:** Built on standard Java AWT/ImageIO for maximum portability without native dependencies.
- **Resource Efficiency:** Must manage memory effectively when processing large image datasets.
