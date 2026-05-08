# Specification: Implement CIELAB colour distance metric

## Problem Statement
The current Redmean colour distance metric is a good approximation but doesn't perfectly match human perception. CIELAB Delta-E (specifically ΔE*ab or ΔE*76) provides a more accurate perceptual distance by transforming RGB colours into the CIELAB colour space (L*a*b*), which is designed to be perceptually uniform.

## Requirements
- Implement RGB to CIELAB colour space conversion.
- Implement ΔE*ab (Euclidean distance in CIELAB space) calculation.
- Add a CLI option to allow users to choose between `redmean` and `cielab` metrics.
- Maintain existing performance by ensuring the conversion is efficient and type-hinted.

## Technical Design
- **New Namespace:** `mosaic.color` for colour space conversions (or extend `mosaic.math`).
- **Algorithm:**
    1. Convert sRGB to Linear RGB.
    2. Convert Linear RGB to XYZ colour space (D65 illuminant).
    3. Convert XYZ to CIELAB.
    4. Calculate Euclidean distance.
- **Interoperability:** Colours will still be stored as `double[]` in the cache, but the distance function will handle the conversion.
