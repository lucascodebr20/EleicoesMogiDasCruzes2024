package org.example.Service;

import org.example.Model.Candidato;
import org.example.Model.Sessao;
import org.example.Model.Voto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessaoService {

    private List<Candidato> candidatos;
    private List <Voto> votos;
    private List <Sessao> sessoes;

    public SessaoService(List<Candidato> candidatos, List<Voto> votos, List<Sessao> sessoes) {
        this.candidatos = candidatos;
        this.votos = votos;
        this.sessoes = sessoes;
    }

    public List<String> listaBairros() {
        Set<String> nomesBairros = new HashSet<>();
        sessoes.removeIf(sessao -> !nomesBairros.add(sessao.getBairro()));
        return new ArrayList<>(nomesBairros);
    }







}
