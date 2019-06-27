GLOBAL $(
	INT c : 0;
	INT max : 20;
	BOOLEAN x : TRUE;
$)

LET start() BE $(
	TEST x THEN $(
		c := c + (10 / 10);
		IF c >= max THEN $(
			x := FALSE;
		$)

		start();
	$)
	ELSE $(
		WRITE(TRUE);
	$)
$)