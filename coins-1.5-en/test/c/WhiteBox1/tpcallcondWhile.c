/* tpcallcondWhile.c  Call function with while in conditional exp */

int printf(char*, ...);

int tester(int x){
  int a;
  a = 0;
  if (a > 10)
    goto lab8;
  while (a < 100){
	if (a == x * x)
	   return 1;
  a++;
  }
lab8:
  return 0;
}
main(){
  int a;	
  a = 1;
  if (a > 10)
    goto lab8;
  for (a=0;a<=10; a++)
    if (tester(a)) printf("%d\n",a);
    else printf("not %d\n",a); 
  lab8: ; /* ##82 */
}
