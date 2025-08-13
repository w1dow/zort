//Handles selection of folder which is to be sorted

package project.frontend;

import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CompletableFuture;

import project.App;

public class frontend {

    // Select folder
    static String slf(ActionEvent e, Frame f) {
        String res = "NO";
        // System.out.println(res);
        FileDialog fs = new FileDialog(f, "Select folder!", FileDialog.LOAD);
        fs.setVisible(true);
        res = fs.getDirectory();
        if (res == null) {
            res = "NO";
            return res;
        }
        App.foldername = res;
        fh.loadfiles(App.foldername);
        return res;
    }

    public static CompletableFuture<String> prepare() {
        CompletableFuture<String> future = new CompletableFuture<>();
        // Initiator
        Frame f = new Frame("Zort");
        Button sfbtn = new Button("Select folder");
        Button sortbtn = new Button("Sort");
        Label fldname = new Label("Select folder");

        // Canvas to show logo
        Image logo = Toolkit.getDefaultToolkit().getImage("lib\\icon.png");
        Canvas logoCnvs = new Canvas() {
            public void paint(Graphics g) {
                int w = getWidth(), h = getHeight(), iw = logo.getWidth(this), ih = logo.getHeight(this);
                double r = Math.min((double) w / iw, (double) h / ih);
                g.drawImage(logo, (w - (int) (iw * r)) / 2, (h - (int) (ih * r)) / 2, (int) (iw * r), (int) (ih * r),
                        this);
            }
        };
        logoCnvs.setPreferredSize(new Dimension(680, 300));

        // Setting size

        sfbtn.setPreferredSize(new Dimension(200, 50));
        sfbtn.setFont(new Font("Consolas", Font.PLAIN, 14));
        sfbtn.setFocusable(false);
        sfbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        sortbtn.setPreferredSize(new Dimension(200, 50));
        sortbtn.setFont(new Font("Consolas", Font.PLAIN, 14));
        sortbtn.setFocusable(false);
        sortbtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        f.setSize(700, 500);
        f.setResizable(false);
        f.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        fldname.setPreferredSize(new Dimension(660, 30));
        fldname.setFont(new Font("Consolas", Font.BOLD, 20));

        // adding things to the frame
        f.add(logoCnvs);
        f.add(fldname);
        f.add(sfbtn);
        f.add(sortbtn);

        // making it visible
        f.setVisible(true);
        f.setResizable(false);

        // Event_listeners

        sfbtn.addActionListener(e -> {
            slf(e, f);
            fldname.setForeground(new Color(2, 113, 72));
            fldname.setText("Selected Folder ->   " + App.foldername + " \n || Total files:" + App.filecount);
        });

        sortbtn.addActionListener(e -> {
            String msg = App.foldername;
            if (msg == "NO") {
                fldname.setForeground(Color.RED);
                fldname.setText("Please select a folder");

            }
            // System.out.println(msg);
            future.complete(msg);
        });

        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Window closing....");
                f.dispose();
            }
        });
        return future;
    }

    public static void nmface(String imgPath) {
        Frame f = new Frame("Enter name");
        f.setSize(700, 500);
        f.setResizable(false);
        f.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

        // Load the image
        Image img = Toolkit.getDefaultToolkit().getImage(imgPath);
        // System.out.println(img);

        // Canvas to draw scaled image
        Canvas imgCanvas = new Canvas() {
            public void paint(Graphics g) {
                int w = getWidth(), h = getHeight();
                int iw = img.getWidth(this), ih = img.getHeight(this);
                if (iw > 0 && ih > 0) { // only draw if image is loaded
                    double r = Math.min((double) w / iw, (double) h / ih);
                    int nw = (int) (iw * r);
                    int nh = (int) (ih * r);
                    g.drawImage(img, (w - nw) / 2, (h - nh) / 2, nw, nh, this);
                }
            }
        };
        imgCanvas.setPreferredSize(new Dimension(680, 300));

        // Text field
        TextField nameField = new TextField(30); // 30 columns
        nameField.setFont(new Font("Consolas", Font.PLAIN, 16));

        // OK button
        Button okBtn = new Button("OK");
        okBtn.setPreferredSize(new Dimension(100, 40));
        okBtn.setFont(new Font("Consolas", Font.PLAIN, 16));
        okBtn.setFocusable(false);
        okBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Add components to frame
        f.add(imgCanvas);
        f.add(nameField);
        f.add(okBtn);

        // Show the frame
        f.setVisible(true);

        // Handle button click
        okBtn.addActionListener(e -> {
            String name = nameField.getText();
            if (name != null && !name.trim().isEmpty()) {
                // System.out.println("User entered: " + name);
                fh.rnm(imgPath, name);
                f.dispose();
            } else {
                System.out.println("Text box is empty!");
            }
        });

        // handle closing
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.out.println("Window closing....");
                f.dispose();
            }
        });

    }

}
