# Mosaic Project Reproduction Plan (Clojure)

This document outlines the plan to reproduce the Python `mosaic` project in
Clojure using Leiningen.

## 1. Project Overview

The goal is to create a Clojure-based CLI tool that generates photo mosaics. It
will take an input image, a directory of tile images, and produce a high-quality
mosaic by matching the average colour of input grid cells to the best available
tiles.

## 2. Tech Stack

- **Language:** Clojure
- **Build Tool:** Leiningen
- **Image Processing:** Pure `java.awt` and `javax.imageio` (Java interop).
- **CLI Parsing:** `org.clojure/tools.cli`
- **Testing:** `clojure.test`
- **Linting:** `clj-kondo` (via Makefile)

## 3. Namespace Structure

- `mosaic.main`: CLI entry point, argument parsing, and top-level orchestration.
- `mosaic.core`: Mosaic generation logic (grid splitting, tile matching).
- `mosaic.image`: Image manipulation (resizing, padding, colour analysis).
- `mosaic.math`: Colour distance calculations (Redmean formula) using primitive
  arrays.
- `mosaic.cache`: Persistence logic for tile metadata (EDN format).

## 4. Key Implementation Details

### Colour Analysis

- **Average Colour**: Arithmetic mean of R, G, B channels.
- **Dominant Colour**: RMS-based dominant colour (matching the Python
  implementation).
- **Distance**: Redmean distance formula implemented with type hints and
  primitive arrays for performance.

### Image Processing & Memory

- **Resize and Pad**: Maintain aspect ratio, scale to fit, and pad with dominant
  colour.
- **Memory Management**: Draw matched tiles directly onto a single persistent
  `BufferedImage` target.
- **LRU Cache**: Use an LRU cache for tile images that represent duplicate
  target colours to avoid redundant I/O.

### Persistence

- **Metadata Cache**: Store pre-computed average colours and file paths in a
  `.mosaic-cache` file using the EDN format. Validated by directory hash.

### Performance

- Use `pmap` for parallel tile matching and image processing.
- Minimise external libraries to stay idiomatic and lightweight.

## 5. Development Phases

### Phase 1: Infrastructure

- [ ] Initialize project with `lein new app mosaic`.
- [ ] Setup `project.clj` with `tools.cli`.
- [ ] Create a `Makefile` to mirror the Python project's interface.

### Phase 2: Core Logic & Image Interop

- [ ] Implement `mosaic.image` (resize, pad, average/dominant colour).
- [ ] Implement `mosaic.math` (Redmean distance with primitive optimizations).
- [ ] Unit tests for math and image logic.

### Phase 3: Orchestration & Caching

- [ ] Implement `mosaic.cache` for EDN-based metadata persistence.
- [ ] Implement `mosaic.core` with `pmap` matching and `Graphics2D` assembly.
- [ ] Implement LRU caching for tile images.

### Phase 4: CLI & Validation

- [ ] Implement `mosaic.main` with `tools.cli`.
- [ ] Integration test matching the Python `make example`.
- [ ] Run `make check` and `make test` to validate functionality.

## 6. CLI Interface

The CLI mirrors the Python version:

```bash
lein run -- -i photo.jpg -d tiles/ -o mosaic.jpg -s 2000 -t 50
```

## 7. Tools

Use `Makefile` targets for common tasks:

- `make check`: Run linters and formatters.
- `make test`: Run unit tests.
- `make run`: Run the CLI with help flag.
- `make clean`: Clean generated files and caches.

## 8. License

Use MIT License, same as the original Python project.
