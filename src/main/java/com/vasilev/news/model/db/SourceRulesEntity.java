package com.vasilev.news.model.db;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "source_rules")
@RequiredArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString(of = {"id", "itemPath"})
@EqualsAndHashCode(of = {"itemPath", "titlePath"})
public class SourceRulesEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NonNull
    @Column(name = "item_path")
    private String itemPath;

    @NonNull
    @Column(name = "title_path")
    private String titlePath;

    @NonNull
    @Column(name = "description_path")
    private String descriptionPath;

    @NonNull
    @Column(name = "date_path")
    private String datePath;

    @NonNull
    @Column(name = "date_format")
    private String dateFormat;
}
