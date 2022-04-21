package org.controlador;

import org.ProgramaPrincipal;
import org.modelo.GestorDelJuego;
import org.modelo.barco.EOrientaconBarco;
import org.modelo.barco.ETipoBarco;
import org.modelo.misil.ETipoMisil;
import org.vista.JCasilla;
import org.vista.JDespOrien;
import org.vista.VentanaMenu;
import org.vista.VentanaPrincipal;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ControladorVentanaPrincipal implements MouseListener, ItemListener {

    private static ControladorVentanaPrincipal controlador;
    private ETipoBarco barcoSel = null;
    private boolean escudoBarco = false;
    private EOrientaconBarco orientacionSel = EOrientaconBarco.NORTE;

    private ETipoMisil misilSel = null;

    private ControladorVentanaPrincipal(){}

    public static ControladorVentanaPrincipal getInstance(){
        if(controlador == null) controlador = new ControladorVentanaPrincipal();
        return controlador;
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {

        // Casilla del tablero. Llamar al modelo para colocar barco o disparar misil.
        if(e.getSource() instanceof JCasilla){
            JCasilla casilla = (JCasilla) e.getSource();

            int pos = casilla.getPos();
            boolean casillaEnemigo = casilla.esEnemigo();
            FormularioControlador datos = new FormularioControlador(pos, casillaEnemigo, barcoSel, escudoBarco, orientacionSel, misilSel);

            // Llamamos al modelo con la informacion necesaria
            try {
                GestorDelJuego.getInstance().notificarCasillaPresionada(datos);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        // Botones de control
        if(e.getSource() instanceof JRadioButton){
            JRadioButton boton = (JRadioButton) e.getSource();
            escudoBarco = false;

            // Botones de barcos
            if(boton.getText().equals("Fragata"))
                barcoSel = ETipoBarco.FRAGATA;

            if(boton.getText().equals("Portaviones"))
                barcoSel = ETipoBarco.PORTAVIONES;

            if(boton.getText().equals("Submarino"))
                barcoSel = ETipoBarco.SUBMARINO;

            if(boton.getText().equals("Destructor"))
                barcoSel = ETipoBarco.DESTRUCTOR;

            if(boton.getText().equals("Poner escudo"))
                escudoBarco = true;
            // Botondes de misiles

            if(boton.getText().equals("Bomba"))
                misilSel = ETipoMisil.BOMBA;

            if(boton.getText().equals("BombaTap"))
                misilSel = ETipoMisil.BOMBAONETAP;
        }
        if(e.getSource() instanceof JButton){
            JButton boton = (JButton) e.getSource();
            if(boton.getText().equals("Usar Radar")){
                GestorDelJuego.getInstance().notificarBotonRevelarRadarPresionado();
            }
            if(boton.getText().equals("Recolocar Radar")){
                GestorDelJuego.getInstance().notificarBotonRecolocarRadarPresionado();
            }
            if (boton.getText().equals("REINICIAR PARTIDA")) {
                //TODO Falta hacer el boton reiniciar
                VentanaPrincipal.getInstance().nuevaPartida();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {
        // Casilla del tablero
        if(e.getSource() instanceof JCasilla){
            JCasilla casilla = (JCasilla) e.getSource();
            casilla.setMouseEntered(true);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Casilla del tablero
        if(e.getSource() instanceof JCasilla){
            JCasilla casilla = (JCasilla) e.getSource();
            casilla.setMouseEntered(false);
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {

        // Orientacion del barco seleccionada
        if(e.getSource() instanceof JDespOrien){
            JDespOrien desp = (JDespOrien) e.getSource();

            assert desp.getSelectedItem() != null;
            String selec = desp.getSelectedItem().toString();

            if(selec.equals("Norte"))
                orientacionSel = EOrientaconBarco.NORTE;

            if(selec.equals("Sur"))
                orientacionSel = EOrientaconBarco.SUR;

            if(selec.equals("Este"))
                orientacionSel = EOrientaconBarco.ESTE;

            if(selec.equals("Oeste"))
                orientacionSel = EOrientaconBarco.OESTE;
        }
    }
}
