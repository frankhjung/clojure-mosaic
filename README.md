# Mosaic

A Clojure project for photo mosaic generation.

This will create a mosaic of images using an input image as a base. The mosaic
is created by sampling the average colour of each cell in the input image and
finding the best matching tile image from a directory using the Redmean colour
distance metric.

## Example

Run a full mosaic build using the bundled example images:

```bash
make example
```

This uses `test.jpg` as the source image, the `images/` directory as the tile
library, and writes `test_mosaic.jpg` at 2000 px with 50 px tiles.

## Requirements

- Java >= 11
- [Leiningen](https://leiningen.org/) build tool

## Quick Start

### Install Leiningen (if not already installed)

```bash
sudo apt install leiningen
```

### Run Checks and Tests

Run the full default pipeline — clean, format, check, compile, and test:

```bash
make
```

Or invoke individual steps:

```bash
make compile     # compile and report reflection warnings
make test        # run unit tests
```

### Fix Formatting

```bash
make fmt         # auto-fix formatting with cljfmt
make check       # check only (no changes)
```

### Run the Application

```bash
make run
```

## CLI Usage

```bash
make example
```

Or with custom arguments:

```bash
lein run -- -i INPUT -d DIRECTORY -o OUTPUT -s SIZE -t TILE
```

| Option | Description |
|--------|-------------|
| `-i, --input INPUT` | Path to the source image file |
| `-d, --directory DIRECTORY` | Directory containing tile images |
| `-o, --output OUTPUT` | Path where the resulting mosaic will be saved |
| `-s, --size SIZE` | Desired size (px) of the largest dimension of the mosaic |
| `-t, --tile TILE` | Width and height (px) of each square tile |
| `-h, --help` | Print help and exit |

### CLI Example

```bash
make example
```

## Make Targets

| Target | Description |
|--------|-------------|
| `make` / `make default` | Clean, format, check, compile, and test |
| `make all` | Check, test, and run with `-h` |
| `make fmt` | Auto-fix source formatting with cljfmt |
| `make check` | Verify source formatting with cljfmt |
| `make compile` | Compile source code |
| `make test` | Run unit tests with eftest |
| `make run` | Run the application with `-h` |
| `make example` | Run a full mosaic build with example images |
| `make clean` | Delete compiled artefacts and cache |
| `make help` | List all available targets |

## Implementation Details

### Project Structure

```text
mosaic/
├── src/mosaic/
│   ├── main.clj    # CLI entry point (clojure.tools.cli)
│   ├── core.clj    # Orchestration: tile loading and mosaic assembly
│   ├── image.clj   # Image I/O, resize-and-pad, colour extraction
│   ├── math.clj    # Redmean colour distance, best-match search
│   └── cache.clj   # EDN file cache keyed on directory hash
├── test/mosaic/
│   └── math_test.clj
├── images/         # Sample tile images
├── project.clj
└── Makefile
```

### Coding Approach

- **Functional style**: core logic is expressed as pure functions; side effects
  are isolated to `main.clj`, `cache.clj`, and the image I/O functions.
- **Parallelism**: tile processing and colour matching use `pmap` to exploit
  multiple cores.
- **Reflection-free**: all namespaces set `*warn-on-reflection* true` and carry
  complete Java type hints for zero-reflection hot paths.
- **Caching**: processed tile metadata is persisted to `.mosaic-cache` (EDN) and
  invalidated automatically when the tile directory changes.

## License

Copyright © 2026 Frank Jung

Released under the [MIT License](LICENSE).
