package org.flatscrew.latte.input;

import org.flatscrew.latte.Message;
import org.flatscrew.latte.message.BlurMessage;
import org.flatscrew.latte.message.FocusMessage;
import org.flatscrew.latte.message.KeyPressMessage;
import org.flatscrew.latte.message.UnknownSequenceMessage;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InputHandlerTest {

    @Mock
    private Terminal terminal;

    @Mock
    private NonBlockingReader reader;

    @Test
    void test_ShouldPublishKeyPressMessage_WhenPressingRegularKey() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);
        Consumer<Message> messageConsumer = e -> {
            receivedMessages.add(e);
            messageLatch.countDown();
        };
        when(reader.read()).thenReturn((int) 'a', -1);

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(KeyPressMessage.class);

        KeyPressMessage keyMessage = (KeyPressMessage) receivedMessages.getFirst();
        assertThat(keyMessage.key()).isEqualTo('a');
        assertThat(keyMessage.alt()).isFalse();
    }

    @Test
    void test_ShouldRecognizeEscapeKey() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        // Setup reader to return ESC and then wait
        when(reader.read()).thenReturn((int) '\u001b', -1);  // return ESC first, then -1
        when(reader.read(50)).thenReturn(-1);

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(KeyPressMessage.class);

        KeyPressMessage keyMessage = (KeyPressMessage) receivedMessages.getFirst();
        assertThat(keyMessage.key()).isEqualTo('\u001b');
        assertThat(keyMessage.alt()).isFalse();
    }

    @Test
    void test_ShouldRecognizeAltKey() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        // Setup reader to return ESC and then wait
        when(reader.read()).thenReturn((int) '\u001b', -1);  // return ESC first, then -1
        when(reader.read(50)).thenReturn((int) 'a', -1);

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();

        // Wait for message or timeout
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(KeyPressMessage.class);

        KeyPressMessage keyMessage = (KeyPressMessage) receivedMessages.getFirst();
        assertThat(keyMessage.key()).isEqualTo('a');
        assertThat(keyMessage.alt()).isTrue();
    }

    @Test
    void test_ShouldPublishFocusMessage_When_FocusGained() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        // Setup reader to return ESC and then wait
        when(reader.read()).thenReturn((int) '\u001b', -1);  // return ESC first, then -1
        when(reader.read(50)).thenReturn((int) '[', (int) 'I', -1);

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();

        // Wait for message or timeout
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(FocusMessage.class);
    }

    @Test
    void test_ShouldPublishBlurMessage_When_FocusLost() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        // Setup reader to return ESC and then wait
        when(reader.read()).thenReturn((int) '\u001b', -1);  // return ESC first, then -1
        when(reader.read(50)).thenReturn((int) '[', (int) 'O', -1);

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();

        // Wait for message or timeout
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(BlurMessage.class);
    }

    @Test
    void test_ShouldPublishMouseMessage_When_ReceivingX10MouseEvent() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        // Simulating a left click at position (10, 20) with Alt modifier
        // Binary: 0000_1000 (Alt bit set) = 8
        int button = 32 + 8; // Adding X10_MOUSE_BYTE_OFFSET (32) + Alt modifier (8)
        int col = 32 + 10;  // X coordinate 10
        int row = 32 + 20;  // Y coordinate 20

        when(reader.read()).thenReturn(
                (int) '\u001b',
                button,
                col,
                row,
                -1
        );

        when(reader.read(50)).thenReturn(
                (int) '[',
                (int) 'M',
                -1
        );

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(MouseMessage.class);

        MouseMessage mouseMessage = (MouseMessage) receivedMessages.getFirst();
        assertThat(mouseMessage.column()).isEqualTo(9);  // 10 - 1 for zero-based
        assertThat(mouseMessage.row()).isEqualTo(19);    // 20 - 1 for zero-based
        assertThat(mouseMessage.isAlt()).isTrue();
        assertThat(mouseMessage.isCtrl()).isFalse();
        assertThat(mouseMessage.isShift()).isFalse();
        assertThat(mouseMessage.getButton()).isEqualTo(MouseButton.MouseButtonLeft);
        assertThat(mouseMessage.getAction()).isEqualTo(MouseAction.MouseActionPress);
        assertThat(mouseMessage.isWheel()).isFalse();
    }

    @Test
    void test_ShouldPublishMouseMessage_When_ReceivingSGRMousePress() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        when(reader.read()).thenReturn(
                (int)'\u001b',    // ESC
                -1          // End of input
        );

        // Simulating a left button press at position (10, 20)
        // button = 0 represents left button with no modifiers in SGR mode
        when(reader.read(50)).thenReturn(
                (int)'[',    // First char after ESC
                (int)'<',    // SGR mouse indicator
                (int)'0',    // button (left click, no modifiers)
                (int)';',    // separator
                (int)'1', (int)'0',  // x = 10
                (int)';',    // separator
                (int)'2', (int)'0',  // y = 20
                (int)'M',    // Press indicator ('M' for press, 'm' would be release)
                -1
        );

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(MouseMessage.class);

        MouseMessage mouseMessage = (MouseMessage) receivedMessages.getFirst();
        assertThat(mouseMessage.column()).isEqualTo(9);  // 10 - 1 for zero-based
        assertThat(mouseMessage.row()).isEqualTo(19);    // 20 - 1 for zero-based
        assertThat(mouseMessage.isAlt()).isFalse();
        assertThat(mouseMessage.isCtrl()).isFalse();
        assertThat(mouseMessage.isShift()).isFalse();
        assertThat(mouseMessage.getButton()).isEqualTo(MouseButton.MouseButtonLeft);
        assertThat(mouseMessage.getAction()).isEqualTo(MouseAction.MouseActionPress);
        assertThat(mouseMessage.isWheel()).isFalse();

        assertThat(mouseMessage.toString())
                .contains("x=9")
                .contains("y=19")
                .contains("shift=false")
                .contains("alt=false")
                .contains("ctrl=false")
                .contains("action=press")
                .contains("button=left");
    }

    @Test
    void test_ShouldPublishMouseMessage_When_ReceivingSGRMouseRelease() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        when(reader.read()).thenReturn(
                (int) '\u001b',    // ESC
                -1          // End of input
        );

        // Simulating a button release at position (10, 20)
        // button = 0 represents left button with no modifiers in SGR mode
        when(reader.read(50)).thenReturn(
                (int)'[',    // First char after ESC
                (int)'<',    // SGR mouse indicator
                (int)'0',    // button (left button, no modifiers)
                (int)';',    // separator
                (int)'1', (int)'0',  // x = 10
                (int)';',    // separator
                (int)'2', (int)'0',  // y = 20
                (int)'m',    // Release indicator (lowercase 'm' for release)
                -1
        );

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(MouseMessage.class);

        MouseMessage mouseMessage = (MouseMessage) receivedMessages.getFirst();
        assertThat(mouseMessage.column()).isEqualTo(9);  // 10 - 1 for zero-based
        assertThat(mouseMessage.row()).isEqualTo(19);    // 20 - 1 for zero-based
        assertThat(mouseMessage.isAlt()).isFalse();
        assertThat(mouseMessage.isCtrl()).isFalse();
        assertThat(mouseMessage.isShift()).isFalse();
        assertThat(mouseMessage.getButton()).isEqualTo(MouseButton.MouseButtonNone);  // None on release
        assertThat(mouseMessage.getAction()).isEqualTo(MouseAction.MouseActionRelease);
        assertThat(mouseMessage.isWheel()).isFalse();

        assertThat(mouseMessage.toString()).contains("x=9")
                .contains("y=19")
                .contains("shift=false")
                .contains("alt=false")
                .contains("ctrl=false")
                .contains("action=release")
                .contains("button=none");  // None on release
    }

    @Test
    void test_ShouldPublishUnknownSequenceMessage_When_ReceivingUnknownEscapeSequence() throws Throwable {
        // given
        when(terminal.reader()).thenReturn(reader);
        List<Message> receivedMessages = new ArrayList<>();
        CountDownLatch messageLatch = new CountDownLatch(1);

        Consumer<Message> messageConsumer = message -> {
            receivedMessages.add(message);
            messageLatch.countDown();
        };

        when(reader.read()).thenReturn(
                (int) '\u001b',    // ESC
                -1          // End of input
        );

        when(reader.read(50)).thenReturn(
                (int)'[',    // First char after ESC
                (int)'?',    // Unknown sequence
                (int)'1',
                -1
        );

        InputHandler inputHandler = new InputHandler(terminal, messageConsumer);

        // when
        inputHandler.start();
        boolean received = messageLatch.await(1, TimeUnit.SECONDS);
        inputHandler.stop();

        // then
        assertThat(received).isTrue();
        assertThat(receivedMessages).hasSize(1);
        assertThat(receivedMessages.getFirst()).isInstanceOf(UnknownSequenceMessage.class);
    }
}