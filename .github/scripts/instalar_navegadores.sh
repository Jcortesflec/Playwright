#!/usr/bin/env bash

echo "--- 🌐 Asegurando todos los navegadores (motores base + comerciales) ---"

# Añadimos 'chrome' y 'msedge' a la lista de instalación
./mvnw exec:java -e -Dexec.mainClass=com.microsoft.playwright.CLI \
  -Dexec.args="install chromium firefox webkit chrome msedge --with-deps"

echo "--- ✅ Todos los navegadores y dependencias están listos ---"