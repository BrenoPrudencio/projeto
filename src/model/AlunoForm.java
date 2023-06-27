// a) Crie uma Java Bean Aluno com os seguintes atributos: matricula, nome, idade, dataNascimento onde deve ser do tipo date e armazenar no nosso formato dd/mm/yyyy, telefone, CPF,. Deve ter também os métodos Getters e Setters de cada atributo.
// OK.

// b) Crie uma tela e os elementos do Swing, em que devem ser salvos alunos e adicionados em uma lista. Não é permitido adicionar aluno com a mesma matricula.
// OK.

// c) Obtenha o terceiro elemento da lista e imprima a quantidade de elementos da lista.
// Obs.: Pq exatamente o terceiro da lista??? Só podem ter 3 alunos cadastrados??? Achei esse requisito um pouco vago.

// d) Obter a matricula do primeiro e ultimo elemento da lista.
// OK. (Obs.: Achei q nao ficou muito claro o objetivo desse requisito, ja q todas as matriculas serao visiveis na tabela.)

// e) Remover o último elemento da lista.
// OK. 

// f) Percorrer toda a lista e identificar o aluno mais novo e mais velho.
// OK.

// g) Crie um objeto da classe Aluno e verifique se já esta na lista.
// OK.

// h) Insira um novo Aluno na terceira posição.
// Obs.: Pq exatamente na terceira posicao? Nao entendi o objetivo desse requisito.

// i) Cria a documentação usando o Java doc de pelo menos uma classe do seu programa.
// OK.

// j) Os campos abaixo a serem salvos na lista deve ser salvos no seguinte formato: CPF: "XXX.XXX.XXX-XX" e Data de Nascimento: 'dd/mm/yyyy'".
// OK.

// 2 – Crie um botão no formulário anterior, que chame um outro formulário que exibe as informações de todos os alunos salvos em memória.
// OK.

// 3 – Tentar salvar os registros de um aluno no arquivo no formato CSV com o nome "ListagemAlunos".
// OK.

