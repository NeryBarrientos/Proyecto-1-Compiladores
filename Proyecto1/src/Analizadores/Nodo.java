package Analizadores;
abstract class Nodo {
    public abstract String toString();
    public abstract void print(String prefix);
}

class NodoOperacion extends Nodo {
    Nodo operador;
    Nodo conjuntoIzq;
    Nodo conjuntoDer;

    public NodoOperacion(Nodo operador, Nodo conjuntoIzq, Nodo conjuntoDer) {
        this.operador = operador;
        this.conjuntoIzq = conjuntoIzq;
        this.conjuntoDer = conjuntoDer;
    }
    public NodoOperacion(Nodo operador, Nodo conjuntoIzq) {
        this.operador = operador;
        this.conjuntoIzq = conjuntoIzq;
        this.conjuntoDer = null;
    }
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "NodoOperacion: " + operador);
        if (conjuntoIzq != null) {
            conjuntoIzq.print(prefix + "  ");
        }
        if (conjuntoDer != null) {
            conjuntoDer.print(prefix + "  ");
        }
    }

    @Override
    public String toString() {
        if (conjuntoDer == null) {
            return "(" + operador + " " + conjuntoIzq + ")";
        }else {
            return "(" + conjuntoIzq + " " + operador + " " + conjuntoDer + ")";
        }
    }

}

class NodoOperador extends Nodo {
    String operador;

    public NodoOperador(String operador) {
        this.operador = operador;
    }

    @Override
    public String toString() {
        return operador;
    }
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "NodoOperador: " + operador);
    }
}

class NodoConjunto extends Nodo {
    String id;

    public NodoConjunto(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id;
    }
    @Override
    public void print(String prefix) {
        System.out.println(prefix + "NodoConjunto: " + id);
    }
}
