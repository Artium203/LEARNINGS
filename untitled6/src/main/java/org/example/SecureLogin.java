package org.example;

import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

public class SecureLogin {

    private static final Map<String, String> users = new HashMap<>();
    private static final Map<String, String> oneTimeTokens = new HashMap<>();

    public static void main(String[] args) throws Exception {
        users.put("Art", "pass123");
        users.put("Ben", "1234");

        Scanner scanner = new Scanner(System.in);

        System.out.print("Username: ");
        String username = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        if (!users.containsKey(username) || !users.get(username).equals(password)) {
            System.out.println("Invalid username or password");
            return;
        }

        String token = UUID.randomUUID().toString();
        oneTimeTokens.put(username, token);
        generateQR(token, "src/main/java/org/example/"+username + "_QR.png");

        System.out.println("QR one time" + username + ": " + token);
        System.out.println("Scan the QR code or enter manually the code");

        System.out.print("Give the following code: ");
        String code = scanner.nextLine();

        if (token.equals(code)) {
            System.out.println("Welcome " + username);
        } else {
            System.out.println("The code you entered does not match the one timer");
        }
    }

    public static void generateQR(String data, String fileName) throws Exception {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(data, BarcodeFormat.QR_CODE, 200, 200);
        Path path = new File(fileName).toPath();
        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
    }
}
