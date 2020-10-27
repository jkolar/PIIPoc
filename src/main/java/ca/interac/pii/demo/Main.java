package ca.interac.pii.demo;

import ca.interac.pii.annotations.ContainsPIIFields;
import ca.interac.pii.annotations.MaskIt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.lookup.StrLookup;
import org.apache.logging.log4j.core.lookup.StrSubstitutor;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@ContainsPIIFields
public class Main {

    private static final Logger logger = LogManager.getLogger("ApplicationLog");
    private static final Logger loggerPayload = LogManager.getLogger("PayloadLog");
    public static Map<String,String> regExpressions = new HashMap<>();
    public static Marker confidentialMarker = null;
    private static Marker marker = null;

    static {
        //read properties from log4j.xml
        // there has to be a better way w/o knowing prop KEY?VALUE
        final LoggerContext loggerContext = (LoggerContext) LogManager.getContext(true);
        final Configuration config = loggerContext.getConfiguration();
        final StrSubstitutor strSubstitutor = config.getStrSubstitutor();
        final StrLookup variableResolver = strSubstitutor.getVariableResolver();

        final String ccRegEx = variableResolver.lookup("CREDIT_CARD_REGEX");
        final String ccMask = variableResolver.lookup("CREDIT_CARD_MASK");
        regExpressions. put(ccRegEx,ccMask);

        final String sinRegEx = variableResolver.lookup("SIN_REGEX");
        final String sinMask = variableResolver.lookup("SIN_MASK");
        regExpressions. put(sinRegEx,sinMask);

        marker = MarkerManager.getMarker("MASKED");
    }

    int Id = -1;

    @MaskIt("phone")
    String firstName ;

    @MaskIt("name")
    String lastName ;

    public static void main(String[] args) {

        Main main = new Main();
        main.Id = -77;
        main.firstName = "John";
        main.lastName = "Smith";

        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        Instant start = Instant.now();
        for (int counter = 1; counter > 0; counter--) {
            // add some randomness
            String reandomString = "";
            for (int i = 1; i> 0; i--) {
                reandomString = reandomString.concat(" ") + random.ints(leftLimit, rightLimit + 1).limit(10).collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            }
            reandomString = reandomString.concat("Credit Card 1:1000002367844224,3425,Credit Card2:12345 , CVV:234, SIN:555-456-789");
            logger.info(reandomString);
            logger.info(/*marker,*/"Line to be masked.");
            loggerPayload.info(reandomString);
        }
        Instant finish = Instant.now();
        long timeElapsed = Duration.between(start, finish).toMillis();

        System.out.println( timeElapsed );

    }
}
