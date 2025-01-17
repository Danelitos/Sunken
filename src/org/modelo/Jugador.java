package org.modelo;

import org.modelo.barco.*;
import org.modelo.excepciones.ImposibleColocarBarcoException;
import org.modelo.excepciones.ImposibleColocarEscudoException;
import org.modelo.excepciones.ImposibleDispararException;
import org.modelo.excepciones.ImposibleUsarRadarException;
import org.modelo.misil.*;
import org.modelo.radar.Radar;
import org.modelo.radar.Radar3x3;

import java.util.ArrayList;
import java.util.Random;

public class Jugador implements Entidad{
	private Tablero tablero;
	private ListaBarcos listaBarcos;
	private ListaMisiles listaMisiles;
	private Radar radar;

	private int numEscudos = 3;
	private int dineroJugador = 10;
	private int numReparar=1;

	public Jugador() {
		this.tablero=new Tablero(false);
		this.listaBarcos=new GeneradorDeBarcos().generarListaBarcos();
		this.listaMisiles=new GeneradorDeMisiles().generarListaMisiles();
		radar=new Radar3x3();
	}

	private boolean posValidaDisparo(int pos){
		boolean valida = true;
		if(pos< 0||100<=pos)valida =false;
		if(valida &&ListaJugadores.getInstance().getEntidad(1).getEstadoCasilla(pos).equals(EEstadoCasilla.HUNDIDO))valida =false;
		if(valida &&ListaJugadores.getInstance().getEntidad(1).getEstadoCasilla(pos).equals(EEstadoCasilla.AGUADISPARO))valida =false;
		if(valida &&ListaJugadores.getInstance().getEntidad(1).getEstadoCasilla(pos).equals(EEstadoCasilla.BARCOHUNDIDO))valida =false;

		return valida;
	}

	// BARCOS --------
	@Override
	public void colocarBarco(int pPos, ETipoBarco pTipoBarco, EOrientaconBarco pOrientacion) throws ImposibleColocarBarcoException {
		Barco barco = listaBarcos.obtenerBarcoNoColocado(pTipoBarco);

		// Si hay un barco disponible comprobamos si se puede colocar en la posicion
		if(barco != null){
			// Si se puede colocar, lo colocamos y actualizamos el estado del barco
			if(tablero.sePuedeColocar(pPos,pOrientacion,barco)){
				tablero.colocarBarco(pPos,pOrientacion,barco);
				barco.actualizarBarcoColocado();

			}else{
				throw new ImposibleColocarBarcoException();
			}

		}else{
			throw new ImposibleColocarBarcoException();
		}
	}

	@Override
	public void colocarBarco() {
		int i = 0;
		Barco b1;
		while ((b1 = listaBarcos.obtenerBarcoEnPos(i)) != null) {
			int posicion = this.obtPosBarco();
			EOrientaconBarco orientacion = this.obtOrientacionBarco();

			if (this.tablero.sePuedeColocar(posicion, orientacion, b1)) {
				this.tablero.colocarBarco(posicion, orientacion, b1);
				i++;
				b1.actualizarBarcoColocado();
			}

		}
	}

	private int obtPosBarco() {
		return new Random().nextInt(100);
	}

	private EOrientaconBarco obtOrientacionBarco() {
		//Si random es 0 la orientacion es horizontal y si es 1 vertical
		Random r = new Random();
		int queOrientacion = r.nextInt(2);

		EOrientaconBarco orientacion;
		if (queOrientacion == 0) {
			orientacion = EOrientaconBarco.ESTE;
		} else {
			orientacion = EOrientaconBarco.SUR;
		}

		return orientacion;
	}

	@Override
	public boolean estanTodosBarcosColocados() {
		return listaBarcos.estanTodosBarcosColocados();
	}

	@Override
	public boolean hayBarcosSinHundir() {
		return listaBarcos.hayBarcosSinHundir();
	}

	@Override
	public void imprimirBarcos() {
		System.out.println("--------------------------- JUGADOR ---------------------------");
		listaBarcos.imprimirBarcos();
	}

	@Override
	public Integer obtenerNumBarcosNoColocados(ETipoBarco tipoBarco) {
		return listaBarcos.obtenerNumBarcosNoColocados(tipoBarco);
	}

	@Override
	public Integer obtenerNumBarcosNoHundidos(ETipoBarco tipoBarco) {
		return listaBarcos.obtenerNumBarcosNoHundidos(tipoBarco);
	}

	// DISPAROS --------
	@Override
	public void dispararBarco(ETipoMisil pTipo, int casillaPos, int pId, boolean pEnemigo) {
		Barco aux=null;
		int cont=0;
		boolean enc=false;
		while(cont<this.listaBarcos.size() && !enc){
			if(this.listaBarcos.obtenerBarcoEnPos(cont).esBarcoId(pId)){
				enc=true;
				aux=this.listaBarcos.obtenerBarcoEnPos(cont);
				aux.recibirDisparoBarco(pTipo, casillaPos, pEnemigo);
			}
			cont++;
		}
	}

	@Override
	public void realizarDisparo(ETipoMisil pTipo, int pPos) throws ImposibleDispararException {
		// Comprobamos si el misil esta disponible
		if (posValidaDisparo(pPos) && listaMisiles.sePuedeDisparar(pTipo)) {
			ArrayList<Integer> posicionesDisparo = listaMisiles.obtAreaMisil(pTipo, pPos, 10);
			System.out.println(" -> disparando: " + posicionesDisparo.toString());
			int i=ListaJugadores.getInstance().getEntidad(1).recibirDisparo(pTipo, posicionesDisparo);
		}else{
			throw new ImposibleDispararException();
		}
	}

