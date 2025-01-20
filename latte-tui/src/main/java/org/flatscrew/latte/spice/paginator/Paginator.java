package org.flatscrew.latte.spice.paginator;

import org.flatscrew.latte.Message;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.spice.key.Binding;

public class Paginator {

    public interface Option {

        static Option withTotalPages(int totalPages) {
            return paginator -> paginator.totalPages = totalPages;
        }

        static Option withPerPage(int perPage) {
            return paginator -> paginator.perPage = perPage;
        }

        void apply(Paginator paginator);
    }

    private Type type;
    private int page;
    private int perPage;
    private int totalPages;
    private String activeDot;
    private String inactiveDot;
    private String arabicFormat;

    private KeyMap keyMap;

    public Paginator(Option... options) {
        this.type = Type.Arabic;
        this.page = 0;
        this.perPage = 1;
        this.totalPages = 1;
        this.keyMap = KeyMap.defaultKeyMap();
        this.activeDot = "•";
        this.inactiveDot = "○";
        this.arabicFormat = "%d/%d";

        for (Option option : options) {
            option.apply(this);
        }
    }

    public int itemsOnPage(int totalItems) {
        if (totalItems < 1) {
            return 0;
        }
        Bounds sliceBounds = getSliceBounds(totalItems);
        return sliceBounds.end() - sliceBounds.start();
    }

    public Bounds getSliceBounds(int length) {
        int start = page * perPage;
        int end = Math.min(page * perPage + perPage, length);
        return new Bounds(start, end);
    }

    public void prevPage() {
        if (page > 0) {
            page--;
        }
    }

    public void nextPage() {
        if (!onLastPage()) {
            page++;
        }
    }

    public boolean onLastPage() {
        return page == totalPages - 1;
    }

    public boolean onFirstPage() {
        return page == 0;
    }

    public Paginator update(Message msg) {
        if (msg instanceof KeyPressMessage keyPressMessage) {
            if (Binding.matches(keyPressMessage, keyMap.nextPage())) {
                nextPage();
            } else if (Binding.matches(keyPressMessage, keyMap.prevPage())) {
                prevPage();
            }
        }
        return this;
    }

    public String view() {
        if (type == Type.Dots) {
            return dotsView();
        }
        return arabicView();
    }

    private String dotsView() {
        StringBuilder view = new StringBuilder();
        for (int i = 0; i < totalPages; i++) {
            if (i == page) {
                view.append(activeDot);
                continue;
            }
            view.append(inactiveDot);
        }
        return view.toString();
    }

    private String arabicView() {
        return String.format(arabicFormat, page + 1, totalPages);
    }

    public int totalPages() {
        return totalPages;
    }

    public int perPage() {
        return perPage;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int page() {
        return page;
    }

    public int totalPages(int items) {
        if (items < 1) {
            return totalPages;
        }
        int n = items / perPage;

        if (items % perPage > 0) {
            n++;
        }
        this.totalPages = n;
        return n;
    }

    public void activeDot(String activeDot) {
        this.activeDot = activeDot;
    }

    public void inactiveDot(String inactiveDot) {
        this.inactiveDot = inactiveDot;
    }

    public void type(Type type) {
        this.type = type;
    }

    public void perPage(int perPage) {
        this.perPage = perPage;
    }
}
