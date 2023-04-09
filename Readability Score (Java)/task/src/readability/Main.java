package readability;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Array;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Path.of(fileName)));
    }
    public static void main(String[] args) throws IOException {
        String line = readFileAsString(args[0]);
        HashMap<String,Integer> polysyllables = new HashMap<>();
        Scanner scanner = new Scanner(System.in);
//        String line = scanner.nextLine();
        String[] sentences = line.split("[.!?]");
        // Meaningful words : split("[.!?,]?\\s+")
        String[] words = line.toLowerCase().split("[.!?,]?\\s+");
        String oneBigWord = String.join("",line.toLowerCase().split("\\s+"));
        HashSet<Character> vowelsSet = new HashSet<>(); //Vowel
        char[] vowels = "aiueoy".toCharArray();
        for(int i =0 ; i < vowels.length ; i++){
            vowelsSet.add(vowels[i]);
        }
        HashMap<String, Integer> syllables = new HashMap<>(words.length);

        for (int i = 0; i < words.length; i++) {
            int syllables_in_a_word = 0;
            boolean hasVowel = false;
            for (int j = 0; j < words[i].length(); j++) {
                    if (vowelsSet.contains(words[i].charAt(j))) {
                        hasVowel = true;
                    }
                    if (hasVowel && !vowelsSet.contains(words[i].charAt(j))) {
                        syllables_in_a_word++;
                        hasVowel = false;
                    }
            }
            if(vowelsSet.contains(words[i].charAt(words[i].length()-1)) && words[i].charAt(words[i].length()-1) != 'e'){
                syllables_in_a_word++;
            }
            if(syllables_in_a_word == 0){
                syllables_in_a_word ++;
            }
            if(syllables_in_a_word > 2){
                if(polysyllables.containsKey(words[i])){
                    polysyllables.put(words[i], polysyllables.get(words[i])+1);
                }else {
                    polysyllables.put(words[i], 1);
                }
            }
            if(syllables_in_a_word >= 1)
            {
                if(!syllables.containsKey(words[i])){
                    syllables.put(words[i],syllables_in_a_word);
                }
                else{
                    syllables.put(words[i], syllables.get(words[i])+syllables_in_a_word);
                }
            }
        }
        int totalSyllables = 0;
        int totalPolysyllables = 0;
        for (Map.Entry<String, Integer> entry: syllables.entrySet()
             ) {
            totalSyllables += entry.getValue();
        }

        for(Map.Entry<String, Integer> entry: polysyllables.entrySet()){
            totalPolysyllables += entry.getValue();
        }
        System.out.println("The text is:");
        System.out.println(line);
        System.out.println();
        System.out.println("Words: "+words.length);
        System.out.println("Sentences: "+ sentences.length);
        System.out.println("Characters: "+ oneBigWord.length());
        System.out.println("Syllables: "+ totalSyllables);
        System.out.println("Polysyllables: "+ totalPolysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        float automated_score = (float) (4.71 * oneBigWord.length()/words.length + 0.5 * words.length/sentences.length - 21.43);
        double SMOG_score = ((1.043 * Math.sqrt((float)(totalPolysyllables*30)/sentences.length)) + 3.1291);
        float FK_score = (float)((0.39 * words.length)/sentences.length + (11.8 * totalSyllables)/words.length - 15.59);
        float CL_score = (float)((5.88 * oneBigWord.length())/words.length - (29.6*sentences.length/words.length) - 15.8);
//        int automated_age = Math.round(automated_score) > automated_score ? (Math.round(automated_score) + 4) : (Math.round(automated_score) + 5);
        double automated_age = Math.ceil(automated_score) + 5;
        double FK_age = Math.ceil(FK_score) + 5;
        double SMOG_age = Math.ceil(SMOG_score) + 5;
        double CL_age = Math.ceil(CL_score) + 6;
        String option = scanner.nextLine();
        switch (option){
            case "ARI":{
                System.out.println("Automated Readability Index: "+automated_score
                +" (about "
                +automated_age+
                "-year-olds).");
                break;
            }
            case "FK":{
                System.out.println("Flesch–Kincaid readability tests: "+FK_score
                        +" (about "
                        +FK_age+
                        "-year-olds).");
                break;
            }
            case "SMOG":{
                System.out.println("Simple Measure of Gobbledygook: "+SMOG_score
                        +" (about "
                        +SMOG_age+
                        "-year-olds).");
                break;
            }
            case "CL":{
                System.out.println("Coleman–Liau index: "+CL_score
                        +" (about "
                        +CL_age+
                        "-year-olds).");
                break;
            }
            case "all":{
                System.out.println("Automated Readability Index: "+automated_score
                        +" (about "
                        +automated_age+
                        "-year-olds).");
                System.out.println("Flesch–Kincaid readability tests: "+FK_score
                        +" (about "
                        +FK_age+
                        "-year-olds).");
                System.out.println("Simple Measure of Gobbledygook: "+SMOG_score
                        +" (about "
                        +SMOG_age+
                        "-year-olds).");
                System.out.println("Coleman–Liau index: "+CL_score
                        +" (about "
                        +CL_age+
                        "-year-olds).");
                break;
            }
        }
        System.out.println("This text should be understood in average by"+ (automated_age + FK_age + SMOG_age + CL_age)/4+"-year-olds.");

    }
}
