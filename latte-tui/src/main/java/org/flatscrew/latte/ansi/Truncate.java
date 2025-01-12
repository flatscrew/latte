package org.flatscrew.latte.ansi;

import java.nio.charset.StandardCharsets;

public class Truncate {

    public static String truncate(String input, int length, String tail) {
        int len = length;
        if (TextWidth.measureCellWidth(input) <= len) {
            return input;
        }

        int tw = TextWidth.measureCellWidth(tail);
        len -= tw;
        if (len < 0) {
            return "";
        }

        int curWidth = 0;
        boolean ignoring = false;
        TransitionTable table = TransitionTable.get();
        State pstate = State.GROUND;
        byte[] b = input.getBytes(StandardCharsets.UTF_8);

        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < b.length; ) {
            byte byteValue = b[i];

            TransitionTable.Transition transition = table.transition(pstate, byteValue);
            State state = transition.state();
            Action action = transition.action();

            if (state == State.UTF8) {
                GraphemeCluster.GraphemeResult graphemeResult = GraphemeCluster.getFirstGraphemeCluster(b, i, -1);
                byte[] cluster = graphemeResult.cluster();
                int width = graphemeResult.width();

                i += cluster.length;

                if (ignoring) {
                    continue;
                }

                if (curWidth + width > length && !ignoring) {
                    ignoring = true;
                    buf.append(tail);
                    continue;
                }

                if (curWidth + width > length) {
                    continue;
                }

                curWidth += width;
                buf.append(new String(cluster, StandardCharsets.UTF_8));
                pstate = State.GROUND;
                continue;
            }

            if (action == Action.PRINT) {
                if (curWidth >= length && !ignoring) {
                    ignoring = true;
                    buf.append(tail);
                    continue;
                }

                if (ignoring) {
                    i++;
                    continue;
                }

                curWidth++;
            }

            // Convert single byte to char before appending
            buf.append((char) b[i]);
            i++;

            pstate = state;

            if (curWidth > length && !ignoring) {
                ignoring = true;
                buf.append(tail);
            }
        }
        return buf.toString();
    }
}
