#!/bin/bash
git ls-files -m "*.js" "*.xhtml" "*.html" "*.css" | sed 's/\(.*\)/"\1"/g'
