package com.selenideintro.common.selectors;

public class XPath {

    public static class Predicate {

        public static String cssClass(String value) {
            return "contains(concat(' ', normalize-space(@class), ' '), ' " + value + " ')";
        }

        public static String not(String predicate) {
            return "not(" + predicate + ")";
        }
    }

        public static String filteredBy(String predicate) {
            return "[" + predicate + "]";
        }

        public static class filterBy{

            public static String cssClass(String value){
                return XPath.filteredBy(XPath.Predicate.cssClass(value));
            }
            public static String  noCssClass(String value){
                return XPath.filteredBy(XPath.Predicate.not(XPath.Predicate.cssClass(value)));
            }
        }
    }

