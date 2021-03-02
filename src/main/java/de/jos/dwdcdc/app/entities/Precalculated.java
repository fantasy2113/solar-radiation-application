package de.jos.dwdcdc.app.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "precalculated")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Precalculated implements Serializable {

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = IDENTITY)
  private Long id;

  @Column(name = "tilt")
  private int tilt;

  @Column(name = "alignment")
  private int alignment;

  @Column(name = "value")
  private int value;

  @Column(name = "longitude")
  private int longitude;

  @Column(name = "latitude")
  private int latitude;
}
