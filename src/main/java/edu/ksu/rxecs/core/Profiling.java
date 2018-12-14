package edu.ksu.rxecs.core;

public class Profiling {

    private Profiling() {
    }

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

}
