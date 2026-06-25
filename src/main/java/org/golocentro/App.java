package org.golocentro;

import javax.swing.SwingUtilities;
import org.golocentro.view.MarcoPrincipalView;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new MarcoPrincipalView().setVisible(true);
        });
    }
}
