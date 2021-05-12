package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class MonederoTest {
  private Cuenta cuenta;


  @BeforeEach
  void init() {
    cuenta = new Cuenta();
  }

  @Test
  void sePuedeInstanciarCuentaConSaldoInicial(){
    Cuenta cuentaConSaldoInicial = new Cuenta(40);
    assertEquals(40,cuentaConSaldoInicial.getSaldo());
  }
  @Test
  void SeLePuedePonerDeFormaSatisfactoria() {
    cuenta.poner(1500);
    assertEquals(1500,cuenta.getSaldo());
  }

  @Test
  void ThrowExceptionCuandoTratoPonerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.poner(-1500));
  }

  @Test
  void PuedoHacerTresDepositosCorrectamente() {
    cuenta.poner(1500);
    cuenta.poner(456);
    cuenta.poner(1900);
    assertEquals(3856,cuenta.getSaldo());
  }

  @Test
  void NoPuedoHacerMasDeTresDepositosEnUnDia() {
    assertThrows(MaximaCantidadDepositosException.class, () -> {
          cuenta.poner(1500);
          cuenta.poner(456);
          cuenta.poner(1900);
          cuenta.poner(245);

    });
  }
  /*
  @Test
  void PuedoHacerMasDeTresDepositosSiNoSonElMismoDia(){
    // Agrego a mano para simular la fecha
    assertDoesNotThrow(()-> {
      cuenta.agregarMovimiento(LocalDate.of(2000,2,2),100,true);
      cuenta.poner(1500);
      cuenta.poner(456);
      cuenta.poner(1900);
    });

  }*/

  @Test
  void ExtraerMasQueElSaldo() {
    assertThrows(SaldoMenorException.class, () -> {
          cuenta.setSaldo(90); // TODO rompo encapsulamiento
          cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMasDe1000() {
    assertThrows(MaximoExtraccionDiarioException.class, () -> {
      cuenta.setSaldo(5000); // TODO rompo encapsulamiento
      cuenta.sacar(1001);
    });
  }

  @Test
  public void ExtraerMontoNegativo() {
    assertThrows(MontoNegativoException.class, () -> cuenta.sacar(-500));
  }

  @Test
  public void SeSiEsDepositoYFecha(){
    Movimiento movimiento = new Movimiento(LocalDate.now(),100,true);
    assertEquals(true,movimiento.fueDepositado(LocalDate.now()));
  }
  @Test
  public void SeSiEsExtraccionYFecha(){
    Movimiento movimiento = new Movimiento(LocalDate.now(),100,false);
    assertEquals(true,movimiento.fueExtraido(LocalDate.now()));
  }

}