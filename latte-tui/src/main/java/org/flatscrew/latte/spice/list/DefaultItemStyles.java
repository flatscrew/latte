package org.flatscrew.latte.spice.list;

import org.flatscrew.latte.cream.Style;
import org.flatscrew.latte.cream.border.StandardBorder;
import org.flatscrew.latte.cream.color.AdaptiveColor;

public class DefaultItemStyles {

    public static final String ELLIPSIS = "…";

    private Style normalTitle;
    private Style normalDesc;

    private Style selectedTitle;
    private Style selectedDesc;

    private Style dimmedTitle;
    private Style dimmedDesc;

    private Style filterMatch;

    public DefaultItemStyles() {
        this.normalTitle = Style.newStyle()
                .foreground(new AdaptiveColor("#1a1a1a", "#dddddd"))
                .padding(0, 0, 0, 2);
        this.normalDesc = normalTitle.copy()
                .foreground(new AdaptiveColor("#A49FA5", "#777777"));

        this.selectedTitle = Style.newStyle()
                .border(StandardBorder.NormalBorder, false, false, false, true)
                .borderForeground(new AdaptiveColor("#F793FF", "#AD58B4"))
                .foreground(new AdaptiveColor("#EE6FF8", "#EE6FF8"))
                .padding(0, 0, 0, 1);
        this.selectedDesc = selectedTitle.copy()
                .foreground(new AdaptiveColor("#F793FF", "#AD58B4"));

        this.dimmedTitle = Style.newStyle()
                .foreground(new AdaptiveColor("#A49FA5", "#777777"))
                .padding(0, 0, 0, 2);
        this.dimmedDesc = dimmedTitle.copy()
                .foreground(new AdaptiveColor("#C2B8C2", "#4D4D4D"));

        this.filterMatch = Style.newStyle().underline(true);
    }

    public Style normalTitle() {
        return normalTitle;
    }

    public Style normalDesc() {
        return normalDesc;
    }

    public Style selectedTitle() {
        return selectedTitle;
    }

    public Style selectedDesc() {
        return selectedDesc;
    }

    public Style dimmedTitle() {
        return dimmedTitle;
    }

    public Style dimmedDesc() {
        return dimmedDesc;
    }

    public Style filterMatch() {
        return filterMatch;
    }
}
