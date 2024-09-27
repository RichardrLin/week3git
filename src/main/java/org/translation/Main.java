package org.translation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Main class for this program.
 * Complete the code according to the "to do" notes.<br/>
 * The system will:<br/>
 * - prompt the user to pick a country name from a list<br/>
 * - prompt the user to pick the language they want it translated to from a list<br/>
 * - output the translation<br/>
 * - at any time, the user can type quit to quit the program<br/>
 */
public class Main {

    /**
     * This is the main entry point of our Translation System!<br/>
     * A class implementing the Translator interface is created and passed into a call to runProgram.
     * @param args not used by the program
     */
    public static void main(String[] args) {
        Translator translator = new JSONTranslator();

        runProgram(translator);
    }

    /**
     * This is the method which we will use to test your overall program, since
     * it allows us to pass in whatever translator object that we want!
     * See the class Javadoc for a summary of what the program will do.
     * @param translator the Translator implementation to use in the program
     */
    public static void runProgram(Translator translator) {
        while (true) {
            String q = "quit";
            String country = promptForCountry(translator);
            if (q.equals(country)) {
                break;
            }
            CountryCodeConverter converter = new CountryCodeConverter();
            String language = promptForLanguage(translator, converter.fromCountry(country));

            if (language.equals(q)) {
                break;
            }

            LanguageCodeConverter langConverter = new LanguageCodeConverter();

            System.out.println(country + " in " + language + " is " + translator.translate(country,
                    langConverter.fromLanguage(language)));
            System.out.println("Press enter to continue or quit to exit.");
            Scanner s = new Scanner(System.in);
            String textTyped = s.nextLine();

            if (q.equals(textTyped)) {
                break;
            }
        }
    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForCountry(Translator translator) {
        CountryCodeConverter countryCodeConverter = new CountryCodeConverter("country-codes.txt");

        List<String> countries = translator.getCountries();
        List<String> countryNames = new ArrayList<>();

        for (String country : countries) {
            String countryName = countryCodeConverter.fromCountryCode(country);
            countryNames.add(countryName);
        }
        Collections.sort(countryNames);

        for (String countryName : countryNames) {
            System.out.println(countryName);
        }
        Scanner s = new Scanner(System.in);
        return s.nextLine();

    }

    // Note: CheckStyle is configured so that we don't need javadoc for private methods
    private static String promptForLanguage(Translator translator, String country) {
        List<String> langaugeCodes = new ArrayList<>(translator.getCountryLanguages(country));
        List<String> languageNames = new ArrayList<>();

        for (String langCode : langaugeCodes) {
            String langName = translator.translate(country, langCode);
            if (langName != null) {
                languageNames.add(langName);
            }
        }
        Collections.sort(languageNames);

        for (String langName : languageNames) {
            System.out.println(langName);
        }

        Scanner s = new Scanner(System.in);
        return s.nextLine();
    }
}
