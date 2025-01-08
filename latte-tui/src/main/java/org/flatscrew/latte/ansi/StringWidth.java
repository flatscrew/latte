package org.flatscrew.latte.ansi;

import java.nio.charset.StandardCharsets;

public class StringWidth {

    public static int measureWidth(String input) {
        if (input.isEmpty()) {
            return 0;
        }

        int width = 0;
        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;

        byte[] b = input.getBytes(StandardCharsets.UTF_8);

        for (int i = 0; i < b.length; i++) {
            byte byteValue = b[i];

            TransitionTable.Transition transition = table.transition(pstate, byteValue);
            State state = transition.state();
            Action action = transition.action();

            // Handle UTF-8 grapheme clusters
            if (state == State.UTF8) {
                GraphemeCluster.GraphemeResult graphemeResult = GraphemeCluster.getFirstGraphemeCluster(b, i, -1);
                byte[] cluster = graphemeResult.cluster();
                int w = graphemeResult.width();

                width += w;

                i += cluster.length - 1; // Skip processed bytes
                pstate = State.GROUND;
                continue;
            }

            // Handle printable characters
            if (action == Action.PRINT) {
                width++;
            }

            pstate = state;
        }

        return width;
    }
}