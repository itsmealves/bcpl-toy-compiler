# Erro sintático
# RK não encontrado

LET f() BE $(

	TEST 10 = 2 THEN $(
		WRITE(TRUE);
	ELSE $(
		WRITE(FALSE);
	$)
$)