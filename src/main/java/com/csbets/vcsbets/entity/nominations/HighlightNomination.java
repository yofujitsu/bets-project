package com.csbets.vcsbets.entity.nominations;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "highlight_nomination")
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class HighlightNomination {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private String clipLink;
    private short votesCount = 0;
    private double votesPercentage = 0.0;
    @OneToMany(mappedBy = "highlight", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<HighlightVote> votes = new ArrayList<>();
}
