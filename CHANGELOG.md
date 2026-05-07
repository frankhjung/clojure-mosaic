# Change Log

All notable changes to this project will be documented in this file. This change
log follows the conventions of
[keepachangelog.com](http://keepachangelog.com/).

## [Unreleased]

## 0.1.1 - 2026-05-07

### Added

- Initial release of the Clojure photo mosaic generator.
- Support for CLI usage to generate mosaics from an input image and a tile
  directory.
- Concurrent tile matching and rendering via `pmap`.
- EDN file cache for tile metadata, keyed on directory content hash.
- Redmean colour distance metric for perceptually accurate tile matching.
- `(set! *warn-on-reflection* true)` to all namespaces (`cache`, `core`, `math`,
  `main`) for consistent reflection checking across the codebase.

### Changed

- **`cache.clj`**: replaced `(if ... nil)` branches with `(when ...)` for
  idiomatic nil-returning conditionals.
- **`core.clj`**: added `^BufferedImage` type hint to `base-img` binding and
  `get-tile-from-cache` return type, eliminating all reflection warnings;
  wrapped `drawImage` coordinate arguments in `(int ...)` to resolve
  `Long`→`int` reflection; replaced `(remove nil? (pmap ...))` with
  `(keep identity (pmap ...))` ; replaced bare `Exception.` with `ex-info`.
- **`image.clj`**: minor idiomatic cleanup.
- **`main.clj`**: improved CLI error handling structure.
- **`README.md`**: full rewrite with quick-start guide, CLI usage table, make
  target reference, project structure, and coding approach sections.
- **`Makefile`**: minor target fixes.

[Unreleased]: https://gitlab.com/frankhjung1/clojure-mosaic/-/compare/0.1.1...HEAD
[0.1.1]: https://gitlab.com/frankhjung1/clojure-mosaic/-/compare/0.1.0...0.1.1
