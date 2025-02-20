1 -instalar as dependencias na pasta do JEDI, é so executar no cli
	>>pip3 install numpy scipy pyQt5 datetime matplotlib pathos

2- para facilitar apenas precisamos rodar o GUI no root do JEDI
	>>python3 tragen_gui.py	

3- Quando abrir o GUI a interface está enumerada com os passos

		1- escolher o tamanho do trace(Numero de request)
		2- unidade do trafego (usamos Request/second)
		3- classe do trafego( primeira coluna da tabela) e o volume deste trafego (terceira coluna da tabela)
			3.1-
		4- aperta o botao

4- o arquivo vai estar em outPut do repositorio
	4.1 - A formatacao dos dados estao assim
		timestamp, object_id, object_size (KB)
		1532702631,0,26624
		1532702631,1,12288
		1532702631,2,26624
		1532702631,3,26624


