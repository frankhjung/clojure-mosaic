# Implementation Plan - Implement CIELAB colour distance metric

## Phase 1: Colour Space Conversion [checkpoint: 98b6eee]

- [x] Task: Create `mosaic.color` namespace and implement RGB to XYZ conversion d4f1e81
  - [x] Write tests for RGB to XYZ conversion
  - [x] Implement `srgb->xyz` function
- [x] Task: Implement XYZ to CIELAB conversion a22e36e
  - [x] Write tests for XYZ to CIELAB conversion
  - [x] Implement `xyz->lab` function
- [x] Task: Conductor - User Manual Verification 'Colour Space Conversion' (Protocol in workflow.md)

## Phase 2: CIELAB Distance Metric [checkpoint: 64611e1]

- [x] Task: Implement CIELAB distance function 18cf830
  - [x] Write tests for CIELAB distance
  - [x] Implement `cielab-distance-sq` in `mosaic.math`
- [x] Task: Update `find-best-match` to support multiple metrics 6339619
  - [x] Write tests for multi-metric matching
  - [x] Refactor `find-best-match` to accept a distance function
- [x] Task: Conductor - User Manual Verification 'CIELAB Distance Metric' (Protocol in workflow.md)

## Phase 3: CLI Integration

- [x] Task: Update CLI options in `mosaic.main` 2442b08
  - [x] Add `--metric` option (defaulting to `redmean`)
- [x] Task: Wire metric selection through `mosaic.core` cd7868d
- [x] Task: Conductor - User Manual Verification 'CLI Integration' (Protocol in workflow.md)
