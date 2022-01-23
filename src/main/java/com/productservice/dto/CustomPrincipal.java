package com.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomPrincipal implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String email;

}
