package com.utreg.service;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "grades_populated")
public class ModelEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
     private int a;

     private int b;
     private int c;

    public ModelEntity() {
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getC() {
        return c;
    }

//    public String getName() {
//        return name;
//    }
//
//    public String getValue() {
//        return value;
//    }
}