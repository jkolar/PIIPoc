package ca.interac.pii.demo;

import ca.interac.pii.annotations.ContainsPIIFields;
import ca.interac.pii.annotations.MaskIt;
import ca.interac.pii.wrapperlogger.InteracLog4jWrapper;

@ContainsPIIFields
public class Main {

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

    }
}
