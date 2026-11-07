package org.example;

import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;

import javax.sound.midi.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;



public class MidiRobloxPlayer implements NativeKeyListener {

    static Robot robot;
    static AtomicBoolean playing = new AtomicBoolean(false);

    static Map<Integer, Integer> keyMap = new HashMap<>();
    static double speed = 1.0; // 🔥 change playback speed here

    static Sequence sequence;

    public static void main(String[] args) throws Exception {
        robot = new Robot();

        setupKeyMap();
        sequence = MidiSystem.getSequence(new File("src/main/java/org/example/midi/Sonic.exe.mid"));

        disableLogs();
        GlobalScreen.registerNativeHook();
        GlobalScreen.addNativeKeyListener(new MidiRobloxPlayer());

        System.out.println("F6 = START | F7 = STOP");
    }

    // 🎹 KEYBOARD MAPPING (your layout)
    static void setupKeyMap() {

        // Lower octave
        keyMap.put(48, KeyEvent.VK_1);
        keyMap.put(49, KeyEvent.VK_2);
        keyMap.put(50, KeyEvent.VK_2);
        keyMap.put(51, KeyEvent.VK_3);
        keyMap.put(52, KeyEvent.VK_3);
        keyMap.put(53, KeyEvent.VK_4);
        keyMap.put(54, KeyEvent.VK_5);
        keyMap.put(55, KeyEvent.VK_5);
        keyMap.put(56, KeyEvent.VK_6);
        keyMap.put(57, KeyEvent.VK_6);
        keyMap.put(58, KeyEvent.VK_7);
        keyMap.put(59, KeyEvent.VK_8);
        keyMap.put(60, KeyEvent.VK_9);
        keyMap.put(61, KeyEvent.VK_0);


        // Middle
        keyMap.put(62, KeyEvent.VK_Q);
        keyMap.put(63, KeyEvent.VK_W);
        keyMap.put(64, KeyEvent.VK_E);
        keyMap.put(65, KeyEvent.VK_R);
        keyMap.put(66, KeyEvent.VK_T);
        keyMap.put(67, KeyEvent.VK_Y);
        keyMap.put(68, KeyEvent.VK_U);
        keyMap.put(69, KeyEvent.VK_I);
        keyMap.put(70, KeyEvent.VK_O);
        keyMap.put(71, KeyEvent.VK_P);
        keyMap.put(72, KeyEvent.VK_A);
        keyMap.put(73, KeyEvent.VK_S);


        // Upper
        keyMap.put(74, KeyEvent.VK_D);
        keyMap.put(75, KeyEvent.VK_F);
        keyMap.put(77, KeyEvent.VK_G);
        keyMap.put(78, KeyEvent.VK_H);
        keyMap.put(79, KeyEvent.VK_J);
        keyMap.put(80, KeyEvent.VK_K);
        keyMap.put(81, KeyEvent.VK_L);
        keyMap.put(82, KeyEvent.VK_Z);
        keyMap.put(83, KeyEvent.VK_X);
        keyMap.put(84, KeyEvent.VK_C);
        keyMap.put(85, KeyEvent.VK_V);
        keyMap.put(86, KeyEvent.VK_B);
        keyMap.put(87, KeyEvent.VK_N);
        keyMap.put(88, KeyEvent.VK_M);
    }

    // 🔥 HOTKEYS
    @Override
    public void nativeKeyPressed(NativeKeyEvent e) {
        if (e.getKeyCode() == NativeKeyEvent.VC_F6) {
            if (!playing.get()) {
                playing.set(true);
                new Thread(this::play).start();
                System.out.println("Started");
            }
        }

        if (e.getKeyCode() == NativeKeyEvent.VC_F7) {
            playing.set(false);
            releaseAllKeys();
            System.out.println("Stopped");
        }
    }

    void play() {
        try {
            List<MidiEvent> events = new ArrayList<>();

            for (Track track : sequence.getTracks()) {
                for (int i = 0; i < track.size(); i++) {
                    events.add(track.get(i));
                }
            }

            events.sort(Comparator.comparingLong(MidiEvent::getTick));

            long lastTick = 0;
            double tempo = 500000;
            int resolution = sequence.getResolution();

            for (MidiEvent event : events) {
                if (!playing.get()) break;

                long tick = event.getTick();
                long delta = tick - lastTick;

                long sleep = (long)((delta * tempo) / resolution / 1000 / speed);
                Thread.sleep(Math.max(0, sleep));

                MidiMessage msg = event.getMessage();

                if (msg instanceof ShortMessage sm) {
                    handleNote(sm);
                }

                if (msg instanceof MetaMessage mm && mm.getType() == 0x51) {
                    byte[] d = mm.getData();
                    tempo = ((d[0] & 0xff) << 16) |
                            ((d[1] & 0xff) << 8) |
                            (d[2] & 0xff);
                }

                lastTick = tick;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void handleNote(ShortMessage sm) {
        int pitch = sm.getData1();
        int velocity = sm.getData2();

        // auto-fit to keyboard range
        while (pitch < 48) pitch += 12;
        while (pitch > 83) pitch -= 12;

        Integer key = keyMap.get(pitch);
        if (key == null) return;

        if (sm.getCommand() == ShortMessage.NOTE_ON && velocity > 0) {
            robot.keyPress(key);
        } else {
            robot.keyRelease(key);
        }
    }

    void releaseAllKeys() {
        for (int key : keyMap.values()) {
            robot.keyRelease(key);
        }
    }

    static void disableLogs() {
        java.util.logging.Logger logger =
                java.util.logging.Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(java.util.logging.Level.OFF);
        logger.setUseParentHandlers(false);
    }

    @Override public void nativeKeyReleased(NativeKeyEvent e) {}
    @Override public void nativeKeyTyped(NativeKeyEvent e) {}
}