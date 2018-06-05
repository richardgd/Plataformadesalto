package com.example.richardgonzalez.plataformadesalto.POJO;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */
public class Medidas  {
    private String vuelo;
    private String altura;
    private String fecha;

    public String getPotencia() {
        return potencia;
    }

    public void setPotencia(String potencia) {
        this.potencia = potencia;
    }

    public String getApoyo() {
        return apoyo;
    }

    public void setApoyo(String apoyo) {
        this.apoyo = apoyo;
    }

    private String potencia;
    private String apoyo;

    public Medidas(String vuelo, String altura, String fecha, String reaccion, String apoyo, String potencia) {
        this.vuelo = vuelo;
        this.altura = altura;
        this.fecha = fecha;
        this.reaccion = reaccion;
        this.apoyo = apoyo;
        this.potencia = potencia;
    }




    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getVuelo() {
        return vuelo;
    }

    public void setVuelo(String vuelo) {
        this.vuelo = vuelo;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getReaccion() {
        return reaccion;
    }

    public void setReaccion(String reaccion) {
        this.reaccion = reaccion;
    }

    private String reaccion;


}
