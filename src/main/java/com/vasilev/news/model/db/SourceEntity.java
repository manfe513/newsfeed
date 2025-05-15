package com.vasilev.news.model.db;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(
        name = "sources",
        uniqueConstraints = @UniqueConstraint(columnNames = {
                "name",
                "url"
        })
)
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(of = {"id", "url"})
@EqualsAndHashCode(of = {"url"})
public class SourceEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "name")
    private String name;

    @NonNull
    @Column(name = "url")
    private String url;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "rules_id", referencedColumnName = "id")
    private SourceRulesEntity rules;
}
