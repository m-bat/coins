/* hircLgand1.c

	HIR-C to HIR-base conversion test
*/

/* short circuit (&&) */
void lgand0()
{
	int a, b, c, d;

	if(a&&b)
		1;
	else
		0;
	/*
	if(a)
		if(b)
			1;
		else
			goto LABEL0;
	else
		LABEL0:0;
	*/
}

/* Short circuit (||) */
void lgand1()
{
	int a, b;

	if(a||b)
		1;
	else
		0;
	/*
	if(a)
		LABEL1:1;
	else
		if(b)
			goto LABEL1;
		else
			0;
	*/
}

/* Short circuit (&& ||) */
void lgand2()
{
	int a, b, c, d;

	if(a&&b||c&&d)
		1;
	else
		0;
	/*
	if(a)
		if(b)
			LABEL3:1;
		else
			goto LABEL4;
	else
		LABEL4:
		if(c)
			if(d)
				goto LABEL3;
			else
				goto LABEL5;
		else
			LABEL5:0;
	*/
}

/* Short circuit (expression statement) */
int lgand3()
{
	int a, b, r, *p;

	r = a&&b; /* if(a&&b) r=true; else r=false; */
	*p = a&&b; /* if(a&&b) *p=true; else *p=false; */
	/*
	if(a&&b) A; else B;  is converted again to
	if(a) if(b) A; else goto LABEL; else LABEL:B;
	*/
}

/* Short circuit (complicated left hand side expression statement) */
int lgand4()
{
	int a, b, v[1];

	/* Assignment to complicated l-value is done by using temporal variable. */
	v[0] = a&&b; /* int tmp; if(a&&b) tmp=true; else tmp=false; v[0]=tmp; */
	/*
	if(a&&b) A; else B;  is converted again to 
	if(a) if(b) A; else goto LABEL; else LABEL:B;
	*/
}

/* SF030609[ */
main()
{
	printf("conversion example from HIR-C to HIR-base.\n");
}
/* SF030609] */
