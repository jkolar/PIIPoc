package ca.interac.pii.mask;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name="LogMaskingConverter", category = "Converter")
@ConverterKeys("pii")
public class LogMaskingConverter extends LogEventPatternConverter {

    private static final String CREDIT_CARD_REGEX = "([0-9]{16})";
    private static final Pattern CREDIT_CARD_PATTERN = Pattern.compile(CREDIT_CARD_REGEX);
    private static final String CAREDIT_CARD_REPLACEMENT_REGEX = "XXXXXXXXXXXXXXXX";

    private static final String SIN_REGEX = "([0-9]{9})";
    private static final Pattern SIN_PATTERN = Pattern.compile(SIN_REGEX);
    private static final String SIN_REPLACEMENT_REGEX = "*********";

    protected LogMaskingConverter(String name, String style) {
        super(name, style);
    }
    public static LogMaskingConverter newInstance(String[] options) {
        return new LogMaskingConverter("pii",Thread.currentThread().getName());
    }

    @Override
    public void format(LogEvent logEvent, StringBuilder output) {
        String message = logEvent.getMessage().getFormattedMessage();
        String maskedMessage = message;
        maskedMessage = mask(message);
        output.append(maskedMessage);
    }

    private String mask(String message) {
        Matcher matcher =null;
        StringBuffer buffer = new StringBuffer();

        matcher = CREDIT_CARD_PATTERN.matcher(message);
        maskMatcher(matcher, buffer,CAREDIT_CARD_REPLACEMENT_REGEX);
        message=buffer.toString();
        buffer.setLength(0);

        matcher = SIN_PATTERN.matcher(message);
        maskMatcher(matcher, buffer,SIN_REPLACEMENT_REGEX);

        return buffer.toString();
    }

    private StringBuffer maskMatcher(Matcher matcher, StringBuffer buffer, String regEx) {
        while (matcher.find()) {
            matcher.appendReplacement(buffer,regEx);
        }
        matcher.appendTail(buffer);
        return buffer;
    }
}
