package org.flatscrew.latte.cream.tree;

public class Filter implements Children {

    public interface FilterFunc {
        boolean filter(int index);
    }

    private final Children data;
    private FilterFunc filterFunc = index -> true;

    public Filter(Children data) {
        this.data = data;
    }

    @Override
    public Node at(int index) {
        int j = 0;
        for (int i = 0; i < data.length(); i++) {
            if (filterFunc.filter(i)) {
                if (j == index) {
                    return data.at(i);
                }
                j++;
            }
        }

        return null;
    }

    public Filter filter(FilterFunc filterFunc) {
        this.filterFunc = filterFunc;
        return this;
    }

    @Override
    public Children remove(int index) {
        return null;
    }

    @Override
    public Children append(Node child) {
        return null;
    }

    @Override
    public int length() {
        int j = 0;
        for (int i = 0; i < data.length(); i++) {
            if (filterFunc.filter(i)) {
                j++;
            }
        }
        return j;
    }
}
