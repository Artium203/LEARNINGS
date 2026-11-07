package org.example;

import javax.sound.midi.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.*;

public class MidiToRobloxSheet {

    static Map<Integer, String> noteMap = new HashMap<>();

    public static void main(String[] args) throws Exception {

        initMap();

        Sequence sequence = MidiSystem.getSequence(new File("C:\\Users\\artem\\IdeaProjects\\untitled8\\src\\main\\java\\org\\example\\PVZ 2 - Pianist Zombie.mid"));

        Map<Long, List<String>> notesByTime = new TreeMap<>();

        for (Track track : sequence.getTracks()) {
            for (int i = 0; i < track.size(); i++) {
                MidiEvent event = track.get(i);
                MidiMessage message = event.getMessage();

                if (message instanceof ShortMessage sm) {
                    if (sm.getCommand() == ShortMessage.NOTE_ON && sm.getData2() > 0) {

                        int pitch = sm.getData1();

                        // 🔥 Auto-fit into playable range
                        while (pitch < 48) pitch += 12;
                        while (pitch > 83) pitch -= 12;

                        String key = noteMap.get(pitch);
                        if (key == null) continue;

                        notesByTime
                                .computeIfAbsent(event.getTick(), k -> new ArrayList<>())
                                .add(key);
                    }
                }
            }
        }

        StringBuilder sheet = new StringBuilder();

        long lastTick = -1;

        for (Map.Entry<Long, List<String>> entry : notesByTime.entrySet()) {

            long tick = entry.getKey();
            List<String> notes = entry.getValue();

            // ⏱️ Add spacing based on time gap
            if (lastTick != -1) {
                long gap = tick - lastTick;

                if (gap > 200) sheet.append("   "); // big pause
                else if (gap > 80) sheet.append("  "); // small pause
                else sheet.append(" ");
            }

            lastTick = tick;

            // 🎹 Remove duplicates
            Set<String> unique = new LinkedHashSet<>(notes);

            if (unique.size() == 1) {
                sheet.append(unique.iterator().next());
            } else {
                sheet.append("[");
                unique.forEach(sheet::append);
                sheet.append("]");
            }
        }

        // 💾 Save to file
        try (PrintWriter out = new PrintWriter("C:\\Users\\artem\\IdeaProjects\\untitled8\\src\\main\\java\\org\\example\\sheet444.txt")) {
            out.println(sheet.toString());
        }

        System.out.println("✅ Sheet generated: sheet.txt");
    }

    static void initMap() {

        // Lower octave
        noteMap.put(48, "z");
        noteMap.put(50, "x");
        noteMap.put(52, "c");
        noteMap.put(53, "v");
        noteMap.put(55, "b");
        noteMap.put(57, "n");
        noteMap.put(59, "m");

        // Middle octave
        noteMap.put(60, "q");
        noteMap.put(62, "w");
        noteMap.put(64, "e");
        noteMap.put(65, "r");
        noteMap.put(67, "t");
        noteMap.put(69, "y");
        noteMap.put(71, "u");

        // Upper octave
        noteMap.put(72, "1");
        noteMap.put(74, "2");
        noteMap.put(76, "3");
        noteMap.put(77, "4");
        noteMap.put(79, "5");
        noteMap.put(81, "6");
        noteMap.put(83, "7");

        // Sharps
        noteMap.put(61, "s");
        noteMap.put(63, "d");
        noteMap.put(66, "g");
        noteMap.put(68, "h");
        noteMap.put(70, "j");
    }
}