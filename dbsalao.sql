-- phpMyAdmin SQL Dump
-- version 4.9.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Tempo de geração: 12-Nov-2019 às 19:13
-- Versão do servidor: 10.3.16-MariaDB
-- versão do PHP: 7.3.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Banco de dados: `dbsalao`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `horarios`
--

CREATE TABLE `horarios` (
  `horario` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `horarios_agendados`
--

CREATE TABLE `horarios_agendados` (
  `data` varchar(15) NOT NULL,
  `funcionario` varchar(50) NOT NULL,
  `horario` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tbclientes`
--

CREATE TABLE `tbclientes` (
  `idcli` int(11) NOT NULL,
  `nomecli` varchar(50) NOT NULL,
  `sexocli` varchar(11) DEFAULT NULL,
  `cpfcli` varchar(15) NOT NULL,
  `endcli` varchar(100) NOT NULL,
  `fonecli` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tbhorarios`
--

CREATE TABLE `tbhorarios` (
  `id_hor` int(11) NOT NULL,
  `cliente_hor` varchar(200) NOT NULL,
  `servico_hor` varchar(200) NOT NULL,
  `data_hor` varchar(11) NOT NULL,
  `horario_hor` varchar(11) NOT NULL,
  `profissional_hor` varchar(200) NOT NULL,
  `id_ser` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tbservicos`
--

CREATE TABLE `tbservicos` (
  `id_ser` int(11) NOT NULL,
  `nome_ser` varchar(200) NOT NULL,
  `usuario` varchar(200) NOT NULL,
  `valor_ser` varchar(11) NOT NULL,
  `dura_ser` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Estrutura da tabela `tbusuarios`
--

CREATE TABLE `tbusuarios` (
  `iduser` int(11) NOT NULL,
  `usuario` varchar(50) NOT NULL,
  `fone` varchar(15) NOT NULL,
  `login` varchar(15) NOT NULL,
  `senha` varchar(15) NOT NULL,
  `perfil` varchar(20) NOT NULL,
  `hora_in` varchar(11) DEFAULT NULL,
  `hora_out` varchar(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `tbusuarios`
--

INSERT INTO `tbusuarios` (`iduser`, `usuario`, `fone`, `login`, `senha`, `perfil`, `hora_in`, `hora_out`) VALUES
(1, 'Hans Mateus Boron', '(42)99837-6088', 'hans', '1111', 'admin', '', ''),
(2, 'Mariele Longato', '(42)99958-8672', 'mari', '1111', 'admin', '', ''),
(3, 'Erica Cristina Moro', '(42)99958-8672', 'erica', '1111', 'admin', '', ''),
(4, 'Bill Gates', '(79)34659-2387', 'bill', '1111', 'user', '08:00', '17:00'),
(5, 'Linus Torvalds', '(42)99999-9999', 'linus', '1111', 'user', '08:00', '17:00'),
(6, 'Ricardo Oliveira', '(42)68768-6797', 'ricardo', '1234', 'user', '07:00', '23:00');

--
-- Índices para tabelas despejadas
--

--
-- Índices para tabela `horarios`
--
ALTER TABLE `horarios`
  ADD UNIQUE KEY `horario` (`horario`) USING BTREE;

--
-- Índices para tabela `horarios_agendados`
--
ALTER TABLE `horarios_agendados`
  ADD UNIQUE KEY `agendamento` (`data`,`funcionario`,`horario`);

--
-- Índices para tabela `tbclientes`
--
ALTER TABLE `tbclientes`
  ADD PRIMARY KEY (`idcli`);

--
-- Índices para tabela `tbhorarios`
--
ALTER TABLE `tbhorarios`
  ADD PRIMARY KEY (`id_hor`),
  ADD KEY `fkid_ser` (`id_ser`);

--
-- Índices para tabela `tbservicos`
--
ALTER TABLE `tbservicos`
  ADD PRIMARY KEY (`id_ser`);

--
-- Índices para tabela `tbusuarios`
--
ALTER TABLE `tbusuarios`
  ADD PRIMARY KEY (`iduser`);

--
-- AUTO_INCREMENT de tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `tbclientes`
--
ALTER TABLE `tbclientes`
  MODIFY `idcli` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `tbhorarios`
--
ALTER TABLE `tbhorarios`
  MODIFY `id_hor` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `tbservicos`
--
ALTER TABLE `tbservicos`
  MODIFY `id_ser` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `tbusuarios`
--
ALTER TABLE `tbusuarios`
  MODIFY `iduser` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- Restrições para despejos de tabelas
--

--
-- Limitadores para a tabela `tbhorarios`
--
ALTER TABLE `tbhorarios`
  ADD CONSTRAINT `fkid_ser` FOREIGN KEY (`id_ser`) REFERENCES `tbservicos` (`id_ser`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
