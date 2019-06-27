GLOBAL $(
	# global vars
	INT id.404 : 2;
	INT id.about : 0;
	INT id.homepage : 1;
	BOOLEAN about : FALSE;
	BOOLEAN homepage : TRUE;
$)

LET server() INT $(
	BOOLEAN request : #will define request on next line
	TRUE;

	TEST request = homepage THEN $(
		RETURN id.homepage;
	$)
	ELSE $(
		TEST request = about THEN $(
			RETURN id.about;
		$)
		ELSE $(
			RETURN id.404;
		$)
	$)
$)

LET start() INT $(
    WRITE(TRUE);
    WRITE(VALOF server());

    RETURN 0;
$)