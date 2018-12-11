package edu.ksu.rxecs.core;

public class Profiling {

    public static int getAvailableProcessors() {
        return Runtime.getRuntime().availableProcessors();
    }

    private Profiling() { }

}
