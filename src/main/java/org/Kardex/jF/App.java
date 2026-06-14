package org.Kardex.jF;

import javax.swing.SwingUtilities;
import org.Kardex.jF.view.LoginView;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}