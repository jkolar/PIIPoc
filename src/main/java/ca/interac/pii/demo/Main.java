package ca.interac.pii.demo;

import ca.interac.pii.annotations.ContainsPIIFields;
import ca.interac.pii.annotations.MaskIt;
import ca.interac.pii.wrapperlogger.InteracLog4jWrapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ContainsPIIFields
public class Main {

    private static final Logger logger = LogManager.getLogger();

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

        InteracLog4jWrapper.info(main,"First Name is " + main.firstName);
        InteracLog4jWrapper.info(main,"Last Name is " + main.lastName);

        logger.info("logger test baseline message");
        logger.info("Credit Card 1:1000002367844224,3425,Credit Card2:12345 , CVV:234, SIN:123456789");

    }
}
