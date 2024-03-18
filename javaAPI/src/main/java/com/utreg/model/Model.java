    package com.utreg.model;

    public class Model {
        private String name;
        private String value;

        public Model() {

        }

        public Model(String name, String value) {
            this.name = name;
            this.value = value;
        }

        // getters and setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    '}';
        }
    }