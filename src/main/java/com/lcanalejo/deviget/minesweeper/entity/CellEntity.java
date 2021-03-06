package com.lcanalejo.deviget.minesweeper.entity;

import com.lcanalejo.deviget.minesweeper.enums.CellStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cell")
public class CellEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "row_position")
    private Integer rowPosition;

    @Column(name = "column_position")
    private Integer columnPosition;

    @Column(name = "status")
    private CellStatus cellStatus;

    @Column(name = "mines_around")
    private Integer minesAround;

    @Column(name = "is_mine")
    private Boolean isMine;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private GameEntity game;

}
