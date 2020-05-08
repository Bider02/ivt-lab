package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore ts1, ts2;

  @BeforeEach
  public void init(){
    ts1 = mock(TorpedoStore.class);
    ts2 = mock(TorpedoStore.class);
    this.ship = new GT4500(ts1, ts2);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(ts1.fire(1)).thenReturn(true);
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(true);

    // Act
    //boolean result = 
    ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    //assertEquals(true, result);
    verify(ts1, times(1)).isEmpty();
    verify(ts1, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(false);
    when(ts1.fire(anyInt())).thenReturn(true);
    when(ts2.fire(anyInt())).thenReturn(true);

    // Act
    //boolean result = 
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    //assertEquals(true, result);
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(3);
    verify(ts2, times(1)).isEmpty();
    verify(ts1, times(1)).isEmpty();
  }


  @Test
  public void fireFirst(){
    // Arrange
    when(ts1.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(ts1, times(1)).fire(1);
  }

  @Test
  public void fireSecond(){
    // Arrange
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    verify(ts1, times(1)).fire(1); //Fist primary

    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);
    verify(ts2, times(1)).fire(1); //Then secondary

    // Assert
    assertEquals(true, result);
    assertEquals(true, result2);
  }

  @Test
  public void fireEmpty(){
    // Arrange
    when(ts1.isEmpty()).thenReturn(true);
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(ts1, times(1)).isEmpty();
    verify(ts1, never()).fire(anyInt());
    verify(ts2, times(1)).isEmpty();
    verify(ts2, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireFailure(){
    // Arrange
    when(ts1.isEmpty()).thenReturn(false);
    when(ts1.fire(1)).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(ts1, times(1)).isEmpty();
    verify(ts1, times(1)).fire(1);
    verify(ts2, never()).isEmpty();
    verify(ts2, never()).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireAll(){
    // Arrange
    when(ts1.fire(anyInt())).thenReturn(true);
    when(ts2.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(ts1, times(1)).isEmpty();
    verify(ts1, times(1)).fire(anyInt());
    verify(ts2, times(1)).isEmpty();
    verify(ts2, times(1)).fire(anyInt());
    assertEquals(true, result);
  }

  @Test
  public void fireAllOneandThree(){
    // Arrange
    when(ts1.fire(anyInt())).thenReturn(true);
    when(ts2.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(3);
    assertEquals(true, result);
  }

  @Test
  public void fireDouble(){
    // Arrange
    when(ts1.fire(anyInt())).thenReturn(true);
    when(ts2.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(ts1, times(2)).isEmpty();
    verify(ts1, times(2)).fire(anyInt());
    verify(ts2, times(1)).isEmpty();
    verify(ts2, never()).fire(anyInt());
    assertEquals(true, result);
    assertEquals(true, result2);
  }


 @Test
  public void fireDoubleOnlyOne(){
    // Arrange
    when(ts2.isEmpty()).thenReturn(true);
    when(ts1.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    when(ts1.isEmpty()).thenReturn(true);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(ts1, times(2)).isEmpty();
    verify(ts1, times(1)).fire(anyInt());
    verify(ts2, times(1)).isEmpty();
    verify(ts2, never()).fire(anyInt());
    assertEquals(true, result);
    assertEquals(false, result2);
  }

  @Test
  public void fireDoubleSecondaryEmpty(){
    // Arrange
    when(ts2.isEmpty()).thenReturn(true);
    when(ts1.fire(anyInt())).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);
    boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(ts2, times(1)).isEmpty();
    verify(ts2, never()).fire(anyInt());
    verify(ts1, times(2)).isEmpty();
    verify(ts1, times(2)).fire(anyInt());
    assertEquals(true, result);
    assertEquals(true, result2);
  }

  @Test
  public void fireAllEmpty(){
    // Arrange
    when(ts1.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(ts1, times(1)).isEmpty();
    assertEquals(false, result);
  }

  @Test
  public void fireFirstEmpty(){
    // Arrange
    when(ts1.isEmpty()).thenReturn(true);
    when(ts2.isEmpty()).thenReturn(false);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(ts1, times(1)).isEmpty();
    assertEquals(false, result);
  }

  @Test
  public void fireAllSecondEmpty(){
    // Arrange
    when(ts1.isEmpty()).thenReturn(false);
    when(ts2.isEmpty()).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(ts1, times(1)).isEmpty();
    verify(ts2, times(1)).isEmpty();
    assertEquals(false, result);
  }

  @Test
  public void fireAllFireOne(){
    // Arrange
    when(ts1.fire(1)).thenReturn(true);
    when(ts2.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(ts1, times(1)).isEmpty();
    verify(ts2, times(1)).isEmpty();
    verify(ts1, times(1)).fire(1);
    verify(ts2, times(1)).fire(3);
    assertEquals(false, result);
  }


  /*@Test
  public void fireNull(){
    // Arrange

    // Act
    boolean result = ship.fireTorpedo(null);

    // Assert
    assertEquals(false, result);
  }*/
}
