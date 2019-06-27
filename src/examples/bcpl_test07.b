# Erro sintático


LET f() BE $(
	INT var : 80;

	TEST 10 = 2 THEN $(
		var := 90;
		WRITE(TRUE);
	$)
	ELSE $(
		WRITE(FALSE)#;
	$)
$)