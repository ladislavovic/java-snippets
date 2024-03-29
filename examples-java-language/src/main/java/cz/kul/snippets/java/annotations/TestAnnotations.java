/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.kul.snippets.java.annotations;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class TestAnnotations {

    @Test
    public void annotationDetectionOnClass() {
        Annotation[] annotations = FooSimpleAnnotation.class.getAnnotationsByType(AnnotationSimple.class);
        Assert.assertEquals(1, annotations.length);
    }

    @Test
    public void annotationDetectionOnMethods() {
        Method[] declaredMethods = FooSimpleAnnotation.class.getDeclaredMethods();
        ArrayList<Method> annotatedMethods = new ArrayList<Method>();
        for (Method declaredMethod : declaredMethods) {
            if (declaredMethod.getAnnotationsByType(AnnotationSimple.class).length > 0) {
                annotatedMethods.add(declaredMethod);
            }
        }
        Assert.assertEquals(1, annotatedMethods.size());
        Assert.assertEquals("bar", annotatedMethods.get(0).getName());
    }

    @Test
    public void annotationData() {
        Class<FooDataAnnotation> clazz = FooDataAnnotation.class;
        AnnotationWithData annotation = clazz.getAnnotation(AnnotationWithData.class);
        Assert.assertEquals(20, annotation.value());
        Assert.assertEquals("foo", annotation.strParam());
        Assert.assertEquals(Day.MO, annotation.enumParam()); // even though it was not defined. It is default val.
        Assert.assertArrayEquals(new Day [] {Day.WE}, annotation.arrParam());
    }
    
}
