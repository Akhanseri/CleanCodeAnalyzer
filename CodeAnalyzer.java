import java.io.*;
import java.util.*;
import java.util.regex.*;

public class CodeAnalyzer {
    private static final String BASE_DIRECTORY = "ваш путь";
    private static List<String> allMethodNames = new ArrayList<>();
    private static List<Integer> allNestingLevels = new ArrayList<>();
    private static List<Integer> allMethodLines = new ArrayList<>();
    private static final Pattern METHOD_DEFINITION = Pattern.compile("^(public|protected|private)\\s+.*\\s+(\\w+)\\s*\\(");

    public static void main(String[] args) {
        try {
            File baseDir = new File(BASE_DIRECTORY);
            if (baseDir.isDirectory()) {
                processDirectory(baseDir);
                analyzeOverall();
            } else {
                System.out.println("Invalid directory: " + BASE_DIRECTORY);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".java")) {
                    try {
                        String className = file.getName().replace(".java", "");
                        analyzeClass(file, className);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private static void analyzeClass(File file, String className) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            List<String> methodNames = new ArrayList<>();
            List<Integer> methodLines = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                Matcher matcher = METHOD_DEFINITION.matcher(line);
                if (matcher.find()) {
                    String methodName = matcher.group(2);
                    methodNames.add(methodName);
                    methodLines.add(0);
                }
                if (!methodNames.isEmpty()) {
                    int lastIndex = methodNames.size() - 1;
                    methodLines.set(lastIndex, methodLines.get(lastIndex) + 1);
                }
            }

            allMethodNames.addAll(methodNames);
            allMethodLines.addAll(methodLines);
            for (int i = 0; i < methodNames.size(); i++) {
                int nestingLevel = analyzeMethod(file, methodNames.get(i));
                allNestingLevels.add(nestingLevel);
            }
        }
    }

    private static int analyzeMethod(File file, String methodName) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            int maxNestingLevel = 0, currentNestingLevel = 0;
            boolean insideMethod = false;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.contains(methodName + "(")) {
                    insideMethod = true;
                }
                if (insideMethod) {
                    for (char c : line.toCharArray()) {
                        if (c == '{') {
                            currentNestingLevel++;
                            if (currentNestingLevel > maxNestingLevel) {
                                maxNestingLevel = currentNestingLevel;
                            }
                        } else if (c == '}') {
                            currentNestingLevel--;
                        }
                    }
                    if (line.contains("}")) {
                        insideMethod = false;
                    }
                }
            }
            return maxNestingLevel;
        }
    }

    private static void analyzeOverall() {
        analyzeCamelCase(allMethodNames);
        findTopNestingLevels(allMethodNames, allMethodLines, allNestingLevels);
    }

    private static void analyzeCamelCase(List<String> methodNames) {
        int totalMethods = methodNames.size();
        long camelCaseMethods = methodNames.stream().filter(CodeAnalyzer::isValidMethodName).count();
        double percentage = (double) camelCaseMethods / totalMethods * 100;
        System.out.println("Overall percentage of methods following CamelCase convention: " + percentage + "%");
    }

    public static boolean isValidMethodName(String methodName) {
        // Pattern for CamelCase
        Pattern pattern = Pattern.compile("^(?:(has|is|get|set|add|remove|update|calculate|initialize|validate|create|generate|retrieve|increment|decrement|counter|load|store)[A-Z][a-z]*|.+[A-Z][a-z]*)(?:[A-Z][a-z]*)?$");


        Matcher matcher = pattern.matcher(methodName);


        return matcher.matches();
    }

    private static void findTopNestingLevels(List<String> methodNames, List<Integer> methodLines, List<Integer> nestingLevels) {


        if (methodNames.isEmpty()) {
            System.out.println("No methods analyzed.");
            return;
        }

        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < methodNames.size(); i++) {
            indices.add(i);
        }

        indices.sort(Comparator.comparing(nestingLevels::get).reversed());
        System.out.println("Top 3 methods with highest nesting levels:");
        int limit = Math.min(3, methodNames.size());
        for (int i = 0; i < limit; i++) {
            int index = indices.get(i);
            System.out.println(methodNames.get(index) + " - Nesting Level: " + nestingLevels.get(index) + ", Lines of Code: " + methodLines.get(index));
        }
    }
}
