package com.example.richardgonzalez.plataformadesalto.POJO;

/**
 * Created by Richard Gonzalez on 22/05/2018.
 */
public class Deportistas {
    private String nombre;
    private int id;
    private  double peso;
    private String foto;

    public Deportistas(String nombre, int id, double peso, String foto) {
        this.nombre = nombre;
        this.id = id;
        this.peso = peso;
        this.foto = foto;

    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getId() {
        return  id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }
}
