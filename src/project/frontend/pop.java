package project.frontend;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.CompletableFuture;
import javax.imageio.ImageIO;

public class pop {

    public static CompletableFuture<String> nmface(String imgPath) {
        CompletableFuture<String> future = new CompletableFuture<>();
        BufferedImage img;

        // Load image synchronously
        try {
            File imgFile = new File(imgPath);
            if (!imgFile.exists() || !imgFile.isFile()) {
                future.completeExceptionally(new RuntimeException("Image file does not exist: " + imgPath));
                return future;
            }
            img = ImageIO.read(imgFile);
            if (img == null) {
                future.completeExceptionally(new RuntimeException("Failed to read image: " + imgPath));
                return future;
            }
        } catch (Exception e) {
            future.completeExceptionally(e);
            return future;
        }

        final BufferedImage finalImg = img;

        Frame f = new Frame("Enter name");
        f.setResizable(false);
        f.setLayout(new BorderLayout());

        Canvas imgCanvas = new Canvas() {
            public void paint(Graphics g) {
                super.paint(g);
                int w = getWidth();
                int h = getHeight();
                if (finalImg != null && w > 0 && h > 0) {
                    int iw = finalImg.getWidth();
                    int ih = finalImg.getHeight();
                    double r = Math.min((double) w / iw, (double) h / ih);
                    int nw = (int) (iw * r);
                    int nh = (int) (ih * r);
                    g.drawImage(finalImg, (w - nw) / 2, (h - nh) / 2, nw, nh, this);
                } else {
                    g.setColor(Color.RED);
                    g.drawString("Image not available", 10, 20);
                }
            }

            public void update(Graphics g) {
                paint(g);
            }
        };
        imgCanvas.setPreferredSize(new Dimension(680, 300));

        Panel inputPanel = new Panel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        TextField nameField = new TextField(30);
        nameField.setFont(new Font("Consolas", Font.PLAIN, 16));
        inputPanel.add(nameField);

        Button okBtn = new Button("OK");
        okBtn.setPreferredSize(new Dimension(150, 60));
        okBtn.setFont(new Font("Consolas", Font.PLAIN, 18));
        okBtn.setFocusable(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        inputPanel.add(okBtn);

        f.add(imgCanvas, BorderLayout.CENTER);
        f.add(inputPanel, BorderLayout.SOUTH);

        // Pack and show frame
        f.pack();
        f.setLocationRelativeTo(null); // center on screen
        f.setVisible(true);

        // Force repaint after visible to ensure image shows
        imgCanvas.repaint();

        // Request focus for the text field
        nameField.requestFocus();

        okBtn.addActionListener(e -> {
            String name = nameField.getText();
            if (name != null && !name.trim().isEmpty()) {
                future.complete(name);
                f.dispose();
            } else {
                System.out.println("Text box is empty!");
            }
        });

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                if (!future.isDone()) {
                    future.completeExceptionally(new RuntimeException("Window closed before name entered"));
                }
                f.dispose();
            }
        });

        return future;
    }
}
