#!/usr/bin/env bash

ALLURE_VERSION="2.29.0"
ALLURE_DIR="/opt/allure-$ALLURE_VERSION"

# Si Allure no está en /opt (por cache miss o primera vez)
if [ ! -d "$ALLURE_DIR" ]; then
    echo "--- 🚀 Descargando Allure $ALLURE_VERSION ---"
    # Usamos curl -L para seguir redirecciones y -o para el nombre del archivo
    curl -sL "https://github.com/allure-framework/allure2/releases/download/$ALLURE_VERSION/allure-$ALLURE_VERSION.tgz" -o allure.tgz

    # Verificamos si se descargó algo antes de intentar descomprimir
    if [ -f "allure.tgz" ]; then
        tar -zxf allure.tgz -C /opt/
        rm allure.tgz
        echo "--- ✅ Allure instalado en /opt ---"
    else
        echo "--- ❌ Error: No se pudo descargar Allure ---"
        exit 1
    fi
else
    echo "--- ✅ Allure ya existe en /opt (Caché) ---"
fi

# Recreamos el link simbólico (siempre necesario en el contenedor)
ln -sf "$ALLURE_DIR/bin/allure" /usr/bin/allure

# Verificamos que ahora sí exista el comando
if ! command -v allure &> /dev/null; then
    echo "--- ❌ Error: El comando allure no está disponible ---"
    exit 1
fi

echo "--- 📄 Generando reporte Single HTML ---"
allure generate target/allure-results -o ./allure-temp --clean --single-file