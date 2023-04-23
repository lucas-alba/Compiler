import java.util.*;

class TACGenerator {
    private Map<String, String> variables;
    private List<String> code;
    private int labelCount;
    private int tempCount;

    public TACGenerator() {
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
        String t = newTemp();
        variables.put(lhs, t);
        code.add(t + " = " + rhs);
    }

    private void processIf(String line) {
        String[] tokens = line.split("THEN");
        String cond = tokens[0].trim();
        String label = newLabel();
        String t = newTemp();
        code.add(t + " = " + cond);
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
            code.add("print " + t);
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
}
