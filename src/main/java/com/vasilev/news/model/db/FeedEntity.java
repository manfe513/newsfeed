package com.vasilev.news.model.db;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "feed")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(of = {"id", "date"})
@EqualsAndHashCode(of = {"date", "source"})
public class FeedEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "date")
    private Instant date;

    @NonNull
    @Column
    private String source;

    @NonNull
    @Column(name = "title")
    private String title;

    @NonNull
    @Column(name = "description")
    private String description;
}
