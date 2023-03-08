create table consultas(

    id bigint not null auto_increment,
    medico_id BIGINT NOT NULL,
    paciente_id BIGINT NOT NULL,
    data DATETIME not null,

    primary key(id),
    constraint fk_consultas_medico_id FOREIGN KEY(medico_id) REFERENCES medicos(id),
    constraint fk_consultas_paciente_id FOREIGN KEY(paciente_id) REFERENCES pacientes(id)

);