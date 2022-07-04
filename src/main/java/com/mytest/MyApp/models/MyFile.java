package com.mytest.MyApp.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@NoArgsConstructor
@Table(name = "files")
public class MyFile {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="file_name")
    @NotNull
    private String fileName;

    @Column(name = "type")
    private String fileType;

    @Column(name = "data")
    private byte[] data;

    @ManyToOne
    private User user;
}