package model;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AlunoForm extends JFrame {
    private List<Aluno> alunos;
    private JTable tabela;

    /**
     * Construtor utilizado para setar o titulo da janela, tamanho da janela e
     * iniciar aplicacao.
     */
    public AlunoForm() {
        alunos = new ArrayList<>();

        setTitle("Cadastro de Alunos");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        criarComponentes();
        carregarAlunos();
    }

    /**
     * Cria o layout da interface grafica com as informacoes solicitadas e os botoes
     * de acao.
     */
    private void criarComponentes() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Matricula");
        modelo.addColumn("Nome");
        modelo.addColumn("Idade");
        modelo.addColumn("Data de Nascimento");
        modelo.addColumn("Telefone");
        modelo.addColumn("CPF");
        tabela = new JTable(modelo);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        JScrollPane scrollPane = new JScrollPane(tabela);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel botaoPainel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton botaoAdicionar = new JButton("Adicionar Aluno");
        botaoAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                adicionarAlunosForm();
            }
        });

        JButton botaoExibirAlunos = new JButton("Mostrar Alunos");
        botaoExibirAlunos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exibirAlunos();
            }
        });

        JButton botaoRemoverUltimoAluno = new JButton("Remover Ultimo Aluno");
        botaoRemoverUltimoAluno.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                removerUltimoAluno();
            }
        });

        JButton botaoVerificarIdade = new JButton("Verificar Idade");
        botaoVerificarIdade.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Aluno maisNovo = encontrarAlunoMaisNovo();
                Aluno maisVelho = encontrarAlunoMaisVelho();
                if (maisNovo != null && maisVelho != null) {
                    String mensagem = "Aluno Mais Novo:\n" + maisNovo.toString() + "\n\n" +
                            "Aluno Mais Velho:\n" + maisVelho.toString();
                    JOptionPane.showMessageDialog(AlunoForm.this, mensagem, "Alunos Mais Novo e Mais Velho",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(AlunoForm.this, "Nao ha alunos cadastrados para verificar.", "Aviso",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        botaoPainel.add(botaoAdicionar);
        botaoPainel.add(botaoExibirAlunos);
        botaoPainel.add(botaoRemoverUltimoAluno);
        botaoPainel.add(botaoVerificarIdade);
        mainPanel.add(botaoPainel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    /**
     * Exibe uma tabela onde serao informados os dados do aluno, caso exista um
     * aluno com a mesma matricula informada, nao sera adicionado um novo aluno.
     */
    private void adicionarAlunosForm() {
        JDialog adicionarAlunosDialog = new JDialog(this, "Adicionar Aluno", true);
        adicionarAlunosDialog.setSize(400, 300);
        adicionarAlunosDialog.setLocationRelativeTo(this);

        JPanel painelAdicionarAlunos = new JPanel(new GridLayout(7, 2, 10, 10));
        painelAdicionarAlunos.setBorder(new EmptyBorder(10, 10, 10, 10));

        JLabel matriculaLabel = new JLabel("Matricula:");
        JLabel nomeLabel = new JLabel("Nome:");
        JLabel idadeLabel = new JLabel("Idade:");
        JLabel dataNascimentoLabel = new JLabel("Data de Nascimento:");
        JLabel telefoneLabel = new JLabel("Telefone:");
        JLabel cpfLabel = new JLabel("CPF:");

        JTextField campoMatricula = new JTextField();
        JTextField campoNome = new JTextField();
        JTextField campoIdade = new JTextField();
        JTextField campoDataNascimento = new JTextField();
        campoDataNascimento.setForeground(Color.GRAY);
        campoDataNascimento.setText("dd/mm/yyyy");

        campoDataNascimento.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoDataNascimento.getText().equals("dd/mm/yyyy")) {
                    campoDataNascimento.setText("");
                    campoDataNascimento.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoDataNascimento.getText().isEmpty()) {
                    campoDataNascimento.setForeground(Color.GRAY);
                    campoDataNascimento.setText("dd/mm/yyyy");
                }
            }
        });

        JTextField campoTelefone = new JTextField();
        JTextField campoCpf = new JTextField();

        campoCpf.setForeground(Color.GRAY);
        campoCpf.setText("XXX.XXX.XXX-XX");

        campoCpf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (campoCpf.getText().equals("XXX.XXX.XXX-XX")) {
                    campoCpf.setText("");
                    campoCpf.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (campoCpf.getText().isEmpty()) {
                    campoCpf.setForeground(Color.GRAY);
                    campoCpf.setText("XXX.XXX.XXX-XX");
                }
            }
        });

        painelAdicionarAlunos.add(matriculaLabel);
        painelAdicionarAlunos.add(campoMatricula);
        painelAdicionarAlunos.add(nomeLabel);
        painelAdicionarAlunos.add(campoNome);
        painelAdicionarAlunos.add(idadeLabel);
        painelAdicionarAlunos.add(campoIdade);
        painelAdicionarAlunos.add(dataNascimentoLabel);
        painelAdicionarAlunos.add(campoDataNascimento);
        painelAdicionarAlunos.add(telefoneLabel);
        painelAdicionarAlunos.add(campoTelefone);
        painelAdicionarAlunos.add(cpfLabel);
        painelAdicionarAlunos.add(campoCpf);

        JButton botaoAdicionar = new JButton("Adicionar");
        botaoAdicionar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int matricula = Integer.parseInt(campoMatricula.getText());
                    String nome = campoNome.getText();
                    int idade = Integer.parseInt(campoIdade.getText());
                    DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date dataNascimento = format.parse(campoDataNascimento.getText());
                    String telefone = campoTelefone.getText();
                    String cpf = campoCpf.getText().replaceAll("[^0-9]", "");
                    if (!validarCPF(cpf)) {
                        JOptionPane.showMessageDialog(adicionarAlunosDialog, "CPF inválido.", "Erro",
                                JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    for (Aluno aluno : alunos) {
                        if (aluno.getMatricula() == matricula) {
                            JOptionPane.showMessageDialog(adicionarAlunosDialog,
                                    "Já existe um aluno com a mesma matricula.",
                                    "Erro", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }

                    Aluno aluno = new Aluno(matricula, nome, idade, dataNascimento, telefone, cpf);
                    alunos.add(aluno);
                    adicionarAlunosDialog.dispose();

                    atualizarTabela();
                    salvarAlunos();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(adicionarAlunosDialog,
                            "Informe valores validos nos campos numericos.", "Erro",
                            JOptionPane.ERROR_MESSAGE);
                } catch (ParseException ex) {
                    JOptionPane.showMessageDialog(adicionarAlunosDialog,
                            "Formato de data invalido, use o formato dd/mm/yyyy.",
                            "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        painelAdicionarAlunos.add(botaoAdicionar);
        adicionarAlunosDialog.add(painelAdicionarAlunos);
        adicionarAlunosDialog.setVisible(true);
    }

    /**
     * Atualiza as informacoes da tabela
     */
    private void atualizarTabela() {
        DefaultTableModel modelo = (DefaultTableModel) tabela.getModel();
        modelo.setRowCount(0);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        for (Aluno aluno : alunos) {
            Object[] row = {
                    aluno.getMatricula(),
                    aluno.getNome(),
                    aluno.getIdade(),
                    dateFormat.format(aluno.getDataNascimento()),
                    aluno.getTelefone(),
                    formatarCPF(aluno.getCpf())
            };

            modelo.addRow(row);
        }
    }

    /**
     * Exibe a lista de alunos cadastrados, caso nao haja nenhum cadastro, sera
     * exibida a mensagem de erro.
     */
    private void exibirAlunos() {
        if (alunos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nenhum aluno cadastrado.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        } else {
            StringBuilder alunosInfo = new StringBuilder();

            for (Aluno aluno : alunos) {
                alunosInfo.append(aluno.toString());
                alunosInfo.append("\n");
            }

            JOptionPane.showMessageDialog(this, alunosInfo.toString(), "Alunos Cadastrados",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Remove o ultimo aluno cadastrado pelo indice.
     */
    private void removerUltimoAluno() {
        if (!alunos.isEmpty()) {
            int ultimoIndice = alunos.size() - 1;
            alunos.remove(ultimoIndice);
            atualizarTabela();
            salvarAlunos();
        } else {
            JOptionPane.showMessageDialog(this, "A lista de alunos esta vazia.", "Aviso",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Percorre a lista de alunos e verifica a idade do aluno mais novo.
     */
    private Aluno encontrarAlunoMaisNovo() {
        if (alunos.isEmpty())
            return null;

        Aluno maisNovo = alunos.get(0);
        for (int i = 1; i < alunos.size(); i++) {
            Aluno aluno = alunos.get(i);
            if (aluno.getIdade() < maisNovo.getIdade())
                maisNovo = aluno;
        }

        return maisNovo;
    }

    /**
     * Percorre a lista de alunos e verifica a idade do aluno mais velho.
     */
    private Aluno encontrarAlunoMaisVelho() {
        if (alunos.isEmpty())
            return null;

        Aluno maisVelho = alunos.get(0);
        for (int i = 1; i < alunos.size(); i++) {
            Aluno aluno = alunos.get(i);
            if (aluno.getIdade() > maisVelho.getIdade())
                maisVelho = aluno;
        }

        return maisVelho;
    }

    /**
     * Valida o CPF inserido.
     */
    private boolean validarCPF(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11)
            return false;

        return cpf.matches("\\d{11}");
    }

    /**
     * Formata o CPF para ser exibido na tabela.
     */
    private String formatarCPF(String cpf) {
        String cpfFormatado = cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");

        return cpfFormatado;
    }

    /**
     * Salva as informacoes dos alunos cadastrados no arquivo CSV.
     */
    private void salvarAlunos() {
        try (PrintWriter writer = new PrintWriter(new FileWriter("ListagemAlunos.csv"))) {
            writer.println("Matricula,Nome,Idade,Data de Nascimento,Telefone,CPF");
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            for (Aluno aluno : alunos) {
                writer.println(aluno.getMatricula() + "," +
                        aluno.getNome() + "," +
                        aluno.getIdade() + "," +
                        format.format(aluno.getDataNascimento()) + "," +
                        aluno.getTelefone() + "," +
                        formatarCPF(aluno.getCpf()));
            }

            JOptionPane.showMessageDialog(this, "Dados dos alunos salvos com sucesso.", "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados dos alunos em arquivo.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carrega os dados ja cadastrados no CSV e exibe na tabela.
     */
    private void carregarAlunos() {
        try (BufferedReader reader = new BufferedReader(new FileReader("ListagemAlunos.csv"))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    continue; // Ignorar a primeira linha (cabeçalho)
                }

                String[] parts = line.split(",");
                if (parts.length == 6) {
                    int matricula = Integer.parseInt(parts[0]);
                    String nome = parts[1];
                    int idade = Integer.parseInt(parts[2]);
                    Date dataNascimento = new SimpleDateFormat("dd/MM/yyyy").parse(parts[3]);
                    String telefone = parts[4];
                    String cpf = parts[5];

                    Aluno aluno = new Aluno(matricula, nome, idade, dataNascimento, telefone, cpf);
                    alunos.add(aluno);
                }
            }

            atualizarTabela();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar os dados dos alunos do arquivo.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Erro ao converter a data de nascimento dos alunos.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Erro ao converter a matrícula ou a idade dos alunos.", "Erro",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

}