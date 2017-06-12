main()
{
	int x = 1, y = 2, z[10];
	int *ip;
	double *dp, d, atof(char *);
	int *iq = ip;

	ip = &x;
	y = *ip;
	*ip = 0;
	ip = &z[0];
	
	*ip = 3;
	printf("x:%d y:%d z[0]:%d \n", x, y, z[0]);
	
	*ip = *ip + 10;
	y = *ip + 1;
	*ip += 1;
	++*ip;
	(*ip)++;

	iq = z + 1;
	*iq = *ip;
	dp = &d;
	*dp = 1.2; 
	printf("x:%d y:%d z[0]:%d z[1]:%d d:%f \n", x, y, z[0], z[1], d);
	return 0;

}
