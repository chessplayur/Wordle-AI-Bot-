import java.io.*;
import java.nio.file.*;
import java.util.*;

public class WordSolver {

    private static final String FIRST_GUESS = "SLATE";

    private static final List<String> allWords = new ArrayList<>();
    private static List<String> candidates = new ArrayList<>();

    public static void main(String[] args) throws Exception {

        loadWords("words.txt");

        if (allWords.isEmpty()) {
            System.out.println("No words loaded.");
            return;
        }

        candidates = new ArrayList<>(allWords);

        Scanner scanner = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("          WORDLE SOLVER");
        System.out.println("=====================================");
        System.out.println();
        System.out.println("Feedback format:");
        System.out.println("G = Green");
        System.out.println("Y = Yellow");
        System.out.println("B = Gray");
        System.out.println();
        System.out.println("Example: BBGYB");
        System.out.println();

        String guess = FIRST_GUESS;
        int round = 1;

        while (true) {

            System.out.println("-------------------------------------");
            System.out.println("Round #" + round);
            System.out.println("Suggested Guess: " + guess);
            System.out.println("-------------------------------------");

            System.out.print("Enter feedback: ");
            String feedback = scanner.nextLine()
                    .trim()
                    .toUpperCase();

            if (feedback.equals("GGGGG")) {
                System.out.println("Solved!");
                break;
            }

            if (!isValidFeedback(feedback)) {
                System.out.println("Invalid feedback.");
                continue;
            }

            filterCandidates(guess, feedback);

            System.out.println();
            System.out.println("Remaining candidates: " + candidates.size());

            if (candidates.isEmpty()) {
                System.out.println("No valid candidates remain.");
                break;
            }

            if (candidates.size() <= 20) {
                System.out.println("Candidates:");
                for (String word : candidates) {
                    System.out.println("  " + word);
                }
            }

            guess = chooseBestGuess();

            round++;
        }

        scanner.close();
    }

    private static void loadWords(String fileName) throws IOException {

        List<String> lines = Files.readAllLines(Paths.get(fileName));

        for (String line : lines) {

            String word = line.trim().toUpperCase();

            if (word.length() == 5) {
                allWords.add(word);
            }
        }

        System.out.println("Loaded " + allWords.size() + " words.");
    }

    private static boolean isValidFeedback(String s) {

        if (s.length() != 5)
            return false;

        for (char c : s.toCharArray()) {

            if (c != 'G' &&
                c != 'Y' &&
                c != 'B') {
                return false;
            }
        }

        return true;
    }

    private static void filterCandidates(
            String guess,
            String feedback) {

        List<String> next = new ArrayList<>();

        for (String candidate : candidates) {

            String generated =
                    generateFeedback(
                            guess,
                            candidate);

            if (generated.equals(feedback)) {
                next.add(candidate);
            }
        }

        candidates = next;
    }
    // Hello
    private static String generateFeedback(
            String guess,
            String answer) {

        char[] result = {'B','B','B','B','B'};

        boolean[] used =
                new boolean[5];

        for (int i = 0; i < 5; i++) {

            if (guess.charAt(i) ==
                    answer.charAt(i)) {

                result[i] = 'G';
                used[i] = true;
            }
        }

        for (int i = 0; i < 5; i++) {

            if (result[i] == 'G')
                continue;

            char g = guess.charAt(i);

            for (int j = 0; j < 5; j++) {

                if (!used[j] &&
                        answer.charAt(j) == g) {

                    result[i] = 'Y';
                    used[j] = true;
                    break;
                }
            }
        }

        return new String(result);
    }

    private static String chooseBestGuess() {

        Map<Character,Integer> frequency =
                buildFrequencyMap();

        int bestScore = -1;
        String bestWord = candidates.get(0);

        for (String word : candidates) {

            int score =
                    scoreWord(
                            word,
                            frequency);

            if (score > bestScore) {

                bestScore = score;
                bestWord = word;
            }
        }

        return bestWord;
    }

    private static Map<Character,Integer>
    buildFrequencyMap() {

        Map<Character,Integer> freq =
                new HashMap<>();

        for (String word : candidates) {

            Set<Character> seen =
                    new HashSet<>();

            for (char c :
                    word.toCharArray()) {

                if (seen.add(c)) {

                    freq.put(
                            c,
                            freq.getOrDefault(c,0)
                                    + 1);
                }
            }
        }

        return freq;
    }

    private static int scoreWord(
            String word,
            Map<Character,Integer> freq) {

        int score = 0;

        Set<Character> used =
                new HashSet<>();

        for (char c :
                word.toCharArray()) {

            if (used.add(c)) {

                score +=
                        freq.getOrDefault(
                                c,
                                0);
            }
        }

        return score;
    }
}