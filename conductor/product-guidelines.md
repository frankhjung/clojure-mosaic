# Product Guidelines

## Prose Style

- **Australian English:** All documentation (Markdown files, docstrings) must use Australian English.
- **Technical Tone:** Maintain a senior engineering tone—professional, direct, and concise.

## User Experience (CLI)

- **CLI First:** The primary interface is the Command Line. Provide a clear, Unix-style experience with standard flags and helpful usage summaries.
- **Progressive Disclosure:** Inform the user of major lifecycle events (e.g., "Loading tiles...", "Assembling mosaic...") without being overly verbose.

## Documentation Standards

- **Public Docstrings:** Every public function and namespace must include a descriptive docstring explaining its purpose and parameters.
- **Architectural Documentation:** Maintain high-level architectural overviews in the `doc/` directory to ensure long-term maintainability.
- **Visual Documentation:** Use Mermaid diagrams to document system architecture and sequence flows.

## Code Quality

- **Functional Purity:** Prioritise pure functions and immutable data structures. Isolate side effects to the edges of the system (I/O, CLI).
- **Reflection Free:** Ensure zero-reflection on hot paths through diligent use of Java type hints.
- **Performance:** Use parallel computation (`pmap`) where appropriate to leverage multi-core processors for image processing.