	@Override
	public void realizarDisparo() {}

	@Override
	public int recibirDisparo(ETipoMisil pTipo, ArrayList<Integer> posicionesDisparo) {
		return(tablero.actualizarCasillasDisparo(pTipo, posicionesDisparo));

	}
	
	//REALIZAR ACCION -------- 
	//Esto s�lo lo utiliza el enemigo
	@Override
	public boolean realizarAccion(boolean juegoTerminado) throws ImposibleUsarRadarException{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Integer obtenerNumMisilesDisponibles(ETipoMisil tipoMisil) {
		return listaMisiles.obtenerNumMisilesDisponibles(tipoMisil);
	}

	// RADAR --------
	@Override
	public void usarRadar() throws ImposibleUsarRadarException {
		if(radar.sePuedeUtilizar()){
			ListaJugadores.getInstance().getEntidad(1).revelarCasillasRadar(radar.obtenerPosicionesReveladas(10, false));

		}else{
			throw new ImposibleUsarRadarException();
		}
	}

	@Override
	public void revelarCasillasRadar(ArrayList<Integer> posciones) {
		tablero.revelarCasillasRadar(posciones);
	}

	@Override
	public void recolocarRadar() {
		if(radar == null) radar = new Radar3x3();
		radar.cambiarPosicionRadar(false);
	}

	@Override
	public void colocarRadarEnCasilla(int posRadarAnt, int posRadarAct) {
		tablero.quitarRadarEnCasilla(posRadarAnt);
		tablero.colocarRadarEnCasilla(posRadarAct);
	}

	@Override
	public Integer obtenerNumUsosRadar() {
		if(radar == null)
			return -1;
		return radar.obtenerNumUsos();
	}

	// CASILLAS --------
	@Override
	public void actualizarContorno(ArrayList<Integer> pLista) {
		this.tablero.actualizarContorno(pLista);
	}

	@Override
	public void actualizarEstadoCasilla(int pCasilla, EEstadoCasilla pEstado) {
		tablero.actualizarEstadoCasilla(pCasilla, pEstado);
	}

	@Override
	public EEstadoCasilla getEstadoCasilla(int pPos) {
		return tablero.getEstadoCasilla(pPos);
	}

	public void actualizarEstadoCasillaOneTap(int pCasilla, EEstadoCasilla pEstado) {
		tablero.actualizarEstadoCasillaOneTap(pCasilla, pEstado);
	}

	// ESCUDOS --------
	@Override
	public void colocarEscudoBarco(int pCasilla) throws ImposibleColocarEscudoException {
		if(numEscudos > 0){
			Integer idBarco = tablero.obtenerIdBarcoCasilla(pCasilla);

			if(idBarco == null){
				throw new ImposibleColocarEscudoException();
			} else{
				Barco barco = listaBarcos.obtenerBarco(idBarco);
				if(barco == null){
					throw new ImposibleColocarEscudoException();
				} else{
					barco.setEscudo(new EscudoBarco());
					barco.actualizarCasillasEscudo(false);
					numEscudos--;
				}
			}

		}
		else
			throw new ImposibleColocarEscudoException();
	}

	@Override
	public void colocarEscudoBarco(){}
	public void quitarRadar(int pCasilla){
		this.tablero.quitarRadar(pCasilla);
	}

	@Override
	public Integer obtenerNumEscudos() {
		return numEscudos;
	}

	@Override
	public void repararPos(int pCasilla){
		boolean reparado=false;
		Integer idBarco = tablero.obtenerIdBarcoCasilla(pCasilla);
		if(this.numReparar>0&&idBarco!=null){
			Barco barco = listaBarcos.obtenerBarco(idBarco);
			if(!barco.estaHundido()&&barco!=null&&!barco.tieneEscudo()&&this.tablero.getEstadoCasilla(pCasilla)==EEstadoCasilla.HUNDIDO){
				barco.repararPos(pCasilla,false);
				ListaJugadores.getInstance().getEntidad(1).notificarCasReparada(pCasilla);
				this.numReparar--;
				reparado=true;
				System.out.println("Se reparo con exito la casilla "+pCasilla+" con un el id barco:"+idBarco);
			}
		}
		if(!reparado){
			System.out.println("No es posible repararlo");
		}

	}


	@Override
	public void notificarCasReparada(int pCasilla) {

	}

	// TIENDA --------
	@Override
	public void comprarObjeto(EObjetoComprable pObj){
		if(Tienda.getInstance().sePuedeComprar(pObj, dineroJugador)){
			boolean error = false;

			// Incrementamos los usos del objeto comprado
			if(pObj.equals(EObjetoComprable.BOMBAONETAP)) {
				Comprable misil = (Comprable) listaMisiles.obtMisil(ETipoMisil.BOMBAONETAP);
				if(misil != null)
					misil.comprar();
				else
					error = true;
			}

			if(pObj.equals(EObjetoComprable.RADAR3x3)){
				Comprable radar3x3 = (Comprable) radar;
				if(radar3x3 != null)
					radar3x3.comprar();
				else
					error = true;
			}

			// Actualizamos el dinero y la tienda
			if(!error)
				dineroJugador = Tienda.getInstance().comprar(pObj, dineroJugador);
				System.out.println("El Jugador ha comprado " + pObj);
		}

	}

	// Para visualizar el dinero en la interfaz
	@Override
	public int obtenerDineroDisponible(){
		return dineroJugador;
	}

}
