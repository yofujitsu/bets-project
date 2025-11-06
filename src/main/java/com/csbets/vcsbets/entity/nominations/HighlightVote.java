package com.csbets.vcsbets.entity.nominations;

import com.csbets.vcsbets.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "highlight_vote")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HighlightVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private User voter;
    @ManyToOne
    private HighlightNomination highlight;
}
