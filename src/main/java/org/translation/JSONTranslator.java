package org.translation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * An implementation of the Translator interface which reads in the translation
 * data from a JSON file. The data is read in once each time an instance of this class is constructed.
 */
public class JSONTranslator implements Translator {

    private Map<String, Map<String, String>> countryTranslations;
    /**
     * Constructs a JSONTranslator using data from the sample.json resources file.
     */

    public JSONTranslator() {
        this("sample.json");
    }

    /**
     * Constructs a JSONTranslator populated using data from the specified resources file.
     * @param filename the name of the file in resources to load the data from
     * @throws RuntimeException if the resource file can't be loaded properly
     */
    public JSONTranslator(String filename) {
        // read the file to get the data to populate things...
        countryTranslations = new HashMap<>();
        try {

            String jsonString = Files.readString(Paths.get(getClass().getClassLoader().getResource(filename).toURI()));

            JSONArray jsonArray = new JSONArray(jsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String alpha3Code = jsonObject.getString("alpha3");
                Map<String, String> languageTranslations = new HashMap<>();

                for (String langCode : jsonObject.keySet()) {
                    if (!"alpha2".equals(langCode) && !"id".equals(langCode) && !"alpha3".equals(langCode)) {
                        languageTranslations.put(langCode, jsonObject.getString(langCode));
                    }
                }
                countryTranslations.put(alpha3Code, languageTranslations);
            }

        }
        catch (IOException | URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public List<String> getCountryLanguages(String country) {
        Map<String, String> languageTranslations = countryTranslations.get(country);
        if (languageTranslations == null) {
            return null;
        }
        List<String> languages = new ArrayList<>();
        for (String langCode : languageTranslations.keySet()) {
            languages.add(langCode);
        }
        return languages;
    }

    @Override
    public List<String> getCountries() {
        List<String> countries = new ArrayList<>(countryTranslations.keySet());
        Collections.sort(countries);
        return List.copyOf(countries);
    }

    @Override
    public String translate(String country, String language) {
        Map<String, String> languageTranslations = countryTranslations.get(country);
        if (languageTranslations != null && languageTranslations.containsKey(language)) {
            return languageTranslations.get(language);
        }
        return null;
    }
    }
