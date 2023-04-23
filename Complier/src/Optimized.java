import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Optimized {
    private Map<String, String> variables;
    private List<String> code;
    private int labelCount;
    private int tempCount;

    public Optimized() {
        variables = new HashMap<>();
        code = new ArrayList<>();
        labelCount = 0;
        tempCount = 0;
    }

    public List<String> generateTAC(String input) {
        String[] lines = input.split("\n");
        for (String line : lines) {
            processLine(line.trim());
        }

        for (String tac : code) {
            generateAssemblyCode(tac);
        }

        return code;
    }


    private void processLine(String line) {
        if (line.startsWith("LET")) {
            processLet(line.substring(3).trim());
        } else if (line.startsWith("IF")) {
            processIf(line.substring(2).trim());
        } else if (line.equals("ENDIF")) {
            processEndIf();
        } else if (line.startsWith("PRINT")) {
            processPrint(line.substring(5).trim());
        }
    }

    private void processLet(String line) {
        String[] tokens = line.split("=");
        String lhs = tokens[0].trim();
        String rhs = tokens[1].trim();
        String t;
        if (rhs.matches("\\d+")) {
            t = rhs;
        } else {
            t = newTemp();
            code.add(t + " = " + rhs);
        }
        variables.put(lhs, t);
    }


    private void processIf(String line) {
        String[] tokens = line.split("THEN");
        String cond = tokens[0].trim();
        String label = newLabel();
        String t;
        if (cond.matches("\\d+")) {
            t = cond;
        } else {
            t = newTemp();
            code.add(t + " = " + cond);
        }
        code.add("if " + t + " goto " + label);
        code.add("goto L" + (labelCount + 1));
        code.add(label + ":");
        labelCount++;
    }


    private void processEndIf() {
        code.add("L" + labelCount + ":");
    }

    private void processPrint(String line) {
        String[] vars = line.split(",");
        for (String var : vars) {
            var = var.trim();
            String t = variables.get(var);
            if (t.matches("\\d+")) {
                code.add("print " + t);
            } else {
                code.add("print " + t);
            }
        }
        code.add("goto L" + (labelCount + 1));
    }


    private String newTemp() {
        tempCount++;
        return "t" + tempCount;
    }

    private String newLabel() {
        labelCount++;
        return "L" + labelCount;
    }


    private void generateAssemblyCode(String tac) {
        String[] tokens = tac.split(" ");

        if (tokens.length >= 2) {
            switch(tokens[1]) {
                case "=":
                    generateAssignmentCode(tokens[0], tokens[2]);
                    break;
                case "+":
                    generateAdditionCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "*":
                    generateMultiplicationCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "/":
                    generateDivisionCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "-":
                    generateSubtractionCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "^":
                    generatePowerCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case ">":
                    generateGreaterThanCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "<":
                    generateLessThanCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "<=":
                    generateLessThanOrEqualCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case ">=":
                    generateGreaterThanOrEqualCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "let":
                    generateVariableDeclarationCode(tokens[2]);
                    break;
                case "print":
                    generatePrintCode(tokens[2]);
                    break;
                case "then":
                    generateThenCode(tokens[0], tokens[2], tokens[4]);
                    break;
                case "if":
                    generateIfCode(tokens[2], tokens[4], tokens[6]);
                    break;
                case "goto":
                    generateGotoCode(tokens[2]);
                    break;

            }
        }
    }

    private void generateAssignmentCode(String destination, String source) {

        String assemblyCode = "mov eax, " + source + "\n";
        assemblyCode += "mov " + destination + ", eax\n";


        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateAdditionCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "add eax, " + operand2 + "\n";
        assemblyCode += "mov " + destination + ", eax\n";


        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateMultiplicationCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "imul eax, " + operand2 + "\n";
        assemblyCode += "mov " + destination + ", eax";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateDivisionCode(String destination, String numerator, String denominator) {
        String assemblyCode = "mov eax, " + numerator + "\n";
        assemblyCode += "cdq\n";
        assemblyCode += "idiv " + denominator + "\n";
        assemblyCode += "mov " + destination + ", eax";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateSubtractionCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "sub eax, " + operand2 + "\n";
        assemblyCode += "mov " + destination + ", eax";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generatePowerCode(String destination, String base, String exponent) {
        String assemblyCode = "mov eax, " + base + "\n";
        assemblyCode += "mov ecx, " + exponent + "\n";
        assemblyCode += "mov ebx, eax\n";
        assemblyCode += "dec ecx\n";
        assemblyCode += "jz _endPow\n";
        assemblyCode += "_powLoop:\n";
        assemblyCode += "mul ebx\n";
        assemblyCode += "dec ecx\n";
        assemblyCode += "jnz _powLoop\n";
        assemblyCode += "_endPow:\n";
        assemblyCode += "mov " + destination + ", eax\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateGreaterThanCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "cmp eax, " + operand2 + "\n";
        assemblyCode += "setg al\n";
        assemblyCode += "movzx eax, al\n";
        assemblyCode += "mov " + destination + ", eax\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateLessThanCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "cmp eax, " + operand2 + "\n";
        assemblyCode += "setl al\n";
        assemblyCode += "movzx eax, al\n";
        assemblyCode += "mov " + destination + ", eax\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateLessThanOrEqualCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "cmp eax, " + operand2 + "\n";
        assemblyCode += "setle al\n";
        assemblyCode += "movzx eax, al\n";
        assemblyCode += "mov " + destination + ", eax\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateGreaterThanOrEqualCode(String destination, String operand1, String operand2) {
        String assemblyCode = "mov eax, " + operand1 + "\n";
        assemblyCode += "cmp eax, " + operand2 + "\n";
        assemblyCode += "setge al\n";
        assemblyCode += "movzx eax, al\n";
        assemblyCode += "mov " + destination + ", eax\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateVariableDeclarationCode(String variableName) {
        String assemblyCode = "section .data\n";
        assemblyCode += variableName + " dd 0\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generatePrintCode(String variableName) {
        String assemblyCode = "section .data\n";
        assemblyCode += "print_format db '%d', 10, 0\n"; // format string for printing integers
        assemblyCode += "section .text\n";
        assemblyCode += "global main\n";
        assemblyCode += "extern printf\n";
        assemblyCode += "main:\n";
        assemblyCode += "push ebp\n";
        assemblyCode += "mov ebp, esp\n";
        assemblyCode += "push dword [" + variableName + "]\n";
        assemblyCode += "push dword print_format\n";
        assemblyCode += "call printf\n";
        assemblyCode += "add esp, 8\n";
        assemblyCode += "mov eax, 0\n";
        assemblyCode += "pop ebp\n";
        assemblyCode += "ret\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateThenCode(String condition, String label, String keyword) {
        String assemblyCode = "cmp " + condition + ", 0\n";
        assemblyCode += "jz " + label + "\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }

    private void generateIfCode(String condition, String trueLabel, String falseLabel) {
        String assemblyCode = "cmp " + condition + ", 0\n";
        assemblyCode += "jnz " + trueLabel + "\n";
        assemblyCode += "jmp " + falseLabel + "\n";

        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
    private void generateGotoCode(String label) {
        String assemblyCode = "jmp " + label;
        try {
            FileWriter writer = new FileWriter("assembly.txt", true);
            writer.write(assemblyCode);
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred while writing to the file.");
            e.printStackTrace();
        }
    }
}