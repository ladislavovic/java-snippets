/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java._14_annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 *
 * @author kulhalad
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationWithData {
    int value() default 10;
    String strParam() default "";
    Class clazzParam() default Object.class;
    Day enumParam() default Day.MO;
    Day[] arrParam() default {Day.MO, Day.TU, Day.WE, Day.TH, Day.FR};
}
