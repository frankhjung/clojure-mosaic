# Glossary

## Average Colour

A representation of the simple mean colour of an image or a specific region
of an image. In this project, it is calculated by averaging the B, G, and R
channels and is used as the primary feature for finding the best-matching
Tile for a Cell.

## Cell

A single rectangular (typically square) unit within the Grid. Each cell in
the scaled-down Source Image is replaced by the best-matching Tile during
mosaic assembly.

## CIELAB

A perceptually uniform colour space (L*a*b*) used to calculate colour
distance. It better matches human visual perception than simple Euclidean
distance in RGB space.

## Distance Metric

A mathematical function used to calculate the similarity (or "distance")
between two colours. The project supports Redmean and CIELAB metrics.

## Dominant Colour

An RMS (Root Mean Square) average of the colour channels in an image. It
is specifically used to determine a suitable background colour when
padding a Tile to maintain its square aspect ratio, as the RMS approach
better preserves the "energy" or brightness of the original image.

## Grid

The two-dimensional arrangement of Cells that defines the structure of the
final Mosaic. The grid dimensions (nx by ny) are determined by the Source
Image's aspect ratio and the Target Size, snapped to the nearest whole
multiple of the tile size.

## Metric

An interface (protocol) that defines how colours are compared for similarity.
It consists of a preparation step (converting to a specific colour space)
and a distance calculation step.

## Mosaic

The final output image, composed of a Grid of Tiles that collectively
resemble the original Source Image when viewed from a distance. Its final
dimensions are exactly the product of the grid cell count and the tile
size, which may be slightly larger than the user-requested Target Size.

## Prepared Colour

A representation of a colour that has been optimised for a specific Metric.
This might be the original BGR array (for Redmean) or a converted LAB
triple (for CIELAB). Preparing colours once before a loop avoids redundant
computations.

## Redmean

An approximation of perceptual colour distance that is computationally
efficient. It weights colour channels differently to better align with
human vision than a simple Euclidean distance in RGB space.

## Source Image

The primary input image that the user wishes to transform into a Mosaic. It
is sampled to determine the target Average Colour for each Cell in the Grid.

## Target Size

The user-requested maximum dimension (width or height) for the final
Mosaic. This value is used to calculate the Grid dimensions, which are
then snapped to the nearest whole Tile size to ensure no partial tiles
are drawn.

## Tile

A small, individual image from a library that is used to represent a single
Cell in the Mosaic's Grid. Tiles are pre-processed to determine their
Average Colour for efficient matching.

## Tile Cache

A persistence layer (stored as an EDN file) that maps Tile file paths and
their last-modified timestamps to their pre-calculated Average Colour. This
avoids redundant image processing on subsequent runs. This cache is
managed internally by the Tile Library and is keyed on both the directory
content and the tile size.

## Tile Library

A deep module that manages a collection of Tiles. It encapsulates metadata
extraction, persistent caching, and the efficient, lazy loading and
resizing of tile images. It provides a simple interface for callers to
retrieve tile colours for matching and prepared images for assembly.
