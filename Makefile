# Makefile for running Leiningen tasks primarily under the `dev` profile.
# The `cicd` profile is only used for GitHub/GitLab pipelines; special
# targets prefixed with "cicd-" invoke it.
LEIN = lein with-profile dev
LEIN_CICD = lein with-profile cicd
LEIN_UBERJAR = lein with-profile cicd,uberjar

.PHONY: default
default: fmt check compile test ## Run default build pipeline

.PHONY: all
all: clean fmt check compile test run ## Clean, build and run the project

.PHONY: help
help: ## Display this help
	@printf "Default goal: \033[36m%s\033[0m\n" "${.DEFAULT_GOAL}"
	@awk 'BEGIN {FS = ":.*##"; \
	  printf "\nUsage:\n  make \033[36m<target>\033[0m\n\nTargets:\n"} \
	    /^[a-zA-Z_-]+:.*?##/ \
	    { printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2 }' \
	  $(MAKEFILE_LIST)

.PHONY: fmt
fmt: ## Format source code with cljfmt
	$(LEIN) cljfmt fix

.PHONY: check
check: ## Check source formatting with cljfmt
	$(LEIN) cljfmt check

.PHONY: compile
compile: ## Compile source code
	$(LEIN) compile

.PHONY: test
test: ## Run unit tests
	$(LEIN) test

.PHONY: build
build: uberjar ## Build a standalone executable jar (alias for uberjar)

.PHONY: uberjar
uberjar: ## Build a standalone executable jar
	$(LEIN_UBERJAR)

.PHONY: run
run: ## Run with help flag
	$(LEIN) run -- -h

.PHONY: example
example: ## Run a full example mosaic build
	# input image: test.jpg (640 x 510)
	# output image: test_mosaic.jpg (2000 x 1600)
	# tile directory: images
	# output size: 2000
	# tile size: 50
	$(LEIN) run -- \
		-i test.jpg \
		-o test_mosaic.jpg \
		-d images \
		-s 2000 \
		-t 50

.PHONY: clean
clean: ## Delete all generated files
	$(LEIN) clean
	$(RM) -f .mosaic-cache

.PHONY: version
version: ## Print the package version
	@grep -m 1 "defproject" project.clj | cut -d'"' -f2
