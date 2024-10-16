package org.example.View;

import org.example.Model.Candidato;
import org.example.Model.Sessao;
import org.example.Model.Voto;
import org.example.Service.CandidatoService;
import org.example.Util.GeradorPDF;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class View {

    private List<Candidato> candidatos;
    private List<Voto> votos;
    private List<Sessao> sessoes;

    public View(List<Candidato> candidatos, List<Voto> votos, List<Sessao> sessoes) {
        this.candidatos = candidatos;
        this.votos = votos;
        this.sessoes = sessoes;

    }

    public void menuPrincipal() throws IOException {
        Scanner scanner = new Scanner(System.in);
        int opcao = 0;

        while (opcao != 4) {
            System.out.println("= ---------------- Menu ---------------- =");
            System.out.println("| 1 - Analisar Model.CandidatoService    |");
            System.out.println("| 2 - Consultar por Sessão               |");
            System.out.println("| 3 - Consultar por Região               |");
            System.out.println("| 4 - Gerar PDF do candidato             |");
            System.out.println("| 5 - Sair                               |");
            System.out.println("= -------------------------------------- =");
            System.out.print("\nEscolha uma opção: ");

            opcao = scanner.nextInt();

            switch (opcao) {
                case 1:
                    analisarCandidato();
                    break;
                case 2:
                    analisarSessao();
                    break;
                case 3:
                    analisarRegiao();
                    break;
                case 4:
                    gerarPDFdoCandidato();
                    break;
                case 5:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }

        scanner.close();
    }

    public void analisarCandidato() {
        CandidatoService candidatoService = new CandidatoService(candidatos, votos, sessoes);
        Scanner sc = new Scanner(System.in);
        System.out.print("\nDigite o numero do candidato: ");
        String numeroCandidato = sc.nextLine();

        Map<Sessao, Integer> sessaoIntegerMap = candidatoService.colegioMaisVotadasDeUmCandidato(numeroCandidato);

        System.out.println();
        System.out.println("SESSÂO MAIS VOTADAS");

        int contador = 0;

        for (Map.Entry<Sessao, Integer> entry : sessaoIntegerMap.entrySet()) {
            if (contador == 5) {
                break;
            }
            Sessao sessao = entry.getKey();
            Integer votos = entry.getValue();
            System.out.println();
            System.out.println(
                    "----------------------------" + "\n" +
                            " ESCOLA: " + sessao.getColegio() + "\n" +
                            " LOGADOURO: " + sessao.getLogadouro() + "\n" +
                            " BAIRRO: " + sessao.getBairro() + "\n" +
                            " TOTAL DE VOTOS: " + votos);
            contador++;
        }

        System.out.println();

        Map<String, Integer> bairrosMaisVotado = candidatoService.bairrosMaisVotadasDeUmCandidato(numeroCandidato);

        System.out.println("BAIRRO MAIS VOTADOS!");

        int contador2 = 0;

        for (Map.Entry<String, Integer> entry : bairrosMaisVotado.entrySet()) {
            if (contador2 == 10) {
                break;
            }
            String bairro = entry.getKey();
            Integer votos = entry.getValue();
            System.out.println(
                    "----------------------------" + "\n" +
                            " BAIRRO: " + bairro + "\n" +
                            " TOTAL DE VOTOS: " + votos);
            contador2++;
        }

        System.out.println();

    }

    public void analisarSessao() {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nDigite o numero da Sessão: ");
        String sessao = sc.nextLine();
        System.out.print("\nDigite o numero da Zona Eleitoral ");
        String zonaEleitoral = sc.nextLine();

        CandidatoService candidatoService = new CandidatoService(candidatos, votos, sessoes);


        List<Candidato> candidatosPorSessao = candidatoService.rankCandidatosPorSessao(sessao, zonaEleitoral);

        System.out.println("RANKING DE CANDIDATOS");
        System.out.println("SESSÃO: " + sessao);
        System.out.println("ZONA ELEITORAL: " + zonaEleitoral);

        candidatosPorSessao.stream().forEach(candidato -> {
            System.out.println(

                    " | " +
                            " | CANDIDATO: " + candidato.getNome() +
                            " | NUMERO CANDIDATO: " + candidato.getNumeroCandidato() +
                            " | TOTAL DE VOTOS: " + candidato.totalVotos());
        });


    }


    public void analisarRegiao() {
        CandidatoService candidatoService = new CandidatoService(candidatos, votos, sessoes);
        Scanner sc = new Scanner(System.in);
        System.out.println("Bairros disponíveis:");
        List<String> bairros = candidatoService.listaBairros();

        bairros.forEach(System.out::println);

        System.out.print("\nDigite o nome do bairro: ");
        String bairroSelecionado = sc.nextLine();

        Map<Candidato, Integer> candidatoVotadoBairro = candidatoService.rankCandidatoVotadoBairro(bairroSelecionado);

        System.out.println("RANKING DE CANDIDATOS");
        System.out.println("BAIRRO: " + bairroSelecionado);

        int contador = 0;
        for (Map.Entry<Candidato, Integer> entry : candidatoVotadoBairro.entrySet()) {
            if (contador == 50) {
                break;
            }
            Candidato candidato = entry.getKey();
            Integer votos = entry.getValue();
            System.out.println(

                    " | " + (contador + 1) +
                            " | CANDIDATO: " + candidato.getNome() +
                            " | NUMERO CANDIDATO: " + candidato.getNumeroCandidato() +
                            " | TOTAL DE VOTOS: " + votos);

            contador++;
        }
    }

    public void gerarPDFdoCandidato() throws IOException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nDigite o número do candidato: ");
        String numeroCandidato = scanner.next();

        GeradorPDF geradorPDF = new GeradorPDF(candidatos, votos, sessoes);

        if (candidatos.stream().anyMatch(candidato -> candidato.getNumeroCandidato().equalsIgnoreCase(numeroCandidato))) {
            geradorPDF.gerarPDF(numeroCandidato);
        } else {
            System.out.println("\nErro: Candidato não encontrado");
        }

        scanner.close();
    }

}






