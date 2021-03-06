package com.lcanalejo.deviget.minesweeper.entity;

import com.lcanalejo.deviget.minesweeper.enums.GameStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OrderBy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "game")
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "game_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rows")
    private Integer rows;

    @Column(name = "columns")
    private Integer columns;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GameStatus gameStatus;

    @Column(name = "last_started_time")
    private LocalDateTime lastStartedTime;

    @Column(name = "elapsed_time_milliseconds")
    private Long elapsedTimeInMilliseconds;

    @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
    @OrderBy(clause = "row_position ASC, column_position ASC")
    private List<CellEntity> cells;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

}
