package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Aluno {

    private int matricula;
    private String nome;
    private int idade;
    private Date dataNascimento;
    private String telefone;
    private String cpf;

    public Aluno() {

    }

    public Aluno(int matricula, String nome, int idade, Date dataNascimento, String telefone, String cpf) {
        this.matricula = matricula;
        this.nome = nome;
        this.idade = idade;
        this.dataNascimento = dataNascimento;
        this.telefone = telefone;
        this.cpf = cpf;
    }

    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    private String formatarCPF(String cpf) {
        String cpfFormatado = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

        return cpfFormatado;
    }

    /**
     * Sobrescreve o metodo toString da classe Object para exibir as informacoes.
     */
    @Override
    public String toString() {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        return "Matrícula: " + matricula + "\n" +
                "Nome: " + nome + "\n" +
                "Idade: " + idade + "\n" +
                "Data de Nascimento: " + format.format(dataNascimento) + "\n" +
                "Telefone: " + telefone + "\n" +
                "CPF: " + formatarCPF(cpf) + "\n";
    }
}
