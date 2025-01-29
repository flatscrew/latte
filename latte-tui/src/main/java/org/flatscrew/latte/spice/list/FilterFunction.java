package org.flatscrew.latte.spice.list;

@FunctionalInterface
public interface FilterFunction {

    Rank[] apply(String term, String[] targets);
}
