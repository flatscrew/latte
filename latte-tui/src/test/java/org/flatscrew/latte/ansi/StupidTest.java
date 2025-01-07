package org.flatscrew.latte.ansi;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class StupidTest {

    @Test
    void Test() {

//        String string = new String(bytes);
//
        String string = "👨‍👩‍👦\u009b38;5;1mhello\u009bm";
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        for (int i = 0; i < bytes.length; i++) {
            byte byteValue = bytes[i];

            int signed = byteValue & 0xFF;

            System.out.println(signed);
        }

    }
}
