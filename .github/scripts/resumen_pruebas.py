"""Convierte los reportes de Surefire y JaCoCo en un JSON para la página web."""
import csv
import glob
import json
import os
import sys
import xml.etree.ElementTree as ET
from datetime import datetime, timezone

target, salida, sha, run_url = sys.argv[1:5]

clases = []
for archivo in sorted(glob.glob(os.path.join(target, "surefire-reports", "TEST-*.xml"))):
    suite = ET.parse(archivo).getroot()
    casos = []
    for caso in suite.iter("testcase"):
        fallo = caso.find("failure") is not None or caso.find("error") is not None
        casos.append({
            "nombre": caso.get("name"),
            "tiempo": float(caso.get("time", 0)),
            "ok": not fallo and caso.find("skipped") is None,
        })
    clases.append({
        "clase": suite.get("name"),
        "total": int(suite.get("tests", 0)),
        "fallos": int(suite.get("failures", 0)) + int(suite.get("errors", 0)),
        "casos": casos,
    })

cobertura = None
csv_path = os.path.join(target, "site", "jacoco", "jacoco.csv")
if os.path.exists(csv_path):
    cubiertas = perdidas = 0
    with open(csv_path, encoding="utf-8") as f:
        for fila in csv.DictReader(f):
            cubiertas += int(fila["LINE_COVERED"])
            perdidas += int(fila["LINE_MISSED"])
    if cubiertas + perdidas:
        cobertura = round(100 * cubiertas / (cubiertas + perdidas), 1)

resumen = {
    "total": sum(c["total"] for c in clases),
    "fallos": sum(c["fallos"] for c in clases),
    "cobertura_lineas": cobertura,
    "commit": sha[:7],
    "ejecucion": run_url,
    "fecha": datetime.now(timezone.utc).isoformat(timespec="seconds"),
    "clases": clases,
}
with open(salida, "w", encoding="utf-8") as f:
    json.dump(resumen, f, ensure_ascii=False, indent=2)
print(f"{resumen['total']} pruebas, {resumen['fallos']} fallos, cobertura {cobertura}%")
