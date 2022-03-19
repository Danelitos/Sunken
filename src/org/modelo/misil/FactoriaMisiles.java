package org.modelo.misil;

public class FactoriaMisiles {
    private static FactoriaMisiles miFactoriaMisiles;

    private  FactoriaMisiles(){

    }
    public static FactoriaMisiles getInstance(){
        if (miFactoriaMisiles==null)

            miFactoriaMisiles=new FactoriaMisiles();

        return miFactoriaMisiles;
    }
    public Misil crearMisil(ETipoMisil pTipo){
        Misil misil = null;

        if(pTipo.equals(ETipoMisil.BOMBA))
            misil = new Bomba();

        return misil;
    }
}
