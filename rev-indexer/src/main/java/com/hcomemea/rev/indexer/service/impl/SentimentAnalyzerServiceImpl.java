package com.hcomemea.rev.indexer.service.impl;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import com.hcomemea.rev.indexer.service.SentimentAnalyzerService;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.springframework.stereotype.Service;

@Service
public class SentimentAnalyzerServiceImpl implements SentimentAnalyzerService {
    private static final ImmutableList<String> keywords = ImmutableList.of(
            "bed",
            "room",
            "pool",
            "sauna",
            "staff",
            "location",
            "clean",
            "food",
            "price",
            "restaurant",
            "bar",
            "bathroom",
            "value",
            "quiet",
            "parking",
            "conditioning",
            "wifi",
            "internet",
            "beach",
            "casino",
            "shopping",
            "ski",
            "spa",
            "romantic",
            "tours",
            "transfer",
            "gym",
            "traffic",
            "noise",
            "suite",
            "surrounding",
            "decoration",
            "furniture",
            "shop",
            "nightlife",
            "concierge",
            "frontdesk",
            "lounge",
            "lobby",
            "garden",
            "minibar",
            "bathrobe",
            "safe",
            "breakfast",
            "shower",
            "bus",
            "shuttle",
            "station"
    );

    private static class StanfordCoreNLPLoader {
        private static final StanfordCoreNLP INSTANCE = load();

        private static StanfordCoreNLP load() {
            Properties props = new Properties();
            props.setProperty("annotators", "tokenize, ssplit, pos, lemma, parse, sentiment");
            StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
            return pipeline;
        }
    }


    @Override
    public Map<String, Integer> getKeywordSentimentMap(String review) {

        StanfordCoreNLP pipeline = StanfordCoreNLPLoader.INSTANCE;
        Map<String, Integer> keywordSentiments = newHashMap();
        if (review != null && review.length() > 0) {
            Annotation annotation = pipeline.process(review);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                final Set<String> tokens = ImmutableSet.copyOf(Iterables.transform(sentence.get(CoreAnnotations.TokensAnnotation.class), new Function<CoreLabel, String>() {
                    @Override
                    public String apply(CoreLabel input) {
                        return input.get(CoreAnnotations.LemmaAnnotation.class);
                    }
                }));
                List<String> keywordsInReview = newArrayList(Iterables.filter(keywords, new Predicate<String>() {
                    @Override
                    public boolean apply(String keyword) {
                        return tokens.contains(keyword);
                    }
                }));

                Tree tree = sentence.get(SentimentCoreAnnotations.AnnotatedTree.class);
                final int sentiment = RNNCoreAnnotations.getPredictedClass(tree);

                keywordSentiments.putAll(Maps.toMap(keywordsInReview, new Function<String, Integer>() {
                    @Override
                    public Integer apply(String keyword) {
                        return sentiment;
                    }
                }));
            }
        }
        return keywordSentiments;

    }
//
//    public static void main(String[] args) {
////        new SentimentAnalyzer().getKeywordSentimentMap("The staff was not very helpful");
//        Map<String, Integer> sentiments = new SentimentAnalyzerService().getKeywordSentimentMap("The Hotel Barocco is right off Barberini Piazza which is an easy walk to the Trevi Fountain, Spanish Steps, Pantheon and Piazza Navonne - great location. We took the hop on/hop off bus across the piazza so it's convenience is great. Hotel is small - small entry way and rooms are small but I guess it's Rome. Our room, 23, had a horrible smell like a sewer in it and we complained but they couldn't get rid of the smell. We stayed because our sons were in the room next door and it was on the second floor but we should have changed. 4 days in it was too much. It's beautifully decorated in elegant style though, the bed was great and the bathroom clean and large with a rain shower. Our shower didn't drain very well (perhaps the cause of the smell) and I ended up taking a shower in a pool of water. The breakfast was fabulous with an egg menu to choose from - great scrambled eggs. Lots to choose from for cheese, meats and fruits. The bartender, Nicola, is a true gem - perfect English, great martini and the BEST Bruschetta. He even gave me his recipe which is very doable and delicious. We asked for non touristy restaurant recommendations but got lots of tourists. Maybe it's just that Rome is swarming with tourists in June but I would rather have had a smaller, typical Italian restaurant. The food was good but like I said filled with tourists. I preferred our lunches when we just wandered into places. Staff is very friendly and helpful. All in all we'd recommend this hotel just not room 23 as our son's room, 22, was fine.");
//        //The Hotel Barocco is right off Barberini Piazza which is an easy walk to the Trevi Fountain, Spanish Steps, Pantheon and Piazza Navonne - great location.
//        System.out.println();
//    }

}
