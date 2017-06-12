/* tpinit11.c:  global variable initiation */
/*     Kitamura mail 030925 */

int printf(char*, ...);

int i[10];
int *ip = i;   /* Compile error 4444. initializer is not constant:i coins-0.9.2 */
int *ip2 = &i; /* implicit cast of incompatible pointer type */

char str1[]= "test";   /* OK */
char str2[5]= "test";  /* OK */
char str[10]="test"; /* incompatible initial value */

char *starr[] = { 
  str, "test2"
};

int plus[]={1, 2};
int minus[]={ -1, -2 };

int *iparr[]={ /* Compile error 4444. initializer is not constant: plus */
  plus, minus
};

int main()
{
  printf("%s %s\n", starr[0],starr[1]);
  printf("%d %d\n",iparr[0][0],iparr[1][1]);
  return 0;
}

