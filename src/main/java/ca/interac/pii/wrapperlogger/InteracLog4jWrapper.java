package ca.interac.pii.wrapperlogger;

import ca.interac.pii.annotations.ContainsPIIFields;
import ca.interac.pii.annotations.MaskIt;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

public class InteracLog4jWrapper {


    private static boolean checkIfTypeIsAnnotated(Object object) {
        if (object==null) return false;
        if (object.getClass().isAnnotationPresent(ContainsPIIFields.class)) return true;
        return false;
    }

    public static void info(Object object, String message) {

        // is the class annotated
        if (!checkIfTypeIsAnnotated(object)) return;

        Class clazz = object.getClass();
        Annotation an = clazz.getAnnotation(ContainsPIIFields.class);   // ContainsPIIFields
        if (an == null) return;

        //System.out.println("object ".concat(object.getClass().getSimpleName()).concat(" is annotated with ").concat(an.getClass().getSimpleName()));

        Field[] fields = clazz.getDeclaredFields();
        String str = "";
        for (Field field : fields) {
            MaskIt[] anns = field.getAnnotationsByType(MaskIt.class);
            if (anns.length > 0) {
                MaskIt maskIt = anns[0];
                String annValue = maskIt.value();
                field.setAccessible(true);
                try {
                    str = (String) field.get(object);
                    if (annValue.equals("name"))  {
                        message = message.replace(str, "PII-NAME " + str + " NAME-PII");
                    }
                    if (annValue.equals("phone"))  {
                        message = message.replace(str, "PII-PHONE " + str + " PHONE-PII");
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                //System.out.println(field.getName() + " value = " + "XXX-" + str + "-XXX");
            }
        }
        //Stream<Field> stream = Arrays.asList(fields).stream();
        //stream.forEach(System.out::println);

        System.out.println(message);

    }
}
