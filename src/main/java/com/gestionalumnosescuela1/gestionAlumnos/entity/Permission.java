package com.gestionalumnosescuela1.gestionAlumnos.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    PROFESOR_READ("profesor:read"),
    PROFESOR_UPDATE("profesor:update"),
    PROFESOR_CREATE("profesor:create"),
    PROFESOR_DELETE("profesor:delete"),
    ALUMNO_READ("alumno:read"),
    ALUMNO_UPDATE("alumno:update"),
    ALUMNO_CREATE("alumno:create"),
    ALUMNO_DELETE("alumno:delete")

    ;

    @Getter
    private final String permission;
}
