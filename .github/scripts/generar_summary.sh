#!/usr/bin/env bash

format_time() {
    local total_seconds=$1
    local hours=$(( total_seconds / 3600 ))
    local minutes=$(( (total_seconds % 3600) / 60 ))
    local seconds=$(( total_seconds % 60 ))

    if [ "$hours" -gt 0 ]; then
        echo "${hours}h ${minutes}m ${seconds}s"
    elif [ "$minutes" -gt 0 ]; then
        echo "${minutes}m ${seconds}s"
    else
        echo "${seconds}s"
    fi
}

# 1. Extraemos SOLO las líneas de resumen para evitar duplicados en tiempos de tests fallidos.
# El modificador '-h' evita que grep imprima el nombre del archivo.
SUMMARY_LINES=$(grep -h '^Tests run:' target/surefire-reports/*.txt 2>/dev/null)

# 2. Verificamos si logramos extraer algo
if [ -n "$SUMMARY_LINES" ]; then

    # 3. Extraemos las métricas solo de esas líneas limpias
    TESTS_RUN=$(echo "$SUMMARY_LINES" | grep -oP 'Tests run: \K[0-9]+' | awk '{sum += $1} END {print sum+0}')
    FAILURES=$(echo "$SUMMARY_LINES" | grep -oP 'Failures: \K[0-9]+' | awk '{sum += $1} END {print sum+0}')
    ERRORS=$(echo "$SUMMARY_LINES" | grep -oP 'Errors: \K[0-9]+' | awk '{sum += $1} END {print sum+0}')
    SKIPPED=$(echo "$SUMMARY_LINES" | grep -oP 'Skipped: \K[0-9]+' | awk '{sum += $1} END {print sum+0}')

    # Ahora sí sumará el tiempo real, sin contar dos veces los fallos
    TIME_ELAPSED=$(echo "$SUMMARY_LINES" | grep -oP 'Time elapsed: \K[0-9.]+' | awk '{sum += $1} END {printf "%.0f", sum}')

    PASSED=$((TESTS_RUN - FAILURES - ERRORS - SKIPPED))
    FAIL_COUNT=$((FAILURES + ERRORS))
    TIME_FORMATTED=$(format_time "$TIME_ELAPSED")

    {
        echo "## 📊 Resultados de la Ejecución"
        echo ""
        echo "| Métrica | Resultado |"
        echo "| :--- | :--- |"
        echo "| 🎯 **Tests ejecutados** | $TESTS_RUN |"
        echo "| 🟢 **Pasadas** | $PASSED |"
        echo "| 🔴 **Fallados / Con error** | $FAIL_COUNT |"
        [ "$SKIPPED" -gt "0" ] && echo "| ⚠️ **Omitidas** | $SKIPPED |"
        echo "| ⏱️ **Tiempo transcurrido** | $TIME_FORMATTED |"
    } >> "$GITHUB_STEP_SUMMARY"

else
    echo "⚠️ No se encontró el reporte (.txt) en target/surefire-reports/. Revisa los artefactos." >> "$GITHUB_STEP_SUMMARY"
fi