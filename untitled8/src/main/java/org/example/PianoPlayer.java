package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;



public class PianoPlayer implements NativeKeyListener {

    static Robot robot;
    static AtomicBoolean playing = new AtomicBoolean(false);
    static String sheet;

    public static void main(String[] args) throws Exception {
        robot = new Robot();

        // Load sheet file
        sheet = Files.readString(Path.of("C:\\Users\\artem\\IdeaProjects\\untitled8\\src\\main\\java\\org\\example\\sheet.txt"));

        // Disable annoying logs
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);
        logger.setUseParentHandlers(false);

        // Register global key listener
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new PianoPlayer());

        System.out.println("F6 = START | F7 = STOP");
    }

    // 🔥 HOTKEYS
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
            if (!playing.get()) {
                playing.set(true);
                new Thread(() -> {
                    try {
                        playLoop();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }).start();
                System.out.println("Started");
            }
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_F7) {
            playing.set(false);
            System.out.println("Stopped");
        }
    }

    static void playLoop() throws Exception {
        while (playing.get()) {
            playSheet(sheet);
        }
    }

    static void playSheet(String sheet) throws Exception {
        String[] tokens = sheet.split("\\s+");

        for (String token : tokens) {
            if (!playing.get()) break;
            if (token.isEmpty()) continue;

            if (token.startsWith("[") && token.endsWith("]")) {
                pressChord(token.substring(1, token.length() - 1));
            } else {
                pressKey(token);
            }

            Thread.sleep(350); // 🎵 speed control
        }
    }

    static void pressChord(String keys) throws Exception {
        boolean shiftNeeded = false;

        for (char c : keys.toCharArray()) {
            if (needsShift(c)) {
                shiftNeeded = true;
                break;
            }
        }

        if (shiftNeeded) robot.keyPress(KeyEvent.VK_SHIFT);

        for (char c : keys.toCharArray()) {
            robot.keyPress(getKeyCode(c));
        }

        Thread.sleep(80);

        for (char c : keys.toCharArray()) {
            robot.keyRelease(getKeyCode(c));
        }

        if (shiftNeeded) robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    static void pressKey(String key) throws Exception {
        char c = key.charAt(0);

        boolean shift = needsShift(c);
        int code = getKeyCode(c);

        if (shift) robot.keyPress(KeyEvent.VK_SHIFT);

        robot.keyPress(code);
        Thread.sleep(60);
        robot.keyRelease(code);

        if (shift) robot.keyRelease(KeyEvent.VK_SHIFT);
    }

    static boolean needsShift(char c) {
        return Character.isUpperCase(c) || c == '$' || c == '(';
    }

    static int getKeyCode(char c) {
        switch (Character.toLowerCase(c)) {

            case 'a': return KeyEvent.VK_A;
            case 's': return KeyEvent.VK_S;
            case 'd': return KeyEvent.VK_D;
            case 'f': return KeyEvent.VK_F;
            case 'g': return KeyEvent.VK_G;
            case 'h': return KeyEvent.VK_H;
            case 'j': return KeyEvent.VK_J;
            case 'k': return KeyEvent.VK_K;
            case 'l': return KeyEvent.VK_L;
            case 'z': return KeyEvent.VK_Z;
            case 'x': return KeyEvent.VK_X;
            case '%': return KeyEvent.VK_5;
            case '@': return KeyEvent.VK_2;
            case 'v': return KeyEvent.VK_V;
            case 'c': return KeyEvent.VK_C;
            case '^': return KeyEvent.VK_6;

            case 'q': return KeyEvent.VK_Q;
            case 'w': return KeyEvent.VK_W;
            case 'e': return KeyEvent.VK_E;
            case 'r': return KeyEvent.VK_R;
            case 't': return KeyEvent.VK_T;
            case 'y': return KeyEvent.VK_Y;
            case 'u': return KeyEvent.VK_U;
            case 'i': return KeyEvent.VK_I;
            case 'o': return KeyEvent.VK_O;
            case 'p': return KeyEvent.VK_P;
            case 'm': return KeyEvent.VK_M;

            case '0': return KeyEvent.VK_0;
            case '1': return KeyEvent.VK_1;
            case '2': return KeyEvent.VK_2;
            case '3': return KeyEvent.VK_3;
            case '4': return KeyEvent.VK_4;
            case '5': return KeyEvent.VK_5;
            case '6': return KeyEvent.VK_6;
            case '7': return KeyEvent.VK_7;
            case '8': return KeyEvent.VK_8;
            case '9': return KeyEvent.VK_9;

            case '$': return KeyEvent.VK_4; // Shift+4
            case '(': return KeyEvent.VK_9; // Shift+9

            default:
                throw new IllegalArgumentException("Unknown key: " + c);
        }
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}