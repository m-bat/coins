/* bobble sort program */
/* for statement test program */
int printf(char *s, ...);
int BubSort(int x[ ], int n);
void ShowData(int x[ ], int n);

int  NUM=10;
int x[ ] = {9, 4, 6, 2, 1, 8, 0, 3, 7, 5};	
	
int BubSort(int x[ ], int n)
{
	int i, j, temp;
	
	for (i = 0; i < n - 1; i++) {
		for (j = n - 1; j > i; j--) {
			if (x[j - 1] > x[j]) {	
				temp = x[j];	
				x[j] = x[j - 1];
				x[j - 1]= temp;
			}
		}	
        }
        return 0;
}
	
void ShowData(int x[ ], int n)
{
	int i;	
	for (i = 0; i < n ; i++)
		printf("%d\t", x[i]);
}

int  main()
{		
	printf("-- before sort -- NUM =%d\n", NUM);
	ShowData(x, NUM);

	BubSort(x, NUM);

        printf("-- after sort -- NUM =%d\n", NUM);
	ShowData(x, NUM);
        return 0;
}

