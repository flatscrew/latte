package org.flatscrew.latte.spice.list;

public enum FilterState {
    Unfiltered("unfiltered"), // no filter set
    Filtering("filtering"), // user is actively setting a filter
    FilterApplied("filter applied");

    private final String stateName;

    FilterState(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return stateName;
    }
}
