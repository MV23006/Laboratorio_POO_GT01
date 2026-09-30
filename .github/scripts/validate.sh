#!/bin/bash
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0;0m'

echo "🚀 Iniciando validación de Laboratorio 2..."

# 1. Verificar archivo build.gradle y dependencia de Gson
echo "✅ Verificando configuración de Gradle..."
if [ ! -f "build.gradle" ]; then
    echo -e "${RED}❌ ERROR: No se encontró el archivo build.gradle en la raíz del proyecto.${NC}"
    exit 1
fi

if ! grep -qi "gson" "build.gradle"; then
    echo -e "${RED}❌ ERROR: El archivo build.gradle no declara la dependencia de 'gson'.${NC}"
    exit 1
fi
echo -e "${GREEN}✔️ Archivo build.gradle y dependencia de Gson verificados.${NC}"

# 2. Verificación de Clases Obligatorias y Estructura POO
echo "✅ Verificando clases obligatorias y sintaxis POO..."
FILES=(
    "src/main/java/org/laboratorio2/config/ConfiguracionFlota.java"
    "src/main/java/org/laboratorio2/model/Vehiculo.java"
    "src/main/java/org/laboratorio2/model/VehiculoCarga.java"
    "src/main/java/org/laboratorio2/model/VehiculoPasajeros.java"
    "src/main/java/org/laboratorio2/dto/ResumenFlotaDTO.java"
    "src/main/java/org/laboratorio2/service/RepositorioGenerico.java"
    "src/main/java/org/laboratorio2/service/GestorFlota.java"
    "src/main/java/org/laboratorio2/controller/Main.java"
)

for file in "${FILES[@]}"; do
    if [ ! -f "$file" ]; then
        echo -e "${RED}❌ ERROR: Falta el archivo obligatorio '$file'.${NC}"
        exit 1
    fi
done

# Validar clase abstracta
if ! grep -q "abstract class" "src/main/java/org/laboratorio2/model/Vehiculo.java"; then
    echo -e "${RED}❌ ERROR: Vehiculo debe ser una clase abstracta ('abstract class').${NC}"
    exit 1
fi

# Validar herencia (extends)
if ! grep -q "extends Vehiculo" "src/main/java/org/laboratorio2/model/VehiculoCarga.java" || \
   ! grep -q "extends Vehiculo" "src/main/java/org/laboratorio2/model/VehiculoPasajeros.java"; then
    echo -e "${RED}❌ ERROR: Las subclases de vehículos deben utilizar 'extends Vehiculo'.${NC}"
    exit 1
fi

# Validar implementación de interfaz (implements)
if ! grep -q "implements RepositorioGenerico" "src/main/java/org/laboratorio2/service/GestorFlota.java"; then
    echo -e "${RED}❌ ERROR: GestorFlota debe utilizar 'implements RepositorioGenerico'.${NC}"
    exit 1
fi

# Validar anotación @Override
if ! grep -q "@Override" "src/main/java/org/laboratorio2/model/VehiculoCarga.java" || \
   ! grep -q "@Override" "src/main/java/org/laboratorio2/service/GestorFlota.java"; then
    echo -e "${RED}❌ ERROR: Faltan anotaciones '@Override' en la implementación/sobrescritura de métodos.${NC}"
    exit 1
fi
echo -e "${GREEN}✔️ Clases obligatorias, herencia (extends), interfaz (implements) y @Override verificados.${NC}"

# 3. Descargar librería temporal para compilación aislada
if [ ! -f "gson.jar" ]; then
    echo "Descargando librería Gson para ejecución..."
    wget -q https://repo1.maven.org/maven2/com/google/code/gson/gson/2.10.1/gson-2.10.1.jar -O gson.jar
fi

# 4. Compilación
echo "✅ Compilando proyecto..."
mkdir -p bin
javac -cp gson.jar -d bin $(find src -name "*.java") 2>/dev/null
if [ $? -ne 0 ]; then echo -e "${RED}❌ ERROR DE COMPILACIÓN.${NC}"; exit 1; fi

# 5. Pruebas unitarias dinámicas
cat <<EOF > TestRunner.java
import org.laboratorio2.config.ConfiguracionFlota;
import org.laboratorio2.model.*;
public class TestRunner {
    public static void main(String[] args) {
        boolean pass = true;
        ConfiguracionFlota c1 = ConfiguracionFlota.getInstance();
        ConfiguracionFlota c2 = ConfiguracionFlota.getInstance();
        if(c1 != c2) { System.out.println("❌ ERROR: ConfiguracionFlota no es Singleton"); pass = false; }
        
        Vehiculo v = new VehiculoCarga(1, "TEST", 100.0, 2.0);
        if(v.calcularCostoMantenimiento() != 200.0) { System.out.println("❌ ERROR: Polimorfismo falló"); pass = false; }
        
        if(!pass) System.exit(1);
    }
}
EOF
javac -cp bin:gson.jar TestRunner.java
java -cp bin:.:gson.jar TestRunner
if [ $? -ne 0 ]; then exit 1; fi

# 6. Ejecución del Main y verificación de JSON
echo "✅ Ejecutando Main.java..."
java -cp bin:gson.jar org.laboratorio2.controller.Main > /dev/null

echo "✅ Verificando persistencia JSON..."
if [ ! -s "flota.json" ]; then
    echo -e "${RED}❌ ERROR: El archivo 'flota.json' no existe o está vacío. Revisa la serialización con Gson.${NC}"
    exit 1
else
    echo -e "${GREEN}✔️ Archivo 'flota.json' generado exitosamente con datos.${NC}"
fi

echo -e "${GREEN}✅ Todos los tests del Laboratorio 2 aprobados.${NC}"
exit 0