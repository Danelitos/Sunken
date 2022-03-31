package org;

import org.modelo.GestorDelJuego;
import org.vista.VentanaPrincipal;

import javax.swing.*;
import java.awt.*;

public class ProgramaPrincipal {

    public static void main(String[] args) {
        System.out.println("███████╗██╗   ██╗███╗   ██╗██╗  ██╗███████╗███╗   ██╗");
        System.out.println("██╔════╝██║   ██║████╗  ██║██║ ██╔╝██╔════╝████╗  ██║");
        System.out.println("███████╗██║   ██║██╔██╗ ██║█████╔╝ █████╗  ██╔██╗ ██║");
        System.out.println("╚════██║██║   ██║██║╚██╗██║██╔═██╗ ██╔══╝  ██║╚██╗██║");
        System.out.println("███████║╚██████╔╝██║ ╚████║██║  ██╗███████╗██║ ╚████║");
        System.out.println("╚══════╝ ╚═════╝ ╚═╝  ╚═══╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═══╝");

        GestorDelJuego.getInstance();



        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new VentanaPrincipal();
            }
        });
    }
}
