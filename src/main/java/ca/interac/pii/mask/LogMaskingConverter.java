package ca.interac.pii.mask;

import ca.interac.pii.demo.Main;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(name="LogMaskingConverter", category = "Converter")
@ConverterKeys("pii")
public class LogMaskingConverter extends LogEventPatternConverter {

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
        Pattern patern = null;
        StringBuffer buffer = new StringBuffer();

        // TODO: map of regular expressions is Main class static field. This has to change to some marker class to be part of a package coming with this util
        Set<String> set = Main.regExpressions.keySet();
        Iterator<String> it = set.iterator();
        while (it.hasNext()) {
            String regEx = it.next();
            patern = Pattern.compile(regEx);
            matcher = patern.matcher(message);
            String mask = Main.regExpressions.get(regEx);
            buffer.setLength(0);
            maskMatcher(matcher, buffer,mask);
            message=buffer.toString();
        }

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
