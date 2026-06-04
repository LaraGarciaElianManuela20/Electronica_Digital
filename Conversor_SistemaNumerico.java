import java.util.Scanner;

public class Conversor_SistemaNumerico {
    static final String[] HEX_CHARS = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            menuPrincipal();
            System.out.print("\nSeleccione opción: ");
            
            try {
                choice = Integer.parseInt(sc.next());
            } catch (Exception e) {
                choice = 0;
            }

            switch (choice) {
                case 1: 
                    int decimal = Integer.parseInt(prompt(sc, "decimal"));
                    decimalAOtroBase(decimal, 2, "BINARIO"); 
                    break;
                case 2: 
                    decimal = Integer.parseInt(prompt(sc, "decimal"));
                    decimalAOtroBase(decimal, 8, "OCTAL"); 
                    break;
                case 3: 
                    decimal = Integer.parseInt(prompt(sc, "decimal"));
                    decimalAOtroBase(decimal, 16, "HEXADECIMAL"); 
                    break;
                case 4: 
                    String binario = prompt(sc, "binario (solo 0s y 1s)");
                    otroBaseADecimal(binario, 2, "BINARIO"); 
                    break;
                case 5: 
                    String octal = prompt(sc, "octal (0-7)");
                    otroBaseADecimal(octal, 8, "OCTAL"); 
                    break;
                case 6: 
                    String hexa = prompt(sc, "hexadecimal (0-9, A-F)");
                    otroBaseADecimal(hexa, 16, "HEXADECIMAL"); 
                    break;
                case 7: 
                    System.out.println("\n¡Gracias por usar el conversor!\n");
                    break;
                default: 
                    System.out.println("❌ Opción inválida. Intente de nuevo.");
            }
        } while (choice != 7);
        sc.close();
    }

    static void menuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║   CONVERSOR DE SISTEMAS NUMÉRICOS      ║");
        System.out.println("╚════════════════════════════════════════╝");
        System.out.println("  1. Decimal → Binario");
        System.out.println("  2. Decimal → Octal");
        System.out.println("  3. Decimal → Hexadecimal");
        System.out.println("  4. Binario → Decimal");
        System.out.println("  5. Octal → Decimal");
        System.out.println("  6. Hexadecimal → Decimal");
        System.out.println("  7. Salir");
    }

    static String prompt(Scanner sc, String type) {
        System.out.print("Ingrese número " + type + ": ");
        return sc.next();
    }

    // ====== CONVERSIONES DESDE DECIMAL ======
    static void decimalAOtroBase(int decimal, int base, String baseName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CONVERSIÓN DE DECIMAL A " + baseName);
        System.out.println("=".repeat(60));
        
        explicacionMetodo(baseName, base);
        System.out.println("\n📊 NÚMERO A CONVERTIR: " + decimal);
        
        if (decimal == 0) {
            System.out.println("✓ Resultado: 0");
            return;
        }

        StringBuilder resultado = new StringBuilder();
        int tempDecimal = decimal;
        int paso = 1;

        System.out.println("\n🔢 PROCESO DE CONVERSIÓN:");
        System.out.println("   Se divide repetidamente entre " + base + " hasta llegar a 0");
        System.out.println("   Los residuos se leen de abajo hacia arriba\n");

        while (tempDecimal > 0) {
            int residuo = tempDecimal % base;
            int cociente = tempDecimal / base;
            String digito = (base == 16) ? HEX_CHARS[residuo] : String.valueOf(residuo);
            
            resultado.insert(0, digito);
            
            System.out.printf("   Paso %d: %d ÷ %d = %d con residuo %s%n", 
                paso, tempDecimal, base, cociente, digito);
            
            tempDecimal = cociente;
            paso++;
        }

        System.out.println("\n📝 LECTURA DE RESIDUOS (de abajo hacia arriba): " + resultado.toString());
        System.out.println("\n✅ RESULTADO FINAL: " + decimal + " (decimal) = " + resultado.toString() + " (" + baseName.toLowerCase() + ")");
    }

    // ====== CONVERSIONES A DECIMAL ======
    static void otroBaseADecimal(String numero, int base, String baseName) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("CONVERSIÓN DE " + baseName + " A DECIMAL");
        System.out.println("=".repeat(60));
        
        explicacionMetodo(baseName, base);
        System.out.println("\n📊 NÚMERO A CONVERTIR: " + numero);

        int decimal = 0;
        int power = 0;
        int paso = 1;

        System.out.println("\n🔢 PROCESO DE CONVERSIÓN:");
        System.out.println("   Se multiplica cada dígito por " + base + " elevado a su posición");
        System.out.println("   La posición comienza en 0 desde la DERECHA\n");

        // Procesar de derecha a izquierda
        for (int i = numero.length() - 1; i >= 0; i--) {
            char caracter = numero.charAt(i);
            int digitoValor = getDigitValue(caracter, base);
            
            if (digitoValor == -1) {
                System.out.println("❌ Error: '" + caracter + "' no es un dígito válido para base " + base);
                return;
            }

            int termino = digitoValor * (int) Math.pow(base, power);
            String baseChar = (base == 16) ? " (representado como " + caracter + ")" : "";
            
            System.out.printf("   Paso %d: %d × %d^%d = %d × %d = %d%s%n", 
                paso, digitoValor, base, power, digitoValor, 
                (int)Math.pow(base, power), termino, baseChar);
            
            decimal += termino;
            power++;
            paso++;
        }

        System.out.println("\n📝 SUMA DE TODOS LOS TÉRMINOS:");
        power = 0;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int dv = getDigitValue(numero.charAt(i), base);
            System.out.print((int)Math.pow(base, power) * dv);
            if (i > 0) System.out.print(" + ");
            power++;
        }
        System.out.println(" = " + decimal);

        System.out.println("\n✅ RESULTADO FINAL: " + numero + " (" + baseName.toLowerCase() + ") = " + decimal + " (decimal)");
    }

    // ====== EXPLICACIÓN DEL MÉTODO ======
    static void explicacionMetodo(String baseName, int base) {
        System.out.println("\n📚 EXPLICACIÓN DEL SISTEMA " + baseName + ":");
        
        switch (baseName) {
            case "BINARIO":
                System.out.println("   • Base: 2 (solo usa dígitos 0 y 1)");
                System.out.println("   • Cada posición representa una potencia de 2");
                System.out.println("   • Ejemplo: 1011₂ = 1×2³ + 0×2² + 1×2¹ + 1×2⁰ = 11₁₀");
                break;
            case "OCTAL":
                System.out.println("   • Base: 8 (usa dígitos 0-7)");
                System.out.println("   • Cada posición representa una potencia de 8");
                System.out.println("   • Ejemplo: 17₈ = 1×8¹ + 7×8⁰ = 15₁₀");
                break;
            case "HEXADECIMAL":
                System.out.println("   • Base: 16 (usa dígitos 0-9 y letras A-F)");
                System.out.println("   • A=10, B=11, C=12, D=13, E=14, F=15");
                System.out.println("   • Cada posición representa una potencia de 16");
                System.out.println("   • Ejemplo: 1F₁₆ = 1×16¹ + 15×16⁰ = 31₁₀");
                break;
        }
    }

    // ====== VALIDACIÓN DE DÍGITOS ======
    static int getDigitValue(char c, int base) {
        int val;
        if (c >= '0' && c <= '9') {
            val = c - '0';
        } else if (c >= 'A' && c <= 'F') {
            val = 10 + (c - 'A');
        } else if (c >= 'a' && c <= 'f') {
            val = 10 + (c - 'a');
        } else {
            return -1;
        }
        return (val < base) ? val : -1;
    }
}
